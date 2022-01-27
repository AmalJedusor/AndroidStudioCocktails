package fr.uha.hassenforder.teams.ui.person;

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
import fr.uha.hassenforder.teams.database.DeltaUtil;
import fr.uha.hassenforder.teams.database.PersonDao;
import fr.uha.hassenforder.teams.model.FullPerson;
import fr.uha.hassenforder.teams.model.Gender;
import fr.uha.hassenforder.teams.model.Person;
import fr.uha.hassenforder.teams.model.Skill;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;
import fr.uha.hassenforder.teams.ui.Savable;

public class PersonViewModel extends ViewModel {

    static private final String TAG = PersonViewModel.class.getSimpleName();

    private PersonDao personDao;
    private MutableLiveData<Long> id = new MutableLiveData<>();
    private LiveData<FullPerson> fullPerson;
    private LiveData<Person> person;
    private MutableLiveData<String> firstName;
    private MutableLiveData<String> lastName;
    private MutableLiveData<String> phone;
    private MutableLiveData<Gender> gender;
    private MutableLiveData<String> highRes;
    private MutableLiveData<Bitmap> lowRes;
    private MutableLiveData<Integer> avatar;
    private MutableLiveData<Integer> height;
    private MutableLiveData<Integer> weight;
    private MediatorLiveData<Double> bmi;
    private MediatorLiveData<List<SkillPersonAssociation>> skills;

    private PersonComparator comparator;
    private PersonValidator validator;
    private Savable savable;

    private LiveData<Integer> bmiValidator;

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
        this.fullPerson = Transformations.switchMap(id, p -> personDao.getFullById(p));
        this.person = Transformations.map(fullPerson, p -> p.person);
        this.firstName = Transformations.map(person, p -> p.getFirstName());
        this.lastName = Transformations.map(person, p -> p.getLastName());
        this.phone = Transformations.map(person, p -> p.getPhone());
        this.gender = Transformations.map(person, p -> p.getGender());
        this.highRes = Transformations.map(person, p -> p.getHighRes());
        this.lowRes = Transformations.map(person, p -> p.getLowRes());
        this.avatar = Transformations.map(person, p -> p.getAvatar());
        this.height = Transformations.map(person, p -> p.getHeight());
        this.weight = Transformations.map(person, p -> p.getWeight());
        this.skills = Transformations.map(fullPerson, p -> clone(p.skills));

        comparator = new PersonComparator(this);
        validator = new PersonValidator(this);
        savable = new Savable(comparator.getModified(), validator.getValidated());

        this.bmi = new MediatorLiveData<>();
        this.bmi.addSource(height, bmiObserver);
        this.bmi.addSource(weight, bmiObserver);
        this.bmiValidator = Transformations.map(bmi, v -> validateBmi(v));
    }

    private List<SkillPersonAssociation> clone (List<SkillPersonAssociation> original) {
        if (original == null) return null;
        List<SkillPersonAssociation> copy = new ArrayList<>(original.size());
        for (SkillPersonAssociation skill : original) {
            copy.add(skill.clone());
        }
        return copy;
    }

    private Observer<Integer> bmiObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            double value = 0.0;
            if (getHeight().getValue() != 0) {
                double w = getWeight().getValue();
                double h = getHeight().getValue() / 100.0;
                value = w/(h*h);
            }
            bmi.postValue(value);
        }
    };

    private int validateBmi(double value) {
        if (value == 0.0) return 0;
        if (value > 40) return R.string.bmi_deadly;
        if (value > 35) return R.string.bmi_high;
        if (value > 30) return R.string.bmi_moderate;
        if (value > 25) return R.string.bmi_overweight;
        if (value > 18.5) return 0;
        if (value > 16.5) return R.string.bmi_underweight;
        return R.string.bmi_starvation;
    }

    public LiveData<FullPerson> getFullPerson() {
        return fullPerson;
    }

    public LiveData<Person> getPerson() {
        return person;
    }

    public MutableLiveData<String> getFirstName() {
        return firstName;
    }

    public MutableLiveData<String> getLastName() {
        return lastName;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public LiveData<Gender> getGender() {
        return gender;
    }

    public void setGender (Gender gender) {
        this.gender.postValue(gender);
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

    public MutableLiveData<Integer> getWeight() {
        return weight;
    }

    public LiveData<Double> getBmi() {
        return bmi;
    }

    public MediatorLiveData<List<SkillPersonAssociation>> getSkills() {
        return skills;
    }

    public LiveData<Boolean> getModified() {
        return comparator.getModified();
    }

    public LiveData<Integer> getFirstNameValidator() {
        return validator.getFirstNameValidator();
    }

    public LiveData<Integer> getLastNameValidator() {
        return validator.getLastNameValidator();
    }

    public LiveData<Integer> getPhoneValidator() {
        return validator.getPhoneValidator();
    }

    public LiveData<Integer> getGenderValidator() {
        return validator.getGenderValidator();
    }

    public LiveData<Integer> getHeightValidator() {
        return validator.getHeightValidator();
    }

    public LiveData<Integer> getWeightValidator() {
        return validator.getWeightValidator();
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

    public void removeSkill(SkillPersonAssociation skill) {
        skills.getValue().remove(skill);
        skills.setValue(skills.getValue());
    }

    public void addSkill(Skill skill, float level) {
        SkillPersonAssociation association = new SkillPersonAssociation(0, id.getValue(), skill, level);
        skills.getValue().add(association);
        skills.setValue(skills.getValue());
    }

    public void updatedSkill(int position) {
        validator.skillItemUpdate (position);
        comparator.skillItemUpdate (position);
    }

    public void save() {
        Person person = new Person(
                id.getValue(),
                firstName.getValue(),
                lastName.getValue(),
                phone.getValue(),
                gender.getValue(),
                highRes.getValue(),
                lowRes.getValue(),
                avatar.getValue(),
                height.getValue(),
                weight.getValue()
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
        List<SkillPersonAssociation> oldList = this.fullPerson.getValue().skills;
        List<SkillPersonAssociation> newList = this.skills.getValue();
        delta.calculate(oldList, newList);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                personDao.upsert(person);
                personDao.removeSkills(delta.getToRemove());
                personDao.addSkills(delta.getToAdd());
                personDao.addSkills(delta.getToUpdate());
            }
        });
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public void createPerson() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Person p = new Person();
                long id = personDao.upsert(p);
                setId(id);
            }
        });

    }

}