# CS2103T Git Workflow

This document describes the Git workflow for the project.

## Before starting work

```powershell
git status
git switch master
git pull --rebase origin master
git switch -c feature/<short-description>
```

Use a short branch name that describes one feature or fix. Keep unrelated work out of the branch.

## While working

Check the working tree regularly:

```powershell
git status
git diff
```

Commit a coherent change when it is complete and verified. Use an imperative commit subject, for example:

```text
Add command-specific CLI help
Fix duplicate help argument parsing
```

Avoid committing generated files, IDE settings, local data, logs, build output, or credentials. Confirm the staged files before committing:

```powershell
git add <files>
git diff --cached
git commit -m "<imperative summary>"
```

## Before pushing

Run the project checks:

```powershell
.\gradlew.bat test checkstyleMain checkstyleTest
```

Update the branch with the latest `master` before opening a review:

```powershell
git fetch origin
git rebase origin/master
git push -u origin feature/<short-description>
```

If a rebase conflict occurs, resolve the files, run the tests again, and continue with `git rebase --continue`. Do not force-push a shared branch.

## Review and merge

The pull request should include:

- a concise summary of the change;
- the user-visible behavior and any limitations;
- tests and checks that were run;
- screenshots for visible UI changes, where useful.

Keep commits focused and explain any deliberate compatibility or design trade-offs in the pull request description.
