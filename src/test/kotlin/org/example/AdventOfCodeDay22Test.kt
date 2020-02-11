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
    fun applyCutTechniqueDealWithIncrementDeck() {
        // ARRANGE
        val deck = Deck(10)

        // ACT
        val result = deck.applyTechnique(Technique.DEAL_WITH_INCREMENT, 3)

        //
        val expected = listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3)
        assertEquals(expected, result.cards)
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

    },
    CUT_N_CARDS {
        override fun shuffle(cards: List<Int>, cardPosition: Int): List<Int> {
            return cards.subList(cardPosition, cards.size) + cards.subList(0, cardPosition)
        }
    },
    DEAL_WITH_INCREMENT {
        override fun shuffle(cards: List<Int>, cardPosition: Int): List<Int> {
            val positions = cards.map { if (it == 0) 0 else (it * cardPosition) % cards.size }.toList()
            return positions
        }
    };

    abstract fun shuffle(cards: List<Int>, cardPosition:Int = 0) : List<Int>
}

fun initListFromRange(cardsNumber: Int) = (0 until cardsNumber).map { it }
