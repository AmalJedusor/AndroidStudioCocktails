package fr.uha.hassenforder.teams.ui.ingredient;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.hassenforder.livedata.Transformations;
import fr.uha.hassenforder.teams.database.DeltaUtil;
import fr.uha.hassenforder.teams.database.IngredientDao;
import fr.uha.hassenforder.teams.model.Cocktail;
import fr.uha.hassenforder.teams.model.CocktailIngredientAssociation;
import fr.uha.hassenforder.teams.model.FullCocktail;
import fr.uha.hassenforder.teams.model.Ingredient;
import fr.uha.hassenforder.teams.model.IngredientType;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;
import fr.uha.hassenforder.teams.ui.Savable;
import fr.uha.hassenforder.teams.ui.cocktail.CocktailComparator;
import fr.uha.hassenforder.teams.ui.cocktail.CocktailValidator;

public class IngredientViewModel extends ViewModel {

    static private final String TAG = IngredientViewModel.class.getSimpleName();

    private IngredientDao ingredientDao;
    private MutableLiveData<Long> id = new MutableLiveData<>();
    private LiveData<Ingredient> ingredient;
    private MutableLiveData<String> ingredientName;
    private MutableLiveData<IngredientType> ingredientType;

    private CocktailComparator comparator;
    private CocktailValidator validator;
    private Savable savable;

    public void setIngredientDao(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;

        this.ingredient = Transformations.map(ingredient, i -> i);
        this.ingredientName = Transformations.map(ingredient, i -> i.getIngredientName());
        this.ingredientType = Transformations.map(ingredient, i -> i.getIngredientType());


    }

    private List<SkillPersonAssociation> clone (List<SkillPersonAssociation> original) {
        if (original == null) return null;
        List<SkillPersonAssociation> copy = new ArrayList<>(original.size());
        for (SkillPersonAssociation skill : original) {
            copy.add(skill.clone());
        }
        return copy;
    }


    public LiveData<Ingredient> getIngredient() {
        return ingredient;
    }

    public MutableLiveData<String> getIngredientName() {
        return ingredientName;
    }

    public MutableLiveData<IngredientType> getIngredientType() {
        return ingredientType;
    }

    public LiveData<Boolean> getModified() {
        return comparator.getModified();
    }

    public LiveData<Integer> getCocktailNameValidator() {
        return validator.getCocktailNameValidator();
    }

    public LiveData<Integer> getCocktailPicValidator() {
        return validator.getCocktailPicValidator();
    }


    public LiveData<Boolean> getValidated() {
        return validator.getValidated();
    }

    public LiveData<Boolean> getSavable() {
        return savable.getSavable();
    }



    public void save(Ingredient ingredient) {

        DeltaUtil<CocktailIngredientAssociation, CocktailIngredientAssociation> delta = new DeltaUtil<CocktailIngredientAssociation, CocktailIngredientAssociation>() {
            @Override
            protected long getId(CocktailIngredientAssociation step) {
                return step.ingid;            }

            @Override
            protected boolean same(CocktailIngredientAssociation initial, CocktailIngredientAssociation now) {
                return false;
            }

            @Override
            protected CocktailIngredientAssociation createFor(CocktailIngredientAssociation object) {
                return null;
            }


        };

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ingredientDao.upsert(ingredient);

            }
        });
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public void createIngredient() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Ingredient i = new Ingredient();
                long id = ingredientDao.upsert(i);
                setId(id);
            }
        });

    }

}