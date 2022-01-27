package fr.uha.hassenforder.teams.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.uha.hassenforder.teams.model.Cocktail;
import fr.uha.hassenforder.teams.model.CocktailIngredientAssociation;
import fr.uha.hassenforder.teams.model.Ingredient;
import fr.uha.hassenforder.teams.model.Person;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;
import fr.uha.hassenforder.teams.model.Team;
import fr.uha.hassenforder.teams.model.TeamPersonAssociation;

@TypeConverters({DatabaseTypeConverters.class})
@Database(entities = {
        Person.class,
        SkillPersonAssociation.class,
        Team.class,
        TeamPersonAssociation.class,
        Cocktail.class,
        Ingredient.class,
        CocktailIngredientAssociation.class,


}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;

    static public void create(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "teams.db").build();
        }
    }

    static public AppDatabase get () {
        return instance;
    }

    public abstract PersonDao getPersonDao ();
    public abstract TeamDao getTeamDao ();
    public abstract CocktailDao getCocktailDao();
    public abstract IngredientDao getIngredientDao();
}
