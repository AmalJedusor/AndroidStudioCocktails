package fr.uha.hassenforder.teams.model;

import androidx.room.Embedded;

public class CocktailWithDetails {

    @Embedded
    public Cocktail cocktail;
    public int stepsCount;

    public static boolean compare(CocktailWithDetails newCocktail, CocktailWithDetails oldCocktail) {
        if (! Cocktail.compare (newCocktail.cocktail, oldCocktail.cocktail)) return false;
        if (! CompareUtil.compare(newCocktail.stepsCount, oldCocktail.stepsCount)) return false;

        return true;
    }
}
