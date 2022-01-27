package fr.uha.hassenforder.teams.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;


public class FullCocktail {
    @Embedded
    public Cocktail cocktail;
    @Relation(
            parentColumn = "cid",
            entityColumn = "ingid",
            associateBy = @Junction(CocktailIngredientAssociation.class)
    )
    public List<Ingredient> steps;


}
