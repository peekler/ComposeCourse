package hu.bme.tododemo.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.bme.tododemo.R
import hu.bme.tododemo.data.TodoItem
import hu.bme.tododemo.data.TodoPriority
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    todoListViewModel: TodoListViewModel = viewModel(),
    navController: NavController
) {
    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var expanded by remember { mutableStateOf(false) }

    var todoToEdit: TodoItem? by rememberSaveable {
        mutableStateOf(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Todo App") },
                actions = {
                    IconButton(onClick = {
                        todoListViewModel.clearAllTodos()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) { Icon(Icons.Filled.MoreVert, contentDescription = null) }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(onClick = { },
                            text = { Text(text = "Demo1") })
                        DropdownMenuItem(onClick = { },
                            text = { Text(text = "Demo2") })
                    }

                },

                )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                },
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                if (showAddDialog) {
                    TodoForm(
                        onDialogClose = {
                            showAddDialog = false
                        },
                        todoListViewModel = todoListViewModel,
                        todoToEdit = todoToEdit
                    )
                }


                if (todoListViewModel.getAllToDoList().isEmpty())
                    Text(text = "No items")
                else {
                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
                        items(todoListViewModel.getAllToDoList()) {
                            TodoCard(it,
                                onRemoveItem = { todoListViewModel.removeTodoItem(it) },
                                onTodoCheckChange = { checked ->
                                    todoListViewModel.changeTodoState(
                                        it,
                                        checked
                                    )
                                },
                                onEditItem = {
                                    showAddDialog = true
                                    todoToEdit = it
                                }
                            )
                        }
                    }
                }

            }
        }
    )
}


@Composable
fun TodoCard(
    todoItem: TodoItem,
    onTodoCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (TodoItem) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = todoItem.priority.getIcon()),
                contentDescription = "Priority",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 10.dp)
            )

            Text(todoItem.title)
            Spacer(modifier = Modifier.fillMaxSize(0.55f))
            Checkbox(
                checked = todoItem.isDone,
                onCheckedChange = { onTodoCheckChange(it) }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                modifier = Modifier.clickable {
                    onRemoveItem()
                },
                tint = Color.Red
            )
            Icon(
                imageVector = Icons.Filled.Build,
                contentDescription = stringResource(R.string.icon_edit),
                modifier = Modifier.clickable {
                    onEditItem(todoItem)
                },
                tint = Color.Red
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoForm(
    todoListViewModel: TodoListViewModel = viewModel(),
    onDialogClose: () -> Unit = {},
    todoToEdit: TodoItem? = null
) {
    var newTodoTitle by remember { mutableStateOf(todoToEdit?.title ?: "") }
    var newTodoDesc by remember { mutableStateOf(todoToEdit?.description ?: "") }
    var newTodoPriority by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDialogClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(value = newTodoTitle,
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Todo title") },
                        onValueChange = {
                            newTodoTitle = it
                        }
                    )
                    OutlinedTextField(value = newTodoDesc,
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Description") },
                        onValueChange = {
                            newTodoDesc = it
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = newTodoPriority, onCheckedChange = {
                        newTodoPriority = it
                    })
                    Text(text = "Important")
                }

                Button(onClick = {
                    if (todoToEdit == null) {
                        todoListViewModel.addTodoList(
                            TodoItem(
                                id = UUID.randomUUID().toString(),
                                title = newTodoTitle,
                                description = newTodoDesc,
                                createDate = Date(System.currentTimeMillis()).toString(),
                                priority = if (newTodoPriority) TodoPriority.HIGH else TodoPriority.NORMAL,
                                isDone = false
                            )
                        )
                    } else { // EDIT mode
                        var todoEdited = todoToEdit.copy(
                            title = newTodoTitle,
                            description = newTodoDesc,
                            priority = if (newTodoPriority) TodoPriority.HIGH else TodoPriority.NORMAL,
                        )

                        todoListViewModel.editTodoItem(todoToEdit, todoEdited)
                    }

                    onDialogClose()
                }) {
                    Text(text = "Save")
                }
            }
        }
    }
}