package fr.uha.hassenforder.teams.ui.cocktail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import fr.uha.hassenforder.livedata.Transformations;
import fr.uha.hassenforder.teams.R;
import fr.uha.hassenforder.teams.model.Gender;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;

public class CocktailValidator {

    private CocktailViewModel vm;
    private LiveData<Integer> cocktailNameValidator;
    private LiveData<Integer> cocktailPicValidator;
    private LiveData<Integer> alcoholValidator;
    private LiveData<Integer> heightValidator;


    private MediatorLiveData<Boolean> validated;

    public CocktailValidator(CocktailViewModel vm) {
        this.vm = vm;
        this.cocktailNameValidator = Transformations.map(vm.getCocktailName(), v -> validateCocktailName(v));
        this.cocktailPicValidator = Transformations.map(vm.getCocktailPic(), v -> validateCocktailPic(v));
        this.alcoholValidator = Transformations.map(vm.getAlcohol(), v -> validateAlcohol(v));
        this.heightValidator = Transformations.map(vm.getHeight(), v -> validateHeight(v));


        this.validated = new MediatorLiveData<>();
        this.validated.setValue(Boolean.FALSE);
        this.validated.addSource(cocktailNameValidator, personValidatorObserver );
        this.validated.addSource(cocktailPicValidator, personValidatorObserver );
        this.validated.addSource(alcoholValidator, personValidatorObserver );

        this.validated.addSource(heightValidator, personValidatorObserver );

    }

    public LiveData<Integer> getCocktailNameValidator() {
        return cocktailNameValidator;
    }

    public LiveData<Integer> getCocktailPicValidator() {
        return cocktailPicValidator;
    }

    public LiveData<Integer> getAlcoholValidator() {
        return alcoholValidator;
    }

    public LiveData<Integer> getHeightValidator() {
        return heightValidator;
    }


    public LiveData<Boolean> getValidated() {
        return validated;
    }

    private Observer<Integer> personValidatorObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer o) {
            boolean valid = true;
            valid = valid && cocktailNameValidator.getValue() != null && cocktailNameValidator.getValue() == 0;
            valid = valid && cocktailPicValidator.getValue() != null && cocktailPicValidator.getValue() == 0;
            valid = valid && alcoholValidator.getValue() != null && alcoholValidator.getValue() == 0;

            valid = valid && heightValidator.getValue() != null && heightValidator.getValue() == 0;

            validated.postValue(valid);
        }
    };

    private int validateCocktailName(String value) {
        if (value == null) return R.string.field_not_null;
        if (value.isEmpty()) return R.string.field_not_empty;
        return 0;
    }

    private int validateCocktailPic(String value) {
        if (value == null) return R.string.field_not_null;
        if (value.isEmpty() ) return R.string.field_not_empty;
        return 0;
    }

    private int validateAlcohol(Boolean value) {
        if (value == null) return R.string.field_not_null;

        return 0;
    }


    private int validateHeight(int value) {
        if (value < 80) return R.string.height_too_small;
        if (value > 220) return R.string.height_too_big;
        return 0;
    }


}
