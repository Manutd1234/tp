# Tutorial 3: Removing the postal-address field

Practice branch: `tutorial-removing-field`, based on `tutorial-adding-command`.

This completes the [AB3 removing-fields tutorial](https://se-education.org/guides/tutorials/ab3RemovingFields.html).
The course lists this tutorial as optional, with no required submission.

## What changed

Postal addresses are removed from `Person`, add/edit parsing, command feedback,
JSON serialization, and JavaFX contact cards. Sample contacts, test fixtures,
command examples, and model diagrams are updated. Names, phones, emails, tags,
and Tutorial 2 remarks remain available.

The field was removed by inspecting its usages and updating each dependent layer.
No IntelliJ Safe Delete session is claimed. Removing a Java field alone is
insufficient: FXML bindings, JSON fixtures, and documentation also depend on it.

## Validation

- Java 25.0.4: 241 tests pass, with no failures, errors, or skipped tests.
- Main and test Checkstyle pass; runnable JAR builds successfully.
- Legacy JSON with a postal address loads; saving omits that field and retains
  the remaining contact data, including remarks.
- Actual JavaFX GUI: add a contact using only name, phone, email, and tag;
  find it; add a remark; edit its phone. The saved JSON contains the updated
  phone and preserved remark, and no postal-address fields.
- Model diagram PNGs regenerated with PlantUML 1.2026.8. The temporary render
  style omits the deprecated sequence-only ParticipantPadding setting, which
  has no effect on these class diagrams.

Example command: `add n/Tutorial Three p/91234567 e/tutorial3@example.com t/practice`.

The practice changes are saved on the personal fork’s `tutorial-removing-field` branch.
No Tutorial 3 PR or merge is required.
