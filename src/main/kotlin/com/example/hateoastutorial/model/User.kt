package com.example.hateoastutorial.model

data class User(val name: String, val id: Int, val roles: List<Role> = emptyList())