package logic

import logic.entity.Destination
import logic.entity.Goal
import logic.entity.Movable
import logic.entity.Worker
import java.util.LinkedList
import kotlin.math.sqrt
import kotlin.random.Random

const val BASES = 1
const val SOURCES = 1
const val WORKERS = 1000
const val MAP_SIZE = 900.0
const val V = 2.0
const val DESTINATION_V = 0.25
const val BASE_CAP = 5000
const val SOURCE_CAP = 4000
const val BASE_R = 30
const val SOURCE_R = 30
const val VOLUME = 80

val random = Random(122233)

class GameWorld {
    val bases = ArrayList<Destination>().apply { generateCoords(BASES).forEach { add(Destination(it, BASE_CAP)) } }
    val sources = ArrayList<Destination>().apply { generateCoords(SOURCES).forEach { add(Destination(it, SOURCE_CAP)) } }
    val workers = ArrayList<Worker>().apply { generateCoords(WORKERS).forEach { add(Worker(it)) } }

    private fun generateCoords(count: Int): List<Pair<Double, Double>>{
        val res = LinkedList<Pair<Double, Double>>()
        for (i in 0 until count)
            res += Pair(random.nextDouble(MAP_SIZE), random.nextDouble(MAP_SIZE))
        return res
    }

    private fun dist(from: Pair<Double, Double>, to: Pair<Double, Double>): Double{
        return sqrt((from.first-to.first)*(from.first-to.first) + (from.second-to.second)*(from.second-to.second))
    }

    fun tick(){
        // 1. select workers who found something, scream and listen
        for (worker in workers) {
            for (base in bases)
                if (dist(worker.coords, base.coords) < BASE_R) {
                    worker.toBase = 0.0
                    if (worker.goal == Goal.BASE) {
                        worker.goal = Goal.SOURCE
                        worker.dx *= -1
                        worker.dy *= -1

                        base.capacity--
                        if (base.capacity == 0)
                            base.reset(generateCoords(1)[0])
                    }
                    scream(worker)
                }
            for (source in sources)
                if (dist(worker.coords, source.coords) < SOURCE_R) {
                    worker.toSource = 0.0
                    if (worker.goal == Goal.SOURCE) {
                        worker.goal = Goal.BASE
                        worker.dx *= -1
                        worker.dy *= -1

                        source.capacity--
                        if (source.capacity == 0)
                            source.reset(generateCoords(1)[0])
                    }
                    scream(worker)
                }
        }

        // 2. move
        for (worker in workers){
            move(worker)

            worker.toBase++
            worker.toSource++
        }
        for (base in bases)
            move(base)
        for (source in sources)
            move(source)
    }

    private fun move(obj: Movable){
        obj.x += obj.dx
        obj.y += obj.dy

        if (obj.x > MAP_SIZE){
            obj.x = 2 * MAP_SIZE - obj.x
            obj.dx *= -1
        }
        if (obj.y > MAP_SIZE){
            obj.y = 2 * MAP_SIZE - obj.y
            obj.dy *= -1
        }
        if (obj.x < 0){
            obj.x *= -1
            obj.dx *= -1
        }
        if (obj.y < 0){
            obj.y *= -1
            obj.dy *= -1
        }
    }

    private fun scream(slave: Worker){
        for (worker in workers) {
            val l = dist(worker.coords, slave.coords)
            if (l < VOLUME) {
                var flag = false
                if (worker.toBase > slave.toBase + VOLUME) {
                    worker.toBase = slave.toBase + VOLUME
                    if (worker.goal == Goal.BASE) {
                        worker.dx = (slave.x - worker.x) * (V / l)
                        worker.dy = (slave.y - worker.y) * (V / l)
                    }
                    flag = true
                }
                if (worker.toSource > slave.toSource + VOLUME) {
                    worker.toSource = slave.toSource + VOLUME
                    if (worker.goal == Goal.SOURCE) {
                        worker.dx = (slave.x - worker.x) * (V / l)
                        worker.dy = (slave.y - worker.y) * (V / l)
                    }
                    flag = true
                }
                if (flag)
                    scream(worker)
            }
        }
    }
}