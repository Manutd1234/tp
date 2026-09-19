# Project Standards

## Scope and architecture

Keep responsibilities within the existing AB3 architecture:

- **UI** receives input and displays results.
- **Logic** parses commands and coordinates execution.
- **Model** owns address-book state and business rules.
- **Storage** persists data.

Commands should not manipulate JavaFX controls directly. Parsers should validate and convert input; command execution should operate through the `Model` API.

## Java style

- Use Java 25 and the Gradle build configured by the repository.
- Use four spaces for indentation and follow the repository Checkstyle rules.
- Use `PascalCase` for classes and `camelCase` for methods, variables, and parameters.
- Use `UPPER_SNAKE_CASE` for constants.
- Keep packages lowercase and names descriptive.
- Prefer small methods with one clear responsibility.
- Use `final` where it communicates that a value or dependency should not change.
- Avoid unrelated refactoring in a feature or bug-fix commit.

Public classes and methods should have Javadoc when their purpose or contract is not obvious. User-visible messages should be defined as constants rather than duplicated inline.

## Commands and parsers

Each command should:

1. define its command word and usage message;
2. have a dedicated parser when it accepts structured arguments;
3. validate input before changing the model;
4. return a `CommandResult` with clear user-facing feedback;
5. leave the model unchanged when validation or execution fails.

Command parsing should remain case-sensitive unless a command explicitly documents otherwise. Whitespace behavior and error messages should be covered by tests.

## Testing

Add or update tests for normal behavior, invalid input, boundary cases, and model-state preservation. Name tests after the behavior they verify, for example `parseCommand_helpInvalidArguments_throwsParseException`.

Run the complete verification suite before submitting a change:

```powershell
.\gradlew.bat test checkstyleMain checkstyleTest
```

Do not ignore failing tests or Checkstyle violations without documenting the reason.

## Documentation

User-visible commands belong in `docs/UserGuide.md`. Design and architecture decisions belong in the developer documentation. Keep examples executable and update documentation when syntax, output, or error behavior changes.
