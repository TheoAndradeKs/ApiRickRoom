package com.theoandrade.propriapokedex.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personagens")
data class RickEntity(

    @PrimaryKey
    val id: Int,

    val name: String,

    val species: String,

    val status: String,

    val imageUrl: String
)