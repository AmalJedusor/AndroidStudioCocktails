package fr.uha.hassenforder.teams.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients")

// recycler view ?
public class Ingredient {
    public void setIngid(long ingid) {
        this.ingid = ingid;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setIngredientType(IngredientType ingredientType) {
        this.ingredientType = ingredientType;
    }

    @PrimaryKey(autoGenerate = true)
    private long ingid;

    private String ingredientName;
    private IngredientType ingredientType;
    public Ingredient() {
        this.ingid = 0;
    }

    @Ignore
    public Ingredient(long pid, String ingredientName, IngredientType ingredientType ) {
        this.ingid = pid;
        this.ingredientName = ingredientName;
        this.ingredientType = ingredientType;


    }

    public long getIngid() {
        return ingid;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public IngredientType getIngredientType() {
        return ingredientType;
    }

    public static boolean compare(Ingredient newIngredient, Ingredient oldIngredient) {
        if (newIngredient.getIngid() != newIngredient.getIngid()) return false;
       return true;
    }
}
