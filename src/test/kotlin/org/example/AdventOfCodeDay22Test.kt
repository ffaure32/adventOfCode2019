import org.example.loadFromFile
import org.junit.jupiter.api.Test
import java.math.BigInteger.*
import kotlin.test.assertEquals

class AdventOfCodeDay22Test {

    @Test
    fun initDeckIsSortedInFactoryOrder() {
        // ARRANGE & ACT
        val deck = Deck(10)

        // ASSERT
        val expectedResult = mutableListOf<Int>()
        for(i in 0 until 10) {
            expectedResult.add(i)
        }
        assertEquals(10, deck.cards.size)
        assertEquals(expectedResult, deck.cards)
    }

    @Test
    fun applyDealIntoNewStackTechniqueInvertsDeck() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result = deck.applyTechnique(Technique.DEAL_INTO_NEW_STACK)

        //
        val expected = (9 downTo 0).map { it}
        assertEquals(result.cards, expected)
    }

    @Test
    fun applyCutTechniqueSplitsAndInvertsDeck() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result = deck.applyTechnique(Technique.CUT_N_CARDS, 3)

        //
        val expected = listOf(3, 4, 5, 6, 7, 8, 9, 0, 1, 2)
        assertEquals(expected, result.cards)
    }

    @Test
    fun revertCutTechniqueSplitsAndInvertsDeck() {
        // ARRANGE
        val target = 9L

        // ACT
        val result = Technique.CUT_N_CARDS.previousPosition(target, 3, 10)

        //
        assertEquals(2, result)
    }

    @Test
    fun revertCutTechniqueSplitsAndNegativeDeck() {
        // ARRANGE
        val target = 4L

        // ACT
        val result = Technique.CUT_N_CARDS.previousPosition(target, -4, 10)

        //
        assertEquals(0, result)
    }

    @Test
    fun revertCutTechniqueDealWithIncrementDeck() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result = Technique.DEAL_WITH_INCREMENT.previousPosition(4, 3, 10)

        //
        assertEquals(8, result)
    }


    @Test
    fun applyCutTechniqueWithNegativeN() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result = deck.applyTechnique(Technique.CUT_N_CARDS, -4)

        //
        val expected = listOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5)
        assertEquals(expected, result.cards)
    }

    @Test
    fun applyCutTechniqueDealWithIncrementDeck() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result = deck.applyTechnique(Technique.DEAL_WITH_INCREMENT, 3)

        // ASSERT
        val expected = listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3)
        assertEquals(expected, result.cards)
        assertEquals(7, Technique.DEAL_WITH_INCREMENT.previousPosition(1, 3, 10))

    }


    @Test
    fun functionalCase1() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result =
            deck.applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .applyTechnique(Technique.DEAL_INTO_NEW_STACK)
                .applyTechnique(Technique.DEAL_INTO_NEW_STACK)

        //
        val expected = listOf(0, 3, 6, 9, 2, 5, 8, 1, 4, 7)
        assertEquals(expected, result.cards)
    }

    @Test
    fun functionalCase2() {
        // ARRANGE
        val deck = Deck(10)
        deck.cards.forEachIndexed { index, i ->  if(i == 3) println(index)}

        // ACT
        val result =
            deck
                .applyTechnique(Technique.CUT_N_CARDS, 6)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.DEAL_INTO_NEW_STACK)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }

        //
        val expected = listOf(3, 0, 7, 4, 1, 8, 5, 2, 9, 6)
        assertEquals(expected, result.cards)
    }

    @Test
    fun revertFunctionalCase2() {
        // ARRANGE
        var currentPosition = 0L
        println(currentPosition)

        // ACT
        currentPosition = Technique.DEAL_INTO_NEW_STACK.previousPosition(currentPosition, 0, 10)
        println(currentPosition)
        currentPosition = Technique.DEAL_WITH_INCREMENT.previousPosition(currentPosition, 7, 10)
        println(currentPosition)
        currentPosition = Technique.CUT_N_CARDS.previousPosition(currentPosition, 6, 10)
        println(currentPosition)
    }

    @Test
    fun functionalCase3() {
        // ARRANGE
        val deck = Deck(10)
        deck.cards.forEachIndexed { index, i ->  if(i == 3) println(index)}

        // ACT
        val result =
            deck
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 9)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.CUT_N_CARDS, -2)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }

        //
        val expected = listOf(6, 3, 0, 7, 4, 1, 8, 5, 2, 9)
        assertEquals(expected, result.cards)
    }
    @Test
    fun revertFunctionalCase3() {
        // ARRANGE
        var currentPosition = 1L
        println(currentPosition)
        // ACT
        currentPosition = Technique.CUT_N_CARDS.previousPosition(currentPosition, -2, 10)
        println(currentPosition)
        currentPosition = Technique.DEAL_WITH_INCREMENT.previousPosition(currentPosition, 9, 10)
        println(currentPosition)
        currentPosition = Technique.DEAL_WITH_INCREMENT.previousPosition(currentPosition, 7, 10)
        println(currentPosition)

        //
        assertEquals(3, currentPosition)
    }


    @Test
    fun functionalCase4() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        deck.cards.forEachIndexed { index, i ->  if(i == 3) println(index)}
        val result =
            deck
                .applyTechnique(Technique.DEAL_INTO_NEW_STACK)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.CUT_N_CARDS, -2)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.CUT_N_CARDS, 8)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.CUT_N_CARDS, -4)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.CUT_N_CARDS, 3)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 9)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 3)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }
                .applyTechnique(Technique.CUT_N_CARDS, -1)
                .also { it.cards.forEachIndexed { index, i ->  if(i == 3) println(index)} }

        //
        val expected = listOf(9, 2, 5, 8, 1, 4, 7, 0, 3, 6)
        assertEquals(expected, result.cards)
    }

    @Test
    fun revertFunctionalCase4() {
        // ARRANGE
        val deck = Deck(10)
        var currentPosition = 8L
        println(currentPosition)
        // ACT
        currentPosition = Technique.CUT_N_CARDS.previousPosition(currentPosition, -1, 10)
        println(currentPosition)
        currentPosition = Technique.DEAL_WITH_INCREMENT.previousPosition(currentPosition, 3, 10)
        println(currentPosition)
        currentPosition = Technique.DEAL_WITH_INCREMENT.previousPosition(currentPosition, 9, 10)
        println(currentPosition)
        currentPosition = Technique.CUT_N_CARDS.previousPosition(currentPosition, 3, 10)
        println(currentPosition)
        currentPosition = Technique.DEAL_WITH_INCREMENT.previousPosition(currentPosition, 7, 10)
        println(currentPosition)
        currentPosition = Technique.CUT_N_CARDS.previousPosition(currentPosition, -4, 10)
        println(currentPosition)
        currentPosition = Technique.CUT_N_CARDS.previousPosition(currentPosition, 8, 10)
        println(currentPosition)
        currentPosition = Technique.DEAL_WITH_INCREMENT.previousPosition(currentPosition, 7, 10)
        println(currentPosition)
        currentPosition = Technique.CUT_N_CARDS.previousPosition(currentPosition, -2, 10)
        println(currentPosition)
        currentPosition = Technique.DEAL_INTO_NEW_STACK.previousPosition(currentPosition, 0, 10)
        println(currentPosition)
        //
        assertEquals(3, currentPosition)
    }
    @Test
    fun real() {
        val input = "/day22.txt".loadFromFile().lines()
        var deck = Deck(10007)
        for(line in input) {
            val technique = line.split(" ")
            if(technique[0] == "cut") {
                deck = deck.applyTechnique(Technique.CUT_N_CARDS, technique[1].toInt())
            } else if(technique[1] == "with") {
                deck = deck.applyTechnique(Technique.DEAL_WITH_INCREMENT, technique.last().toInt())
            } else {
                deck = deck.applyTechnique(Technique.DEAL_INTO_NEW_STACK)
            }
        }
        println(deck.cards[2019])
        assertEquals(6696, deck.cards.indexOf(2019))
    }

    @Test
    fun revertReal() {
        val input = "/day22.txt".loadFromFile().lines()
        var deckSize = 10007L
        var currentPosition = 6696L
        // currentPosition = revertRealInput(currentPosition, deckSize)
        for(instruction in buildInstructions(input.reversed())) {
            currentPosition = instruction.first.previousPosition(currentPosition, instruction.second, deckSize)
        }
        assertEquals(2019, currentPosition)
    }

    fun buildInstructions(input : List<String>) : List<Pair<Technique, Int>> {
        return input.map { line ->
            val technique = line.split(" ")
            if(technique[0] == "cut") {
                Pair(Technique.CUT_N_CARDS, technique.last().toInt())
            } else if(technique[1] == "with") {
                Pair(Technique.DEAL_WITH_INCREMENT, technique.last().toInt())
            } else {
                Pair(Technique.DEAL_INTO_NEW_STACK, 0)
            }

        }
    }

    @Test
    fun revertRealPart2() {
        val input = "/day22.txt".loadFromFile().lines()
        val deckSize = valueOf(119315717514047)
        val step = valueOf(101741582076661)
        var currentPosition = 2020L
        val instructions = buildInstructions(input.reversed())
        val firstValues = IntRange(1, 3).map {
            for (instruction in instructions) {
                currentPosition = instruction.first.previousPosition(currentPosition, instruction.second, deckSize.toLong())
            }
            currentPosition
        }

        val v1 = valueOf(firstValues[0])
        val v2 = valueOf(firstValues[1])
        val v3 = valueOf(firstValues[2])

        // v2 = (a*v1 + b)  % nb_cards
        // v3 = (a*v2 +b) % nb_cards
        // a = (v3-v2)*pow(v2-v1, -1, nb_cards)
        val a = ((v3-v2)*((v2-v1).modInverse(deckSize))).mod(deckSize)
        val b = (v2 - a*v1).mod(deckSize)

        // vn = a^(n-1)*v1+b*(1-a^(n-1))/(1-a)
        val result = (a.modPow(step - ONE, deckSize) * v1 + b * (ONE - a.modPow(step-ONE, deckSize)) * (ONE-a).modInverse(deckSize)).mod(deckSize)

        assertEquals(93750418158025, result.toLong())
    }

}


class Deck{
    val cards : List<Int>

    constructor(cardsSize : Int) {
        cards = initListFromRange(cardsSize)
    }
    constructor(newCards : List<Int>) {
        cards = newCards
    }

    fun applyTechnique(technique: Technique, cardPosition: Int = 0): Deck {
        return Deck(technique.shuffle(cards, cardPosition ))
    }
}

enum class Technique {
    DEAL_INTO_NEW_STACK {
        override fun shuffle(cards: List<Int>, cardPosition: Int): List<Int> {
            return cards.reversed()
        }

        override fun previousPosition(currentPosition: Long, cardPosition: Int, deckSize: Long): Long {
            return deckSize-currentPosition-1
        }

    },
    CUT_N_CARDS {
        override fun shuffle(cards: List<Int>, cardPosition: Int): List<Int> {
            if(cardPosition >= 0) {
                return cards.subList(cardPosition, cards.size) + cards.subList(0, cardPosition)
            } else {
                return cards.subList(cards.size+cardPosition, cards.size) + cards.subList(0, cards.size+cardPosition)
            }
        }

        override fun previousPosition(currentPosition: Long, cardPosition: Int, deckSize: Long): Long {
            return (currentPosition+cardPosition+deckSize) % deckSize
        }

    },
    DEAL_WITH_INCREMENT {
        override fun shuffle(cards: List<Int>, cardPosition: Int): List<Int> {
            val positions = cards.indices.map { if (it == 0) 0 else (it * cardPosition) % cards.size }.toList()
            val arr = IntArray(positions.size)
            for(item in positions.indices) {
                arr[positions[item]] = cards[item]
            }
            return arr.toList()
        }

        override fun previousPosition(currentPosition: Long, cardPosition: Int, deckSize: Long): Long {
            val modInverse =
                valueOf(cardPosition.toLong()).modInverse(valueOf(deckSize))
            return (valueOf(currentPosition) * valueOf(modInverse.toLong())).mod(valueOf(deckSize)).toLong()
        }
    };

    abstract fun shuffle(cards: List<Int>, cardPosition:Int = 0) : List<Int>

    abstract fun previousPosition(currentPosition: Long, cardPosition: Int = 0, deckSize: Long) : Long

}

fun initListFromRange(cardsNumber: Int) = IntRange(0, cardsNumber-1).map { it }
