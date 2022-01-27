package fr.uha.hassenforder.teams.ui.person;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.uha.hassenforder.teams.model.SkillPersonAssociation;

public class PersonComparator {

    private PersonViewModel vm;
    private MediatorLiveData<Boolean> modified;

    private enum Field {
        FIRSTNAME,
        LASTNAME,
        PHONE,
        GENDER,
        HEIGHT,
        WEIGHT,
        HIGHRES,
        LOWRES,
        AVATAR,
        SKILLS
    }

    private EnumMap<Field, Boolean> states;

    public PersonComparator(PersonViewModel vm) {
        this.vm = vm;
        states = new EnumMap<Field, Boolean>(Field.class);
        for (Field key : Field.values()) {
            states.put(key, Boolean.FALSE);
        }
        this.modified = new MediatorLiveData<>();
        this.modified.setValue(Boolean.FALSE);
        this.modified.addSource(vm.getFirstName(), newValue -> modificationUpdater (Field.FIRSTNAME) );
        this.modified.addSource(vm.getLastName(), newValue -> modificationUpdater (Field.LASTNAME) );
        this.modified.addSource(vm.getPhone(), newValue -> modificationUpdater (Field.PHONE) );
        this.modified.addSource(vm.getGender(), newValue -> modificationUpdater (Field.GENDER) );
        this.modified.addSource(vm.getHeight(), newValue -> modificationUpdater (Field.HEIGHT) );
        this.modified.addSource(vm.getWeight(), newValue -> modificationUpdater (Field.WEIGHT) );
        this.modified.addSource(vm.getHighRes(), newValue -> modificationUpdater (Field.HIGHRES) );
        this.modified.addSource(vm.getLowRes(), newValue -> modificationUpdater (Field.LOWRES) );
        this.modified.addSource(vm.getAvatar(), newValue -> modificationUpdater (Field.AVATAR) );
        this.modified.addSource(vm.getSkills(), newValue -> modificationUpdater (Field.SKILLS) );
    }

    public LiveData<Boolean> getModified() {
        return modified;
    }

    public void skillItemUpdate(int position) {
        modificationUpdater (Field.SKILLS);
    }

    private boolean checkStates (boolean lastUpdate) {
        if (! lastUpdate) return false;
        for (Field key : Field.values()) {
            if (! states.get(key)) return false;
        }
        return true;
    }

    private void modificationUpdater (Field key) {
        boolean value = false;
        switch (key) {
        case FIRSTNAME: value = compareFirstNames(); break;
        case LASTNAME: value = compareLastNames(); break;
        case PHONE: value = comparePhones(); break;
        case GENDER: value = compareGenders(); break;
        case HEIGHT: value = compareHeights(); break;
        case WEIGHT: value = compareWeights(); break;
        case HIGHRES: value = compareHightRes(); break;
        case LOWRES: value = compareLowRes(); break;
        case AVATAR: value = compareAvatar(); break;
        case SKILLS: value = compareSkills(); break;
        }
        states.put(key, value);
        modified.postValue(! checkStates(value));
    }

    private boolean compareFirstNames () {
        if (vm.getFirstName().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getPerson().getValue().getFirstName() == null;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getFirstName().getValue().equals(vm.getPerson().getValue().getFirstName());
        }
    }

    private boolean compareLastNames () {
        if (vm.getLastName().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getPerson().getValue().getLastName() == null;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getLastName().getValue().equals(vm.getPerson().getValue().getLastName());
        }
    }

    private boolean comparePhones () {
        if (vm.getPhone().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getPerson().getValue().getPhone() == null;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getPhone().getValue().equals(vm.getPerson().getValue().getPhone());
        }
    }

    private boolean compareGenders () {
        if (vm.getGender().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getPerson().getValue().getGender() == null;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getGender().getValue().ordinal() == vm.getPerson().getValue().getGender().ordinal();
        }
    }

    private boolean compareHeights () {
        if (vm.getHeight().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return false;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getHeight().getValue() == vm.getPerson().getValue().getHeight();
        }
    }

    private boolean compareWeights () {
        if (vm.getWeight().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return false;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getWeight().getValue() == vm.getPerson().getValue().getWeight();
        }
    }

    private boolean compareHightRes () {
        if (vm.getHighRes().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getPerson().getValue().getHighRes() == null;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getHighRes().getValue().equals(vm.getPerson().getValue().getHighRes());
        }
    }

    private boolean compareLowRes () {
        if (vm.getLowRes().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getPerson().getValue().getLowRes() == null;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getLowRes().getValue().equals(vm.getPerson().getValue().getLowRes());
        }
    }

    private boolean compareAvatar () {
        if (vm.getAvatar().getValue() == null) {
            if (vm.getPerson().getValue() == null) return false;
            return false;
        } else {
            if (vm.getPerson().getValue() == null) return false;
            return vm.getAvatar().getValue() == vm.getPerson().getValue().getAvatar();
        }
    }

    private boolean compareSkills () {
        if (vm.getSkills().getValue() == null) {
            if (vm.getSkills().getValue() == null) return false;
            return vm.getFullPerson().getValue().skills == null;
        } else {
            if (vm.getSkills().getValue() == null) return false;
            List<SkillPersonAssociation> now = vm.getSkills().getValue();
            List<SkillPersonAssociation> initial = vm.getFullPerson().getValue().skills;
            if (initial == null) return true;
            if (initial.size() != now.size()) return false;
            Map<Long, SkillPersonAssociation> lhs = new TreeMap<>();
            for (SkillPersonAssociation s : initial) {
                lhs.put(s.getSid(), s);
            }
            Map<Long, SkillPersonAssociation> rhs = new TreeMap<>();
            for (SkillPersonAssociation s : now) {
                rhs.put(s.getSid(), s);
            }
            if (! lhs.keySet().containsAll(rhs.keySet())) return false;
            if (! rhs.keySet().containsAll(lhs.keySet())) return false;
            for (SkillPersonAssociation next : now) {
                SkillPersonAssociation previous = lhs.get(next.getSid());
                if (!SkillPersonAssociation.compare(previous, next)) return false;
            }
            return true;
        }
    }
}
