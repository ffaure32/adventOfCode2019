package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        val target = 9

        // ACT
        val result = Technique.CUT_N_CARDS.previousPosition(target, 3, 10)

        //
        assertEquals(2, result)
    }

    @Test
    fun revertCutTechniqueSplitsAndNegativeDeck() {
        // ARRANGE
        val target = 4

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
        val result = Technique.DEAL_WITH_INCREMENT.previousPosition(4, 4, 10)

        //
        assertEquals(2, result)
        /*
        0 -> 0
        1 -> 7
        2 -> 4
        3 -> 1
        4 -> 8
        5 -> 5
        6 -> 2
        7 -> 9
        8 -> 6
        9 -> 3
         */
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

        //
        val expected = listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3)
        assertEquals(expected, result.cards)
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

        // ACT
        val result =
            deck
                .applyTechnique(Technique.CUT_N_CARDS, 6)
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .applyTechnique(Technique.DEAL_INTO_NEW_STACK)

        //
        val expected = listOf(3, 0, 7, 4, 1, 8, 5, 2, 9, 6)
        assertEquals(expected, result.cards)
    }

    @Test
    fun functionalCase3() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result =
            deck
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 9)
                .applyTechnique(Technique.CUT_N_CARDS, -2)

        //
        val expected = listOf(6, 3, 0, 7, 4, 1, 8, 5, 2, 9)
        assertEquals(expected, result.cards)
    }

    @Test
    fun functionalCase4() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result =
            deck
                .applyTechnique(Technique.DEAL_INTO_NEW_STACK)
                .applyTechnique(Technique.CUT_N_CARDS, -2)
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .applyTechnique(Technique.CUT_N_CARDS, 8)
                .applyTechnique(Technique.CUT_N_CARDS, -4)
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 7)
                .applyTechnique(Technique.CUT_N_CARDS, 3)
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 9)
                .applyTechnique(Technique.DEAL_WITH_INCREMENT, 3)
                .applyTechnique(Technique.CUT_N_CARDS, -1)

        //
        val expected = listOf(9, 2, 5, 8, 1, 4, 7, 0, 3, 6)
        assertEquals(expected, result.cards)
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
        assertEquals(6696, deck.cards.indexOf(2019))
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

        override fun previousPosition(currentPosition: Int, cardPosition: Int, deckSize: Int): Int {
            return deckSize-currentPosition
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

        override fun previousPosition(currentPosition: Int, cardPosition: Int, deckSize: Int): Int {
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

        override fun previousPosition(currentPosition: Int, cardPosition: Int, deckSize: Int): Int {
            return (deckSize/cardPosition)*(currentPosition%cardPosition)
        }
    };

    abstract fun shuffle(cards: List<Int>, cardPosition:Int = 0) : List<Int>

    abstract fun previousPosition(currentPosition: Int, cardPosition: Int = 0, deckSize: Int) : Int

}

fun initListFromRange(cardsNumber: Int) = (0 until cardsNumber).map { it }
