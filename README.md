# mini-todo (Kotlin + Jetpack Compose)
to-do list app built with Kotlin and Jetpack Compose.
Users can add tasks to to-do tasks list with an add button, and check off a box when they are done to move the task to complete tasks list.
Users can also delete tasks by clicking the trash bin icon.

Data class:
TodoItem holds task data (id, label, done).

State:
mutableStateListOf<TodoItem>() inside TodoViewModel for reactive task updates.
rememberSaveable for the input field text.

State hoisting:
TodoScreen receives the list of to-do/completed items and callback functions (onAdd, onToggle, onRemove) from the parent composable.

<img width="183" height="403" alt="1" src="https://github.com/user-attachments/assets/de695942-96cf-4f08-842e-1b89b6f0aff4" />
<img width="187" height="401" alt="4" src="https://github.com/user-attachments/assets/449aa0f9-cfca-4c91-809d-b6cc50c80571" />
<img width="181" height="402" alt="3" src="https://github.com/user-attachments/assets/d6f70b44-acfb-4cd2-a1f7-c5fd838c1d29" />
<img width="180" height="406" alt="2" src="https://github.com/user-attachments/assets/9382ea80-6d6f-4388-a8aa-c969f6d03cab" />

