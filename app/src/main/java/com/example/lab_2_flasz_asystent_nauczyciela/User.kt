package com.example.lab_2_flasz_asystent_nauczyciela

data class User(val name: String, val surname: String, val age: Int) {
    override fun toString(): String {
        return "$name $surname\nAge: $age"
    }
}