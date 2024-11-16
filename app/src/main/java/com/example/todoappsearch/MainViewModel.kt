package com.example.todoappsearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappsearch.data.Todo
import com.example.todoappsearch.data.TodoState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class MainViewModel (
    private val todoSeachManager: TodoSeachManager
): ViewModel() {

    var state by mutableStateOf(TodoState())
        private set

    private var searchJob : Job? = null

    init {
        viewModelScope.launch {
            todoSeachManager.init()
            val todos = (1..100).map {
                Todo(
                    namespace = "my_todos",
                    id = UUID.randomUUID().toString(),
                    score = 1,
                    title = "Todo $it",
                    text = "Describtion $it",
                    isDone = Random.nextBoolean()

                )
            }
            todoSeachManager.putTodos(todos)
        }
    }

    fun onSearchQueryChanges(query :String){
        state = state.copy(searchQuery = query)
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500L)
           val todos = todoSeachManager.searchTodos(query)
            state = state.copy(todos = todos)
        }
    }
    fun onDoneChanges(todo: Todo, isDone : Boolean){
        viewModelScope.launch {
            todoSeachManager.putTodos(
                listOf(todo.copy( isDone = isDone))
            )
            state = state.copy(
                todos = state.todos.map {
                    if (it.id == todo.id){
                        it.copy(isDone = isDone)
                    }else it
                }
            )
        }
    }

    override fun onCleared() {
        todoSeachManager.closeSession()
        super.onCleared()
    }
}