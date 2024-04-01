---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# TeamTracker Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

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

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, `TaskListPanel` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteTaskCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteTaskCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteTaskCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a task).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddTaskCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddTaskCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddTaskCommandParser`, `DeleteTaskCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2324S2-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="675" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the task list data i.e., all `Task` objects (which are contained in a `TaskList` object).
* stores the currently 'selected' `Task` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Task>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. `TaskList` implements the `ReadOnlyTaskList` interface, and has a `UniqueTaskList` that contains all `Task`s. This allows `TaskList` to be implemented in a way that is consistent to how `AddressBook` is implemented, thus any benefits arising from the design decisions of `Person` also applies to `Task`. We are currently not adopting this model due to time constraints and the benefits are not immediately obvious.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="562.5" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Grouping Feature

#### Proposed Implementation

The proposed grouping mechanism is facilitated by `GroupedUniquePersonList`. It extends `UniquePersonList` with task that are linked between the people of the same group. Additionally, it implements the following operations:

* `GroupedUniquePersonList#assignTask(Task)` - Add task to everyone in the group
* `GroupedAddressBook#markTask(Index)` - Mark task of everyone in the group
* `GroupedAddressBook#unmarkTask(Index)` - Unmark task of everyone in the group

These operations are exposed in the `Model` interface as `Model#assignTaskToGroup(String, Task)`, `Model#markTaskOfGroup(String, Index)` and `Model#unmarkTaskofGroup(String, Index)` respectively.

`GroupedUniquePersonList` adds a new string called `groupName` to label each of their groups.

A new list of `GroupedUniquePersonList` will be added to the `Model` interface.

To add to the list of `GroupedUniquePersonList`, the Model interface includes `Model#addGroup(String, List<Person>)` and `Model#addListOfGroups(List<Group>)`.

To remove to the list of `GroupedUniquePersonList`, the Model interface includes `Model#removeGroup(String)`.

New operation are exposed in the `Model` interface are `Model#addPersonToGroup(String, Person)`, `Model#removePersonFromGroup(String, Person)` and `Model#deleteAssignedTaskGroup(String, Task)` which would call `UniquePersonList#add(Person)`, `UniquePersonList#remove(Person)` and `UniquePersonList#deleteAssignedTask(Person)`respectively.

Given below is an example usage scenario and how the grouping mechanism behaves at each step.

Step 1. The user launches the application for the first time. The list of the `GroupedUniquePersonList` will be empty if there are no groups stored in the storage.

<puml src="diagrams/GroupingState0.puml" alt="GroupingState0" />

Step 2. The user executes `group gn/2103T gp/Ivan gp/Greg gp/Dave` command to group Ivan, Greg and Dave from the displayed person list to one group. The `group` command calls `Model#addGroup(String, List<Person>)`, which creates a new group with that contains the list of people that was indicated by the user.

<puml src="diagrams/GroupingState1.puml" alt="GroupingState1" />

Step 3. The user executes `assigngroup gn/2103T gt/Task 1` command to assign a task named "Task 1" to the group named "2103T" from the group list. The `assigngroup` command calls `Model#assignTaskToGroup(String, Task)`, which finds the group with the same name and assign that task to everyone that is in the group.

<puml src="diagrams/GroupingState2.puml" alt="GroupingState2" />

Step 4. The user executes `addpersontogroup gn/2103T gp/Bob` command to add Bob to the group named "2103T" from the group list. The `addpersontogroup` command calls `Model#addPersonToGroup(String, Person)`, which finds the group with the same name and add the person to the group.

Step 5. The user executes `removepersonfromgroup gn/2103T gp/4` command to remove Bob from the group named "2103T" from the group list. The `removepersonfromgroup` command calls `Model#removePersonFromGroup(String, Person)`, which finds the group with the same name and remove the person to the group.

Step 6. The user executes `deletetaskgroup gn/2103T gt/Task 1` command to remove a task named "Task 1" from the group named "2103T" from the group list. The `deletetaskgroup` command calls `Model#deleteAssignedTaskGroup(String, Task)`, which finds the group with the same name and remove that task from everyone that is in the group.

Step 7. The user executes `deletegroup gn/2103T` command to remove the group from the list. The `deletegroup` command calls `Model#removeGroup(String)`, which finds the group with the same name and remove that group from the list.

### \[Proposed\] Undo/Redo Feature

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book and task list state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book and task list state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book and task list state from its history.

These operations are exposed in the `Model` interface as `Model#commit()`, `Model#undo()` and `Model#redo()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book and task list state, with the `addressBookStatePointer` and `taskListStatePointer`.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `add n/task 1 …​` to add a new task. The `addtask` command also calls `Model#commit()`, causing another modified task kust state to be saved into the `taskListStateList`.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commit()`, so the state will not be saved.

</box>

Step 3. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undo()`, which will shift the `taskListStatePointer` once to the left, pointing it to the previous task list state, and restores the task list to that state.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />


<box type="info" seamless>

**Note:** If the pointers are at index 0, pointing to the initial state, then there are no previous states to restore. The `undo` command uses `Model#canUndo()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redo()`, which shifts the pointers once to the right, pointing to the previously undone state, and restores the address book or task list to that state.

<box type="info" seamless>

**Note:** If the `addressBookStatePointer` is at index `addressBookStateList.size() - 1` or `taskListStatePointer` is at index `taskListStateList.size() - 1`, pointing to the latest state, then there are no undone states to restore for the respective commands. The `redo` command uses `Model#canRedo()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 4. The user then decides to execute the command `listtask`. Commands that do not modify the task list, such as `listtask`, will usually not call `Model#commit()`, `Model#undo()` or `Model#redo()`. Thus, the `taskListStateList` and `taskListStatePointer` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `deletetask`, just save the task being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_



--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* are currently managing people for groups
* has a tendency to forget tasks to complete

**Value proposition**: This app aims to help leaders to keep track of members of formed groups and their contact information. This app helps to keep track of individual and group tasks, deadlines and meetings, thus allowing them to have a better overview of the structure.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                     | I want to …​                                     | So that I can…​                                                         |
|----------|--------------------------------------------|-------------------------------------------------|------------------------------------------------------------------------|
| `* * *`  | student                                    | add personal tasks                              | keep up to date with the different tasks to complete                   |
| `* * *`  | student                                    | delete tasks                                    |                                                                        |
| `* * *`  | student                                    | mark/unmark the tasks as done/not done          | keep track of tasks that are completed                                 |
| `* *`    | group leader                               | assign tasks to individuals within the group    | manage individual tasks                                                |
| `* *`    | busy group leader                          | see an overview of all the saved task           | save time                                                              |
| `* *`    | student                                    | set deadline for my tasks                       | see which task need to be done earlier                                 |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Assigns a task**

**MSS**

1.  User requests to list of contacts
2.  TeamTracker shows a list of contacts
3.  User requests to assign a task to a contact
4.  TeamTracker assigns the task to the contact

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 4a. The task given does not exist.
* 4a1. TeamTracker shows an error message.

  Use case ends.

**Use case: Delete a task**

**MSS**

1. User requests to list tasks
2. TeamTracker shows a list of tasks
3. User requests to delete a specific task in the list
4. TeamTracker deletes the task

   Use case ends.

**Extensions**

* 2a. The list is empty.

   Use case ends.

* 3a. The given index is invalid.
* 3a1. TeamTracker shows an error message.

   Use case resumes at step 2.

**Use case: Add a task**

**MSS**

1. User requests to add a task to the list of tasks
2. TeamTracker adds to the list

   Use case ends.

**Extensions**

* 2a. The given parameters is invalid.
* 2a1. TeamTracker shows an error message.

   Use case ends.


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to be used for long periods without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should respond within one seconds.
5.  Documentation should be easy for users that are inexperienced in command line to follow.
6.  User Interface should be straightforward and intuitive to navigate even for first-time users.
7.  Should be able to store more than 10,000 tasks.
8.  Should lose no more than 1 command worth of work in case of an app crash.
9.  Should work without internet access.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **System admin commands**: commands or instructions that are used through a command-line interface (CLI)
or a terminal window
* **Action**: The AddressBook processing a user command

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
