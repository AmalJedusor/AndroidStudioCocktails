package fr.uha.hassenforder.teams.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import fr.uha.hassenforder.teams.model.Cocktail;
import fr.uha.hassenforder.teams.model.CocktailIngredientAssociation;
import fr.uha.hassenforder.teams.model.CocktailWithDetails;
import fr.uha.hassenforder.teams.model.FullCocktail;
import fr.uha.hassenforder.teams.model.Ingredient;

@Dao
public interface IngredientDao {


    @Query("SELECT * FROM ingredients WHERE ingid = :id")
    LiveData<Ingredient> getIngredientsById (long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert (Ingredient ingredient);

    @Delete
    void delete (Ingredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addIngredient(CocktailIngredientAssociation step);

    @Delete
    void removeStep(CocktailIngredientAssociation step);

    @Query("SELECT * FROM ingredients")
    LiveData<List<Ingredient>>  getAll();
}
