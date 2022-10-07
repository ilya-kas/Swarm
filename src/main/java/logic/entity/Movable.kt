package logic.entity

abstract class Movable{
    var x = 0.0
    var y = 0.0
    val coords: Pair<Double, Double>
        get() = Pair(x,y)
    var dx = 0.0
    var dy = 0.0
}