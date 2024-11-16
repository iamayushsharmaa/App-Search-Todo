package com.example.todoappsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.collection.mutableObjectIntMapOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoappsearch.data.Todo
import com.example.todoappsearch.ui.theme.TodoAppSearchTheme

class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModels(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(TodoSeachManager(applicationContext)) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppSearchTheme {
                val state = viewModel.state
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ){
                        TextField(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            value = state.searchQuery,
                            onValueChange = viewModel::onSearchQueryChanges

                        )
                        LazyColumn (
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(8.dp)

                        ){
                            items(state.todos){ todo ->
                               TodoItem(
                                   todo = todo,
                                   onDonechanges = { isDone ->
                                       viewModel.onDoneChanges(todo, isDone)
                                   }
                               )

                            }
                        }
                    }
                }
            }
        }
    }
@Composable
fun TodoItem(
    todo: Todo,
    onDonechanges: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Row (
        modifier = modifier.padding(5.dp)
        .fillMaxWidth()
    ){
        Column(
            modifier = modifier.weight(1f)
        ) {
            Text(
                text = todo.title,
                modifier = modifier.padding(5.dp),
                fontSize = 16.sp,
                )
            Text(
                text = todo.text,
                modifier = modifier.padding(5.dp),
                fontSize = 12.sp,
            )
        }
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = onDonechanges
        )
    }
}
}
