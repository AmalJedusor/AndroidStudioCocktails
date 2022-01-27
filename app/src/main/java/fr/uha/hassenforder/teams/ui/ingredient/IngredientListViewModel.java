package fr.uha.hassenforder.teams.ui.ingredient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.hassenforder.teams.database.CocktailDao;
import fr.uha.hassenforder.teams.database.IngredientDao;
import fr.uha.hassenforder.teams.model.Cocktail;
import fr.uha.hassenforder.teams.model.CocktailWithDetails;
import fr.uha.hassenforder.teams.model.Ingredient;

public class IngredientListViewModel extends ViewModel {


    private IngredientDao ingredientDao;
    private MediatorLiveData<List<Ingredient>> ingredients;

    public void setIngredientDao(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
        this.ingredients = new MediatorLiveData<>();
        this.ingredients.addSource(ingredientDao.getAll(), ingredients::setValue);
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredients;
    }

    public void deleteIngredient(Ingredient ing) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ingredientDao.delete(ing);
            }
        });
    }

}
