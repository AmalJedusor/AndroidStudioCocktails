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
import fr.uha.hassenforder.teams.model.FullPerson;
import fr.uha.hassenforder.teams.model.Person;
import fr.uha.hassenforder.teams.model.PersonWithDetails;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;

@Dao
public interface CocktailDao {


        // cocktail avec son nombre d'étapes
        @Transaction
        @Query("SELECT * "
                + ", (SELECT COUNT(*) FROM steps ST WHERE ST.cid = C.cid) AS stepsCount"
                + " FROM cocktails AS C")
        LiveData<List<CocktailWithDetails>> getAll ();

        @Transaction
        @Query("SELECT * FROM cocktails WHERE cid = :id")
        LiveData<FullCocktail> getFullById (long id);

        @Query("SELECT * FROM cocktails WHERE cid = :id")
        LiveData<Cocktail> getCocktailsById (long id);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long upsert (Cocktail cocktail);

        @Delete
        void delete (Cocktail cocktail);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void addStep(CocktailIngredientAssociation step);

        @Delete
        void removeStep(CocktailIngredientAssociation step);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void addSteps(List<CocktailIngredientAssociation> step);

        @Delete
        void removeSteps(List<CocktailIngredientAssociation> step);
        @Query("DELETE FROM cocktails")
        void deleteAll();

}
