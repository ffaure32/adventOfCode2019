package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.abs
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay10Test {
    @Test
    fun buildMap() {
        val lines = mutableListOf<String>()
            lines.add(".#..#")
            lines.add(".....")
            lines.add("#####")
            lines.add("....#")
            lines.add("...##")
        val asteroids = buildAsteroids(lines)
        assertEquals(10, asteroids.size)
        assertEquals(7, detectOthers(Asteroid(1, 0), asteroids))
        assertEquals(7, detectOthers(Asteroid(4, 0), asteroids))
        assertEquals(6, detectOthers(Asteroid(0, 2), asteroids))
        assertEquals(7, detectOthers(Asteroid(1, 2), asteroids))
        assertEquals(7, detectOthers(Asteroid(2, 2), asteroids))
        assertEquals(7, detectOthers(Asteroid(3, 2), asteroids))
        assertEquals(5, detectOthers(Asteroid(4, 2), asteroids))
        assertEquals(7, detectOthers(Asteroid(4, 3), asteroids))
        assertEquals(8, detectOthers(Asteroid(3, 4), asteroids))
        assertEquals(7, detectOthers(Asteroid(4, 4), asteroids))
        assertEquals(8, asteroids.map{ detectOthers(it, asteroids)}.max())
    }

    @Test
    fun splitBigString() {
        val input = """
            ......#.#.
            #..#.#....
            ..#######.
            .#.#.###..
            .#..#.....
            ..#....#.#
            #..#....#.
            .##.#..###
            ##...#..#.
            .#....####
        """.trimIndent()
        val split = input.split("\n")
        assertEquals(10, split.size)
    }
    @Test
    fun secondSample() {
        val input = """
            ......#.#.
            #..#.#....
            ..#######.
            .#.#.###..
            .#..#.....
            ..#....#.#
            #..#....#.
            .##.#..###
            ##...#..#.
            .#....####
        """.trimIndent()
        val lines = input.split("\n")
        val asteroids = buildAsteroids(lines)
        asteroids.forEach {
            println(it)
            println(detectOthers(it, asteroids))
        }
        assertEquals(33, asteroids.map { detectOthers(it, asteroids) }.max())
    }

    @Test
    fun thirdSample() {
        val input = """
            #.#...#.#.
            .###....#.
            .#....#...
            ##.#.#.#.#
            ....#.#.#.
            .##..###.#
            ..#...##..
            ..##....##
            ......#...
            .####.###.
        """.trimIndent()
        val lines = input.split("\n")
        val asteroids = buildAsteroids(lines)
        assertEquals(35, detectOthers(Asteroid(1, 2), asteroids))
        assertEquals(35, asteroids.map { detectOthers(it, asteroids) }.max())
    }

    @Test
    fun sampleWithLetters() {
        val input = """
                #.........
                ...#......
                ...#..#...
                .####....#
                ..#.#.#...
                .....#....
                ..###.#.##
                .......#..
                ....#...#.
                ...#..#..#
        """.trimIndent()
        val lines = input.split("\n")
        val asteroids = buildAsteroids(lines)
        assertEquals(7, detectOthers(Asteroid(0, 0), asteroids))
        // assertEquals(35, asteroids.map { detectOthers(it, asteroids) }.max())
    }

    @Test
    fun tesRealFile() {
        val input = "/day10.txt".loadFromFile().trimIndent()
        val lines = input.split("\n")
        val asteroids = buildAsteroids(lines)
        //assertEquals(7, detectOthers(Asteroid(0, 0), asteroids))
        assertEquals(221, asteroids.map { detectOthers(it, asteroids) }.max())
    }

    @Test
    fun bigSample() {
        val lines = mutableListOf<String>()

        lines.add(".#..##.###...#######")
        lines.add("##.############..##.")
        lines.add(".#.######.########.#")
        lines.add(".###.#######.####.#.")
        lines.add("#####.##.#.##.###.##")
        lines.add("..#####..#.#########")
        lines.add("####################")
        lines.add("#.####....###.#.#.##")
        lines.add("##.#################")
        lines.add("#####.##.###..####..")
        lines.add("..######..##.#######")
        lines.add("####.##.####...##..#")
        lines.add(".#####..#.######.###")
        lines.add("##...#.##########...")
        lines.add("#.##########.#######")
        lines.add(".####.#.###.###.#.##")
        lines.add("....##.##.###..#####")
        lines.add(".#.#.###########.###")
        lines.add("#.#.#.#####.####.###")
        lines.add("###.##.####.##.#..##")
        val asteroids = buildAsteroids(lines)
        assertEquals(210, asteroids.map{ detectOthers(it, asteroids)}.max())
    }

    private fun gcd(number1 : Int, number2: Int) : Int {
        var n1 = number1
        var n2 = number2
        n1 = if (n1 > 0) n1 else -n1
        n2 = if (n2 > 0) n2 else -n2
        while (n1 != n2) {
            if (n1 > n2)
                n1 -= n2
            else
                n2 -= n1
        }
        return n1
    }

    private fun detectOthers(target: Asteroid, asteroids: Set<Asteroid>) : Int {
        val asteroidsWithoutTarget = asteroids.minus(target)
        return asteroidsWithoutTarget.filter{
            var seeable = true
            if(it != target) {
                val diffx = target.x - it.x
                val diffy = target.y - it.y
                val xsign = if(diffx == 0) 0 else diffx/abs(diffx)
                val ysign = if(diffy == 0) 0 else diffy/abs(diffy)
                val gcd = gcd(diffx, diffy)
                val stepx = diffx/gcd
                val stepy = diffy/gcd
                if (abs(diffx) > abs(diffy)) {
                    if(diffy == 0) {
                        for(x in 1 until abs(diffx)) {
                            val find = asteroidsWithoutTarget.minus(it).find { target.y == it.y && target.x == it.x + (x * xsign)}
                            if(find != null) {
                                seeable = false
                            }
                        }

                    } else {
                        val loopCount= diffy/stepy
                        for(y in 1 until loopCount) {
                            val find = asteroidsWithoutTarget.minus(it).find {
                                target.y == it.y + (y * stepy) && target.x == it.x + (stepx * y)
                            }
                            if(find != null) {
                                seeable = false
                            }
                        }
                    }
                } else {
                    if (diffx == 0) {
                        for(y in 1 until abs(diffy)) {
                            val find = asteroidsWithoutTarget.minus(it).find { target.x == it.x && target.y == it.y + y * ysign}
                            if(find != null) {
                                seeable = false
                            }
                        }

                    } else {
                        val loopCount= diffx/stepx
                        for(x in 1 until loopCount) {
                            val find = asteroidsWithoutTarget.minus(it).find {
                                target.y == it.y + (stepy * x) && target.x == it.x + (stepx * x)
                            }
                            if(find != null) {
                                seeable = false
                            }
                        }
                    }

                }
            }
            seeable
        }.count()

    }

    private fun buildAsteroids(lines: List<String>): Set<Asteroid> {
        val asteroids = mutableSetOf<Asteroid>()
        for (y in 0 until lines.size) {
            val line = lines[y]
            for(x in 0 until line.length) {
                if(line[x] == '#') {
                    asteroids.add(Asteroid(x, y))
                }
            }
        }
        return asteroids.toSet()
    }


}
