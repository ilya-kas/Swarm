package logic.entity

import MAP_SIZE
import V
import random
import kotlin.math.cos
import kotlin.math.sin

class Worker(coords: Pair<Double, Double>): Movable(){
    var goal = Goal.SOURCE
    var toBase = MAP_SIZE *3
    var toSource = MAP_SIZE *3

    init {
        x = coords.first
        y = coords.second

        val a = random.nextFloat()
        dx = V * cos(a)
        dy = V * sin(a)
    }
}

enum class Goal{
    BASE,
    SOURCE
}