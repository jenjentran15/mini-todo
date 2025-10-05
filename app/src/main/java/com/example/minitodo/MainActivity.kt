package com.example.minitodo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.lifecycle.ViewModel
import java.util.UUID
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Jenny Tran

data class TodoItem(
    val id: UUID = UUID.randomUUID(),
    val label: String,
    val done: Boolean = false
)
class TodoViewModel : ViewModel() {
    var items = mutableStateListOf<TodoItem>()
        private set
//add new item
    fun add(label: String) {
        val t = label.trim()
        if (t.isNotEmpty()) {
            items.add(TodoItem(label = t))
        }
    }
// done/not done
    fun toggle(item: TodoItem) {
        val idx = items.indexOfFirst { it.id == item.id }
        if (idx != -1) items[idx] = items[idx].copy(done = !items[idx].done)
    }
// little trash icon action
    fun remove(item: TodoItem) {
        items.removeAll { it.id == item.id }
    }
}
class MainActivity : ComponentActivity() {
    private val vm: TodoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TodoApp(vm)
            }
        }
    }
}
// with a touch of pizzaz
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(vm: TodoViewModel) {
    val context = LocalContext.current
    val items = vm.items
    val active = items.filter { !it.done }
    val completed = items.filter { it.done }
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Mini To Do App") }) }
    ) { padding ->
        TodoScreen(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            active = active,
            completed = completed,
            onAdd = { input ->
                if (input.isBlank()) {
                    Toast.makeText(context, "Please enter a task", Toast.LENGTH_SHORT).show()
                } else {
                    vm.add(input)
                }
            },
            onToggle = vm::toggle,
            onRemove = vm::remove
        )
    }
}
//done/not done in their subsections
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    active: List<TodoItem>,
    completed: List<TodoItem>,
    onAdd: (String) -> Unit,
    onToggle: (TodoItem) -> Unit,
    onRemove: (TodoItem) -> Unit
) {
    var input by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier) {
        AddTaskBar(
            text = input,
            onTextChange = { input = it },
            onSubmit = {
                onAdd(input)
                if (input.isNotBlank()) input = ""
            }
        )
        Spacer(Modifier.height(16.dp))
 //lazy column
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Active items section
            item {
                Text("To Do Items", style = MaterialTheme.typography.titleMedium)
            }
            if (active.isEmpty()) {
                item { Text("No active items") }
            } else {
                items(active, key = { it.id }) { item ->
                    TodoRow(item, onToggle, onRemove)
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            // done tasks
            item {
                Text("Completed Items", style = MaterialTheme.typography.titleMedium)
            }
            if (completed.isEmpty()) {
                item { Text("No completed items") }
            } else {
                items(completed, key = { it.id }) { item ->
                    TodoRow(item, onToggle, onRemove)
                }
            }
        }
    }
}
// text bar for inputs
@Composable
fun AddTaskBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Enter the task name") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Spacer(Modifier.width(8.dp))
        Button(onClick = onSubmit) { Text("Add") }
    }
}

//items
@Composable
fun TodoRow(
    item: TodoItem,
    onToggle: (TodoItem) -> Unit,
    onRemove: (TodoItem) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(checked = item.done, onCheckedChange = { onToggle(item) })
        Text(text = item.label, modifier = Modifier.weight(1f))
        IconButton(onClick = { onRemove(item) }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete ${item.label}"
            )
        }
    }
}
