---
layout: page
title: Tutorial remark command
---

# Tutorial: adding a remark command

This tutorial implementation follows the [SE-EDU AB3 Adding a Command tutorial](https://se-education.org/guides/tutorials/ab3AddRemark.html).
It is practice work on the `tutorial-adding-command` branch.

## Using the command

- `remark 1 r/Likes swimming` adds or replaces the first displayed person's remark.
- `remark 1 r/` clears that person's remark.
- `remark 1` also clears the remark, matching the tutorial's parser behaviour.
- After a `find` command, the index refers to the filtered list. A successful remark command shows all contacts again.
- Indices must be positive integers and identify a displayed person.
- Repeating `r/` produces a duplicate-field error, consistent with other single-valued fields.

The note appears on the person's card and is saved in the address book's JSON file.
Older JSON files without remarks load with empty remarks. Ordinary `edit` commands preserve the existing remark.
Newly added and sample contacts start with no remark.

## Implementation

`AddressBookParser` dispatches to `RemarkCommandParser`, which extracts the index and note.
`RemarkCommand` replaces the immutable `Person` with a copy containing the new `Remark`.
`PersonCard` displays it, and `JsonAdaptedPerson` persists it.

The existing five-argument `Person` constructor remains available and supplies an empty remark,
so existing add commands and sample data do not need to change.

## Verification

Tests cover adding, replacing, removing, filtered indices, invalid input, edit preservation,
and a JSON round trip containing Unicode and quotation marks.
