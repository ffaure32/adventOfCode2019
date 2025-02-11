fun List<Int>.multiply(): Int {
    return this.reduce{acc, next -> acc * next}
}
fun List<Long>.multiply(): Long {
    return this.reduce{acc, next -> acc * next}
}

fun generatePermutationsList(initNumbers: Set<Long>): List<List<Long>> {
    val result = mutableListOf<List<Long>>()
    generatePermutations(initNumbers.size, initNumbers.toMutableList(), result)
    return result.toList()
}
fun generatePermutations(n: Int, elements: MutableList<Long>, result :MutableList<List<Long>>) {
    if (n == 1) {
        result.add(elements.toList())
    } else {
        for (i in 0 until n - 1) {
            generatePermutations(n - 1, elements, result)
            if (n % 2 == 0) {
                swap(elements, i, n - 1)
            } else {
                swap(elements, 0, n - 1)
            }
        }
        generatePermutations(n - 1, elements, result)
    }
}

private fun swap(elements: MutableList<Long>, a: Int, b: Int) {
    val tmp = elements[a]
    elements[a] = elements[b]
    elements[b] = tmp
}

fun <T> Iterable<T>.combinations(length: Int): Sequence<List<T>> = sequence {
    val pool = this@combinations as? List<T> ?: toList()
    val n = pool.size
    if(length > n) return@sequence
    val indices = IntArray(length) { it }
    while(true) {
        yield(indices.map { pool[it] })
        var i = length
        do {
            i--
            if(i == -1) return@sequence
        } while(indices[i] == i + n - length)
        indices[i]++
        for(j in i+1 until length) indices[j] = indices[j - 1] + 1
    }
}


fun <T> Iterable<T>.permutations(length: Int? = null): Sequence<List<T>> = sequence {
    val pool = this@permutations as? List<T> ?: toList()
    val n = pool.size
    val r = length ?: n
    if(r > n) return@sequence
    val indices = IntArray(n) { it }
    val cycles = IntArray(r) { n - it }
    yield(List(r) { pool[indices[it]] })
    if(n == 0) return@sequence
    cyc@ while(true) {
        for(i in r-1 downTo 0) {
            cycles[i]--
            if(cycles[i] == 0) {
                val temp = indices[i]
                for(j in i until n-1) indices[j] = indices[j+1]
                indices[n-1] = temp
                cycles[i] = n - i
            } else {
                val j = n - cycles[i]
                indices[i] = indices[j].also { indices[j] = indices[i] }
                yield(List(r) { pool[indices[it]] })
                continue@cyc
            }
        }
        return@sequence
    }
}