---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* _{List the sources of reused or adapted ideas, code, documentation, and third-party libraries here, with links to the originals.}_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in `docs/diagrams`. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Person` objects from the model.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the `Person` objects selected by the current filter, such as search results, in a separate _filtered_ list. It exposes this list as an unmodifiable `ObservableList<Person>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The alternative, arguably more object-oriented, design below keeps a unique list of tags in `AddressBook`, and each `Person` references tags from that list. This lets `AddressBook` maintain one `Tag` object per unique tag instead of each `Person` holding its own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

These are the planned requirements for TrackCall. They describe the intended product,
not a claim that every feature is already implemented. The starting code still uses AB3 behaviour.

### Product scope

**Target user profile**:

* A membership secretary who maintains an organisation's roster of up to 500 members.
* Keeps member names, phone numbers, email addresses, and addresses up to date.
* Organises members by groups, such as committee, year group, or alumni.
* Is comfortable typing short commands and prefers a desktop app.
* Needs to update a whole group without editing each member separately.

**Value proposition**: Keep member records accurate and organised using short typed commands.
Search and filter the roster, then add or remove a group tag for everyone shown in one step.

**MVP scope**: Manage contact records, search names, filter by tags, edit tags in bulk,
show command help, save data locally, and close the app. A member has a name, phone number,
email address, address, and zero or more tags. Group membership is represented by tags.
TrackCall does not make calls, send messages, track payments, or automatically renew memberships.
Sharing or exporting a roster is not part of the specified MVP.

### User stories

Priorities: `* * *` = required for the MVP; `* *` = useful later; `*` = low priority.
The last three stories record longer-term possibilities. They are outside the MVP and are not release promises.

| ID | Priority | As a... | I want to... | So that I can... |
| --- | --- | --- | --- | --- |
| US01 | `* * *` | new secretary | see a command summary and help for one command | learn the syntax without leaving the app |
| US02 | `* * *` | secretary | add a member with contact details and optional tags | keep new members in the roster |
| US03 | `* * *` | secretary | list all members | review the roster and see current record numbers |
| US04 | `* * *` | secretary | edit a member's details | keep the roster accurate when details change |
| US05 | `* * *` | secretary | find members by name | locate their contact details quickly |
| US06 | `* * *` | secretary | delete one selected member | remove a record I no longer need |
| US07 | `* * *` | secretary | clear the whole roster | reset the directory when all old records are no longer needed |
| US08 | `* * *` | secretary | filter the displayed members by a tag | work with one group or an overlap of groups |
| US09 | `* * *` | secretary | add one tag to all displayed members | assign a group without editing each record |
| US10 | `* * *` | secretary | remove one tag from all displayed members | update a group's membership in one step |
| US11 | `* * *` | secretary | have valid changes saved automatically | keep my changes after restarting the app |
| US12 | `* * *` | secretary | see clear input and storage errors | correct a problem and know whether my changes were saved |
| US13 | `* * *` | secretary | close the app with a command | finish my work using the keyboard |
| US14 | `* * *` | experienced secretary | edit a backed-up data file while the app is closed | correct stored data outside the app when needed |
| US15 | `* *` | secretary | export a roster | share the required member records with an authorised colleague |
| US16 | `* *` | secretary | hide private contact details on screen | reduce accidental disclosure to people nearby |
| US17 | `*` | secretary | sort members by name | browse a long roster more easily |

### Shared behaviour rules

These rules apply across the use cases below.

1. Commands and prefixes are lowercase. Surrounding spaces and tabs are ignored.
   Invalid input leaves records, the displayed list, and the saved file unchanged.
2. Two records are duplicates only when their trimmed name, phone, email, and address
   all match exactly, including case and internal spaces. Tags do not affect identity.
   Two members may share a name if another contact field differs.
3. A tag contains 1 to 30 ASCII letters or digits. Tags are case-sensitive and cannot contain spaces.
   A record stores each tag once. Display tags in ASCII order: digits, uppercase, then lowercase.
4. `list` resets the search and tag filters. `find KEYWORD [MORE_KEYWORDS]` searches the full roster
   and replaces the previous search and filters. It matches complete name words, ignores letter case,
   and accepts a match on any keyword.
5. `filter t/TAG` narrows the current list. Repeated filters keep only members who match all applied tags
   and the active name search. Use `list` first to filter the whole roster.
6. Record numbers start at 1 and refer to the displayed list when a command is entered.
   Commands preserve roster order and renumber the visible cards. Users must check the current number
   before editing or deleting a member.
7. `edit INDEX ...` changes only supplied fields. Supplied tags replace the member's full tag set.
   A lone `t/` clears the member's tags. It cannot be combined with a non-empty tag.
   Edits reapply the active search and filters, so an edited member may disappear from view.
8. Bulk tag commands fix their target set before making changes: everyone visible when Enter is pressed.
   Hidden members are untouched. Reapply the active search and filters after the whole batch.
   Adding an existing tag does not create a duplicate; removing an absent tag skips that member.
9. Valid `add`, `edit`, `delete`, `clear`, `tagall`, and `untagall` commands attempt to save the whole roster,
   including valid operations that make no change. An empty bulk target is an error and does not save.
   `help`, `list`, `find`, `filter`, and `exit` do not save.
10. Report data-changing command success only after saving succeeds. If saving fails, preserve the previous
    saved file and show the storage error. Except for `clear`, changes remain in memory and may be lost
    on exit. A failed `clear` restores the previous records, view, and filters.
11. `add` restores the full list and appends the new record. `delete` keeps the active search and filters.
    `clear` removes the whole roster, including hidden members, and resets the view.
    Deletion and clearing are immediate: the MVP has no confirmation or undo.
12. Startup reads `data/addressbook.json` relative to the working directory. A missing file loads sample
    members. An invalid or unreadable file loads zero members and shows an error. Startup and read-only
    commands do not overwrite that file. A valid empty roster stays empty.

### Use cases

For all use cases, the **system** is TrackCall and the **actor** is the membership secretary.
**MSS** means the main success scenario. Except for UC07, the app is open.
These use cases describe planned behaviour; they must be tested as the features are implemented.

#### UC01: Register a member

Related story: US02.

**Command syntax**: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]...`. The four
contact fields are required and can appear in any order. Tags are optional and may be
repeated. Names are nonblank and contain only ASCII letters, digits, and spaces; phone
numbers contain only ASCII digits and are at least three digits long. Email addresses use
the `local-part@domain` format, and addresses are non-empty. Each tag contains 1 to 30 ASCII
letters or digits and is case-sensitive. Repeated identical tags in one command are stored
only once. A member is a duplicate when its
trimmed name, phone, email, and address all match an existing record; tags do not affect
this comparison.

**MSS**

1. The secretary enters the new member's required contact fields and optional tags.
2. TrackCall checks the input and checks for a duplicate record.
3. TrackCall adds the member, saves the roster, and shows the full list with the new member last.
4. TrackCall reports the added member's details. The use case ends.

**Extensions**

* 2a. A required value is missing, a field is invalid, or the record is a duplicate.
  TrackCall shows the relevant error and changes nothing. The secretary can return to step 1.
* 3a. Saving fails. TrackCall shows the storage error instead of success. The new member stays in memory.
  The secretary fixes the storage problem and runs a valid data-changing command to retry saving.
  The use case ends without confirmed persistence until that retry succeeds.

#### UC02: Find and update a member

Related stories: US03, US04, US05.

**Command syntax**: `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]...`.
The index refers to the current displayed list. At least one field must be supplied, and
fields can appear in any order. Supplied contact fields follow the same validation rules as
UC01; omitted fields remain unchanged. If one or more tags are supplied, they replace the
member's existing tags, and repeated identical tags are stored only once. A lone `t/` clears
all tags and cannot be combined with a non-empty tag. The edited record must not duplicate
another member under the identity rule in UC01.

**MSS**

1. The secretary searches for a name.
2. TrackCall shows matching members with their current record numbers.
3. The secretary checks the required record and submits its number and the changed fields.
4. TrackCall validates the edit, updates the record, saves the roster, and reapplies the active filters.
5. TrackCall reports the updated details. The use case ends.

**Extensions**

* 2a. No members match. TrackCall shows an empty list. The secretary can search again at step 1 or stop.
* 3a. The number is invalid, no fields are supplied, a field is invalid, or the edit duplicates another record.
  TrackCall shows an error without changing records or the view. Resume at step 3.
* 4a. The edited member no longer matches the search or filters. It disappears from the view but remains
  stored. Continue at step 5.
* 4b. Saving fails. The edit remains in memory and TrackCall reports the error. The secretary must fix the
  storage problem and retry saving with a valid data-changing command before relying on persistence.

#### UC03: Update a group's tags

Related stories: US08, US09, US10.

**MSS**

1. The secretary lists all members, then searches or filters to select a group.
2. TrackCall shows the matching members. The secretary checks that the list is the intended group.
3. The secretary requests adding or removing one tag for everyone shown.
4. TrackCall validates the tag, fixes the target set, and updates that whole set.
5. TrackCall saves the roster, reapplies the active search and filters, and reports changed and skipped counts.
   The use case ends.

**Example**: Enter `list`, then `filter t/year1`, then `tagall t/committee` as separate commands.
This adds `committee` to all year-one members and keeps their other tags.

**Extensions**

* 2a. The list is empty. The secretary can return to step 1. A bulk command on this list reports an error
  and changes nothing.
* 3a. The tag is empty or invalid, or more than one tag is supplied. TrackCall reports the error and makes
  no changes. Resume at step 3.
* 4a. Some members already have the added tag, or lack the removed tag. TrackCall skips those members
  and includes them in the count at step 5. If none need changes, it reports this without duplicating tags.
* 5a. Removing a tag makes members fail an active filter. Those cards disappear, but their records remain
  stored. Counts still refer to the target set from step 4.
* 5b. Saving fails. The complete batch remains in memory. TrackCall shows the storage error instead of
  success. Fix the storage problem and retry a valid data-changing command to save the current roster.

#### UC04: Remove one member

Related story: US06.

**MSS**

1. The secretary lists, searches, or filters members.
2. TrackCall displays matching records and their current numbers.
3. The secretary checks the member's details and requests deletion using that displayed number.
4. TrackCall removes that one record, saves the roster, and renumbers the remaining visible records.
5. TrackCall reports the deleted record. The use case ends.

**Extensions**

* 2a. The list is empty. No member can be selected. The use case ends.
* 3a. The number or command is invalid. TrackCall shows an error and removes nothing. Resume at step 2.
* 4a. Saving fails. The member remains deleted in memory but may return after restart. TrackCall shows
  the storage error. The secretary fixes the problem and retries saving through a valid data-changing command.

#### UC05: Reset the whole roster

Related story: US07.

**MSS**

1. The secretary decides to remove all records and enters `clear`.
2. TrackCall removes all members, including hidden ones, and resets the search and filters.
3. TrackCall saves the empty roster and reports the number removed. The use case ends.

**Extensions**

* 1a. The command has extra arguments. TrackCall rejects it without changing anything. Resume at step 1.
* 2a. The roster is already empty. TrackCall still attempts to save and reports that no changes were needed
  only after saving succeeds. The use case ends.
* 3a. Saving fails. TrackCall restores the previous records, displayed list, and filters. It reports that no
  members were removed. The secretary can fix the storage problem and retry at step 1.

There is no confirmation or undo. A successful clear requires a backup for recovery.

#### UC06: Look up a command

Related story: US01.

**Command syntax**: `help` or `help COMMAND`, where `COMMAND` is one valid, lowercase
command keyword. Leading and trailing whitespace is ignored, and repeated whitespace
between tokens is treated as a single separator. At most one command keyword may follow
`help`.

**MSS**

1. The secretary enters `help` to see the available commands.
2. TrackCall displays the command syntax quick-reference in the result display and reports
   `Showing command syntax quick-reference.` The use case ends.

Alternatively:

1. The secretary enters `help COMMAND`, such as `help tagall`.
2. TrackCall displays a Vim-like help topic containing that command's syntax, description,
   and usage rules, and reports `Showing help for COMMAND.` The use case ends.

**Extensions**

* 1a. The secretary supplies an unknown or incorrectly cased topic. TrackCall reports
  `No help entry found for "COMMAND". Type help for a list of available commands.`
  The secretary can retry at step 1.
* 1b. The secretary supplies more than one topic. TrackCall reports
  `Only one command can be inspected at a time. Usage: help [COMMAND]`. The secretary can
  retry at step 1.
* 1c. The secretary uses an invalid help flag format, such as `-help` or `-h`. TrackCall
  reports `Unknown command format. Usage: help [COMMAND]`. The secretary can retry at step 1.

All help requests leave member data and the displayed member list unchanged. Help topics
are case-sensitive; command names must use their standard lowercase spelling.

#### UC07: Load a manually edited data file

Related stories: US11, US12, US14.
Precondition: The app is closed and the secretary has backed up the data file.

**MSS**

1. The secretary edits the JSON data file, keeping the expected structure and valid member fields.
2. The secretary starts TrackCall.
3. TrackCall validates the complete file and loads the members in file order.
4. TrackCall displays the loaded roster. The use case ends.

**Extensions**

* 3a. The file is missing. TrackCall loads sample members and reports the missing file. It creates a file
  only after a valid data-changing command. The use case ends.
* 3b. The file has invalid JSON, invalid fields, or duplicate members. TrackCall loads zero members,
  reports the problem, and leaves the original file untouched. The secretary closes the app and repairs
  or restores the file before returning to step 2. No partial import occurs.
* 3c. The file cannot be read. TrackCall loads zero members, shows the operating-system error, and leaves
  the file untouched. The secretary fixes access before returning to step 2.

A later valid data-changing command can overwrite an invalid file. Restore or repair the backup before
making further data changes. Do not edit the data file while the app is open.

### Non-Functional Requirements

These are acceptance targets for the intended product, not results already measured on the starter.

1. **Portability**: The application must run on Windows, macOS, and Linux with a compatible Java 25 runtime.
2. **Capacity and speed**: With 500 member records, each normal command should finish within 2 seconds
   on a computer with at least 4 GB RAM and local SSD storage. Measure from Enter to the displayed result,
   including saving for commands that change data. Record the OS and hardware used for these checks.
   The 500-member target is a supported workload, not a rule that rejects the 501st record.
3. **Offline use**: Core member management and in-app command help must work without a network connection.
4. **Keyboard use**: After launch, users must be able to manage members and request help through the
   command box without using the mouse.
5. **Input safety**: Invalid commands must not change records, the view, or the saved file.
   Error messages must explain what needs correcting.
6. **Reliable storage**: A successful data-changing command must survive restart. Saving must preserve
   the last valid file if writing fails. Never report a failed save as success. The special rollback rule
   for `clear` must preserve both the previous in-memory state and the saved file.
7. **Local privacy**: Core features must keep member records on the user's computer and must not transmit
   them to external services. The MVP has no login or file encryption; operating-system access controls
   are needed to protect the local data file.
8. **Readable results**: The member list and long help output must be scrollable. All records and their
   details must remain accessible when they do not fit in the window.
9. **Consistent results**: Repeating a read-only command without intervening data changes must produce
   the same records in the same order. Repeated bulk tagging must not create duplicate tags.

### Glossary

| Term | Meaning |
| --- | --- |
| Membership secretary | The person who maintains an organisation's member records. |
| Roster | The complete set of stored member records, including members hidden by a filter. |
| Member record | One person's name, phone number, email address, address, and tags. |
| Tag | A case-sensitive group label, such as `committee` or `year1`. |
| Displayed list | The members currently shown after any active search and filters. |
| Index | A member's current displayed number, starting at 1. It is not a permanent member ID. |
| Bulk tag update | Adding or removing one tag for every member in the displayed list. |
| Duplicate record | A record with the same trimmed name, phone, email, and address as another record. |
| No-op | A valid operation that leaves record values unchanged. It can still retry saving. |
| In-memory data | The working roster held by the running app. Unsaved changes can be lost on exit. |
| JSON | The structured text format used for the local data file. |
| MVP | Minimum viable product: the first version containing the required core features. |

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Double-click the JAR file.<br>
      Expected: The GUI opens with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command, with multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: The first contact is deleted from the list. The status message shows the deleted contact's details.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. The status message shows error details.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{Explain how to simulate missing or corrupted data files and state the expected behavior.}_

1. _{ more test cases …​ }_
