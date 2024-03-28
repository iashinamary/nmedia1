package ru.netology.nmedia.model

data class FeedModelState(
    val loading: Boolean = false,
    val errorLoading: Boolean = false,
    val unsaved: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
)