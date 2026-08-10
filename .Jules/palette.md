# Palette's Journal 🎨

A record of critical UX and accessibility learnings.

## 2025-02-15 - Interactive Soft-Keyboard Forms in Jetpack Compose
**Learning:** Text input forms lacking appropriate `ImeAction` and `KeyboardActions` force the user to manually dismiss the virtual keyboard and tap submission buttons, creating friction, especially for users relying on screen readers or single-hand navigation. By configuring standard text field focus transitions and executing the button's action directly from the soft keyboard's Search/Done key, form ergonomics improve dramatically.
**Action:** Always configure `keyboardOptions = KeyboardOptions(imeAction = ...)` and `keyboardActions = KeyboardActions(on... = { ... })` for input forms with multiple fields or quick actions in Jetpack Compose.

## 2025-08-10 - Custom Segmented Controls and Accessibility in Jetpack Compose
**Learning:** Custom tab-like, segment-like, or toggle rows created using multiple `clickable` components lack semantic relationships and selectable state info for assistive technologies. This makes it impossible for screen readers to convey that the elements belong to a mutually exclusive group or to announce their current selection state. Applying `selectableGroup()` to the container and `selectable(selected = ..., role = Role.RadioButton)` to individual items ensures clear and standardized user feedback.
**Action:** When designing custom toggle rows or multi-segment selectors in Compose, group them with `selectableGroup()` and use `.selectable()` with proper role declarations instead of generic `.clickable()`.
