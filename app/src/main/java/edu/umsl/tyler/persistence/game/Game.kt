package edu.umsl.tyler.persistence.game

data class Game(val title: String, var score: Int, val developer: String, val hoursPlayed: Int, val review: String)