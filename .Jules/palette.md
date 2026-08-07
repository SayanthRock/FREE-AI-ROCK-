# Palette's Journal - FREE-AI-ROCK

This journal tracks critical UX/accessibility learnings specific to this application's components and design system.

## 2025-02-14 - Auto-Extraction from Raw URLs on Paste
**Learning:** Users naturally copy the entire browser URL (like a full PR link or a raw code link) rather than manually extracting the owner, repo name, PR number, or filename. Requiring them to manually parse these details is a significant cognitive load and causes friction.
**Action:** Always intercepts text input in repo/PR fields. If a URL format is detected, automatically extract all relevant segments (owner, repo, filename, PR #) and distribute them to the corresponding form fields in a single operation.

## 2025-02-14 - Aesthetic-Consistent Show/Hide Secret Toggles
**Learning:** This app operates under a super-minimalist aesthetic and has zero vector icon dependencies (the bottom navigation uses monospace characters as icons). Adding standard vector icons for password toggle (visibility/visibility-off) would introduce dependency overhead or visual inconsistency.
**Action:** Design custom text-based toggles (e.g., "Show" / "Hide" monospace button) as `trailingIcon` on password inputs. This keeps the design system beautifully uniform, maintains full screen-reader accessibility, and avoids bundling heavy vector drawables.
