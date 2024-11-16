package com.example.todoappsearch.data

data class TodoState(
    val todos : List<Todo> = emptyList(),
    val searchQuery : String = ""
)
