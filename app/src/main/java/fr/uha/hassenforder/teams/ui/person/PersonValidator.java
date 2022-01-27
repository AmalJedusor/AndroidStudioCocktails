package fr.uha.hassenforder.teams.ui.person;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import fr.uha.hassenforder.livedata.Transformations;
import fr.uha.hassenforder.teams.R;
import fr.uha.hassenforder.teams.model.Gender;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;

public class PersonValidator {

    private PersonViewModel vm;
    private LiveData<Integer> firstNameValidator;
    private LiveData<Integer> lastNameValidator;
    private LiveData<Integer> phoneValidator;
    private LiveData<Integer> genderValidator;
    private LiveData<Integer> heightValidator;
    private LiveData<Integer> weightValidator;
    private MutableLiveData<Integer> skillsValidator;

    private MediatorLiveData<Boolean> validated;

    public PersonValidator(PersonViewModel vm) {
        this.vm = vm;
        this.firstNameValidator = Transformations.map(vm.getFirstName(), v -> validateFirstName(v));
        this.lastNameValidator = Transformations.map(vm.getLastName(), v -> validateLastName(v));
        this.phoneValidator = Transformations.map(vm.getPhone(), v -> validatePhone(v));
        this.genderValidator = Transformations.map(vm.getGender(), v -> validateGender(v));
        this.heightValidator = Transformations.map(vm.getHeight(), v -> validateHeight(v));
        this.weightValidator = Transformations.map(vm.getWeight(), v -> validateWeight(v));
        this.skillsValidator = Transformations.map(vm.getSkills(), v -> validateSkills(v));

        this.validated = new MediatorLiveData<>();
        this.validated.setValue(Boolean.FALSE);
        this.validated.addSource(firstNameValidator, personValidatorObserver );
        this.validated.addSource(lastNameValidator, personValidatorObserver );
        this.validated.addSource(phoneValidator, personValidatorObserver );
        this.validated.addSource(genderValidator, personValidatorObserver );
        this.validated.addSource(heightValidator, personValidatorObserver );
        this.validated.addSource(weightValidator, personValidatorObserver );
        this.validated.addSource(skillsValidator, personValidatorObserver );
    }

    public void skillItemUpdate(int position) {
        this.skillsValidator.postValue(validateSkills(vm.getSkills().getValue()));
    }

    public LiveData<Integer> getFirstNameValidator() {
        return firstNameValidator;
    }

    public LiveData<Integer> getLastNameValidator() {
        return lastNameValidator;
    }

    public LiveData<Integer> getPhoneValidator() {
        return phoneValidator;
    }

    public LiveData<Integer> getGenderValidator() {
        return genderValidator;
    }

    public LiveData<Integer> getHeightValidator() {
        return heightValidator;
    }

    public LiveData<Integer> getWeightValidator() {
        return weightValidator;
    }

    public LiveData<Boolean> getValidated() {
        return validated;
    }

    private Observer<Integer> personValidatorObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer o) {
            boolean valid = true;
            valid = valid && firstNameValidator.getValue() != null && firstNameValidator.getValue() == 0;
            valid = valid && lastNameValidator.getValue() != null && lastNameValidator.getValue() == 0;
            valid = valid && phoneValidator.getValue() != null && phoneValidator.getValue() == 0;
            valid = valid && genderValidator.getValue() != null && genderValidator.getValue() == 0;
            valid = valid && heightValidator.getValue() != null && heightValidator.getValue() == 0;
            valid = valid && weightValidator.getValue() != null && weightValidator.getValue() == 0;
            validated.postValue(valid);
        }
    };

    private int validateFirstName(String value) {
        if (value == null) return R.string.field_not_null;
        if (value.isEmpty()) return R.string.field_not_empty;
        return 0;
    }

    private int validateLastName(String value) {
        if (value == null) return R.string.field_not_null;
        if (value.isEmpty()) return R.string.field_not_empty;
        return 0;
    }

    private int validatePhone(String value) {
        if (value == null) return R.string.field_not_null;
        if (value.isEmpty()) return R.string.field_not_empty;
        if (value.length() != 10 && value.length() != 4) return R.string.phone_4_or_10_numbers;
        if (! value.matches("\\d*")) return R.string.phone_numbers_only;
        return 0;
    }

    private int validateGender(Gender value) {
        if (value == null) return R.string.field_not_null;
        switch (value) {
        case GIRL: return 0;
        case BOY: return 0;
        }
        return R.string.gender_not_valid;
    }

    private int validateHeight(int value) {
        if (value < 80) return R.string.height_too_small;
        if (value > 220) return R.string.height_too_big;
        return 0;
    }

    private int validateWeight(int value) {
        if (value < 30) return R.string.weight_too_small;
        if (value > 180) return R.string.weight_too_big;
        return 0;
    }

    private int validateSkills(List<SkillPersonAssociation> associations) {
        return 0;
    }
}
