package fr.uha.hassenforder.teams.ui.cocktail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.hassenforder.teams.database.CocktailDao;
import fr.uha.hassenforder.teams.database.PersonDao;
import fr.uha.hassenforder.teams.model.Cocktail;
import fr.uha.hassenforder.teams.model.CocktailWithDetails;
import fr.uha.hassenforder.teams.model.Person;
import fr.uha.hassenforder.teams.model.PersonWithDetails;

public class CocktailListViewModel extends ViewModel {

    private CocktailDao cocktailDao;
    private MediatorLiveData<List<CocktailWithDetails>> cocktails;

    public void setCocktailDao(CocktailDao cocktailDao) {
        this.cocktailDao = cocktailDao;
        this.cocktails = new MediatorLiveData<>();
        this.cocktails.addSource(cocktailDao.getAll(), cocktails::setValue);
    }

    public LiveData<List<CocktailWithDetails>> getCocktails() {
        return cocktails;
    }

    public void deleteCocktail(Cocktail cocktail) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cocktailDao.delete(cocktail);
            }
        });
    }

}