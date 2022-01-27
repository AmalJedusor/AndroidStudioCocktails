package fr.uha.hassenforder.teams.ui.cocktail;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.hassenforder.livedata.Transformations;
import fr.uha.hassenforder.teams.R;
import fr.uha.hassenforder.teams.database.CocktailDao;
import fr.uha.hassenforder.teams.database.DeltaUtil;
import fr.uha.hassenforder.teams.database.PersonDao;
import fr.uha.hassenforder.teams.model.Cocktail;
import fr.uha.hassenforder.teams.model.FullCocktail;
import fr.uha.hassenforder.teams.model.FullPerson;
import fr.uha.hassenforder.teams.model.Gender;
import fr.uha.hassenforder.teams.model.Person;
import fr.uha.hassenforder.teams.model.Skill;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;
import fr.uha.hassenforder.teams.ui.Savable;

public class CocktailViewModel extends ViewModel {

    static private final String TAG = CocktailViewModel.class.getSimpleName();

    private CocktailDao cocktailDao;
    private MutableLiveData<Long> id = new MutableLiveData<>();
    private LiveData<FullCocktail> fullCocktail;
    private LiveData<Cocktail> cocktail;
    private MutableLiveData<String> cocktailName;
    private MutableLiveData<String> cocktailPic;
    private MutableLiveData<Boolean> alcohol;
    private MutableLiveData<String> highRes;
    private MutableLiveData<Bitmap> lowRes;
    private MutableLiveData<Integer> avatar;
    private MutableLiveData<Integer> height;
    private MediatorLiveData<Double> bmi;

    private CocktailComparator comparator;
    private CocktailValidator validator;
    private Savable savable;

    private LiveData<Integer> bmiValidator;

    public void setCocktailDao(CocktailDao cocktailDao) {
        this.cocktailDao = cocktailDao;
        this.fullCocktail = Transformations.switchMap(id, p -> cocktailDao.getFullById(p));
        this.cocktail = Transformations.map(fullCocktail, p -> p.cocktail);
        this.cocktailName = Transformations.map(cocktail, p -> p.getCocktailName());
        this.cocktailPic = Transformations.map(cocktail, p -> p.getCocktailPic());
        this.alcohol = Transformations.map(cocktail, p -> p.getAlcohol());
        this.highRes = Transformations.map(cocktail, p -> p.getHighRes());
        this.lowRes = Transformations.map(cocktail, p -> p.getLowRes());
        this.avatar = Transformations.map(cocktail, p -> p.getAvatar());
        this.height = Transformations.map(cocktail, p -> p.getHeight());
        comparator = new CocktailComparator(this);
        validator = new CocktailValidator(this);
        savable = new Savable(comparator.getModified(), validator.getValidated());

        this.bmi = new MediatorLiveData<>();

    }

    private List<SkillPersonAssociation> clone (List<SkillPersonAssociation> original) {
        if (original == null) return null;
        List<SkillPersonAssociation> copy = new ArrayList<>(original.size());
        for (SkillPersonAssociation skill : original) {
            copy.add(skill.clone());
        }
        return copy;
    }



    public LiveData<FullCocktail> getFullCocktail() {
        return fullCocktail;
    }

    public LiveData<Cocktail> getCocktail() {
        return cocktail;
    }

    public MutableLiveData<String> getCocktailName() {
        return cocktailName;
    }

    public MutableLiveData<String> getCocktailPic() {
        return cocktailPic;
    }

    public MutableLiveData<Boolean> getAlcohol() {
        return alcohol;
    }


    public MutableLiveData<String> getHighRes() {
        return highRes;
    }

    public MutableLiveData<Bitmap> getLowRes() {
        return lowRes;
    }

    public MutableLiveData<Integer> getAvatar() {
        return avatar;
    }

    public MutableLiveData<Integer> getHeight() {
        return height;
    }


    public LiveData<Double> getBmi() {
        return bmi;
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

    public LiveData<Integer> getAlcoholValidator() {
        return validator.getAlcoholValidator();
    }


    public LiveData<Integer> getHeightValidator() {
        return validator.getHeightValidator();
    }

    public LiveData<Integer> getBmiValidator() {
        return bmiValidator;
    }

    public LiveData<Boolean> getValidated() {
        return validator.getValidated();
    }

    public LiveData<Boolean> getSavable() {
        return savable.getSavable();
    }

    public void setHighRes(String absoluteFile) {
        getHighRes().postValue(absoluteFile);
    }

    public void setLowRes(Bitmap bitmap) {
        getLowRes().postValue(bitmap);
    }

    public void setAvatar(int avatar) {
        getAvatar().postValue(avatar);
    }



    public void save() {
        Cocktail cocktail = new Cocktail(
                id.getValue(),
                cocktailName.getValue(),
                cocktailPic.getValue(),
                alcohol.getValue()
        );
        DeltaUtil<SkillPersonAssociation, SkillPersonAssociation> delta = new DeltaUtil<SkillPersonAssociation, SkillPersonAssociation>() {
            @Override
            protected long getId(SkillPersonAssociation skill) {
                return skill.getSid();
            }

            @Override
            protected boolean same(SkillPersonAssociation initial, SkillPersonAssociation now) {
                return SkillPersonAssociation.compare(initial, now);
            }

            @Override
            protected SkillPersonAssociation createFor(SkillPersonAssociation skill) {
                return skill;
            }

        };


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cocktailDao.upsert(cocktail);

            }
        });
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public void createCocktail() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Cocktail p = new Cocktail();
                long id = cocktailDao.upsert(p);
                setId(id);
            }
        });

    }

}