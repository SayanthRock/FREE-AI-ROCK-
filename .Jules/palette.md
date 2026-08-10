# Palette's Journal 🎨

A record of critical UX and accessibility learnings.

## 2025-02-15 - Interactive Soft-Keyboard Forms in Jetpack Compose
**Learning:** Text input forms lacking appropriate `ImeAction` and `KeyboardActions` force the user to manually dismiss the virtual keyboard and tap submission buttons, creating friction, especially for users relying on screen readers or single-hand navigation. By configuring standard text field focus transitions and executing the button's action directly from the soft keyboard's Search/Done key, form ergonomics improve dramatically.
**Action:** Always configure `keyboardOptions = KeyboardOptions(imeAction = ...)` and `keyboardActions = KeyboardActions(on... = { ... })` for input forms with multiple fields or quick actions in Jetpack Compose.
