import { test, expect } from '@playwright/test';

const appUrl = '/index.html';

async function seedSettings(page) {
  await page.addInitScript(() => {
    localStorage.setItem('free-ai-rock-config', JSON.stringify({
      endpoint: '/mock/v1/chat/completions',
      apiKey: 'browser-test-key',
      theme: 'dark'
    }));
  });
}

async function mockStreamingProvider(page) {
  await page.route('**/mock/v1/chat/completions', async route => {
    const body = [
      'data: ' + JSON.stringify({ choices: [{ delta: { content: 'Hello' } }] }) + '\n\n',
      'data: ' + JSON.stringify({ choices: [{ delta: { content: ' from Rock.' } }] }) + '\n\n',
      'data: [DONE]\n\n'
    ].join('');
    await route.fulfill({ status: 200, contentType: 'text/event-stream', body });
  });
}

test.describe('FREE AI ROCK browser smoke', () => {
  test('starts a chat, persists it, and renders streamed text', async ({ page }) => {
    await seedSettings(page);
    await mockStreamingProvider(page);
    await page.goto(appUrl);
    await expect(page.locator('#messages')).toContainText('Ask better questions');
    await page.locator('#prompt').fill('Say hello');
    await page.locator('#form').evaluate(form => form.requestSubmit());
    await expect(page.locator('.message.user')).toContainText('Say hello');
    await expect(page.locator('.message.assistant')).toContainText('Hello from Rock.');
    await expect(page.locator('#history')).toContainText('Say hello');
    const saved = await page.evaluate(() => JSON.parse(localStorage.getItem('free-ai-rock-chats')));
    expect(saved).toHaveLength(1);
    expect(saved[0].messages.map(message => message.role)).toEqual(['user', 'assistant']);
  });

  test('stop button aborts an in-flight stream', async ({ page }) => {
    await seedSettings(page);
    await page.route('**/mock/v1/chat/completions', async route => {
      await route.fulfill({ status: 200, contentType: 'text/event-stream', body: 'data: ' + JSON.stringify({ choices: [{ delta: { content: 'partial' } }] }) + '\n\n' });
    });
    await page.goto(appUrl);
    await page.locator('#prompt').fill('Stop this');
    await page.locator('#form').evaluate(form => form.requestSubmit());
    await expect(page.locator('#send')).toContainText('Stop');
    await page.locator('#send').click();
    await expect(page.locator('#send')).toContainText('Send');
    await expect(page.locator('#status')).toContainText('Generation stopped');
  });

  test('switches dark, light, and system themes', async ({ page }) => {
    await seedSettings(page);
    await page.goto(appUrl);
    await page.locator('#settings').click();
    await page.locator('#theme').selectOption('light');
    await page.locator('#saveSettings').click();
    await expect(page.locator('html')).toHaveAttribute('data-theme', 'light');
    await page.locator('#settings').click();
    await page.locator('#theme').selectOption('dark');
    await page.locator('#saveSettings').click();
    await expect(page.locator('html')).toHaveAttribute('data-theme', 'dark');
    await page.locator('#settings').click();
    await page.locator('#theme').selectOption('system');
    await page.locator('#saveSettings').click();
    await expect(page.locator('html')).toHaveAttribute('data-theme', /light|dark/);
  });
});
