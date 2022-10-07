package logic.entity

import DESTINATION_V
import random
import kotlin.math.cos
import kotlin.math.sin

class Destination(coords: Pair<Double, Double>, val initCapacity: Int): Movable() {
    var capacity = initCapacity

    init {
        reset(coords)
    }

    fun reset(coords: Pair<Double, Double>){
        x = coords.first
        y = coords.second

        val a = random.nextFloat()
        dx = DESTINATION_V * cos(a)
        dy = DESTINATION_V * sin(a)

        capacity = initCapacity
    }
}