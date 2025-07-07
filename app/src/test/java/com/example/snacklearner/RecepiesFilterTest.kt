package com.example.snacklearner

import com.example.snacklearner.model.Recipe
import com.example.snacklearner.util.RecipeFilter
import org.junit.Assert.*
import org.junit.Test
class RecepiesFilterTest {
    private val recipes = listOf(
        Recipe("Salata od tune", "Jednostavna salata s tunom.", "Tuna, majoneza, luk"),
        Recipe("Smoothie od banane", "Slatki i zdravi smoothie.", "Banana, mlijeko, med"),
        Recipe("Tost s avokadom", "Brzi doručak ili međuobrok.", "Avokado, kruh, jaje"),
        Recipe("Piletina s povrćem", "Zdravi ručak", "Piletina, brokula, mrkva, paprika"),
        Recipe("Juha od rajčice", "Fina juha", "Rajčica, luk, češnjak")
    )

    @Test
    fun filterRecipes_matchesTitle() {
        val result = RecipeFilter().filterRecipes("Tost s avokadom", recipes)
        assertEquals(1, result.size)
        assertEquals("Tost s avokadom", result[0].title)
    }

    @Test
    fun filterRecipes_matchesIngredient() {
        val result = RecipeFilter().filterRecipes("Banana", recipes)
        assertEquals(1, result.size)
        assertEquals("Smoothie od banane", result[0].title)
    }

    @Test
    fun filterRecipes_emptyQuery_returnsAll() {
        val result = RecipeFilter().filterRecipes("", recipes)
        assertEquals(5, result.size)
    }

    @Test
    fun filterRecipes_noMatch_returnsEmptyList() {
        val result = RecipeFilter().filterRecipes("Kava", recipes)
        assertTrue(result.isEmpty())
    }
}