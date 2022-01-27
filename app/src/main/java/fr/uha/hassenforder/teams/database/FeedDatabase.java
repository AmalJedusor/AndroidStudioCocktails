package fr.uha.hassenforder.teams.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.hassenforder.teams.R;
import fr.uha.hassenforder.teams.model.Cocktail;
import fr.uha.hassenforder.teams.model.Gender;
import fr.uha.hassenforder.teams.model.Objective;
import fr.uha.hassenforder.teams.model.Person;
import fr.uha.hassenforder.teams.model.Skill;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;
import fr.uha.hassenforder.teams.model.Team;
import fr.uha.hassenforder.teams.model.TeamPersonAssociation;

public class FeedDatabase {

    static private Random rnd = new Random();

    private long [] feedCocktails() {
        AppDatabase.get().getCocktailDao().deleteAll();
        CocktailDao dao = AppDatabase.get().getCocktailDao();
        long [] ids = new long [8];
        ids[0] = dao.upsert(new Cocktail(1, "Mojito","mojito",true));
        ids[1] = dao.upsert(new Cocktail(2, "Chocolate","chocolate",true));
        ids[3] = dao.upsert(new Cocktail(3, "Sweet Bananas","sweet_banana",true));
        ids[4] = dao.upsert(new Cocktail(4, "Margarita","margarita",true));
        ids[5] = dao.upsert(new Cocktail(5, "Sex on the beach","sex_on_the_beach",true));
        ids[6] = dao.upsert(new Cocktail(6, "Whiskey Sour","whiskey_sour",true));
        ids[7] = dao.upsert(new Cocktail(7, "Swedish Coffee","swedish_coffee",true));


        return ids;
    }



    public void feed () {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long [] cids = feedCocktails ();
            }
        });
    }

}
