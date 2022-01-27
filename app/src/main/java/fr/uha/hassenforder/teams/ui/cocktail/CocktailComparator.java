package fr.uha.hassenforder.teams.ui.cocktail;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.uha.hassenforder.teams.model.SkillPersonAssociation;

public class CocktailComparator {

    private CocktailViewModel vm;
    private MediatorLiveData<Boolean> modified;

    private enum Field {
        COCKTAILNAME,
        COCKTAILPIC,
        ALCOHOL,
        HIGHRES,
        LOWRES,
        AVATAR,
        HEIGHT,
    }

    private EnumMap<Field, Boolean> states;

    public CocktailComparator(CocktailViewModel vm) {
        this.vm = vm;
        states = new EnumMap<Field, Boolean>(Field.class);
        for (Field key : Field.values()) {
            states.put(key, Boolean.FALSE);
        }
        this.modified = new MediatorLiveData<>();
        this.modified.setValue(Boolean.FALSE);
        this.modified.addSource(vm.getCocktailName(), newValue -> modificationUpdater (Field.COCKTAILNAME) );
        this.modified.addSource(vm.getCocktailPic(), newValue -> modificationUpdater (Field.COCKTAILPIC) );
        this.modified.addSource(vm.getAlcohol(), newValue -> modificationUpdater (Field.ALCOHOL) );

        this.modified.addSource(vm.getHeight(), newValue -> modificationUpdater (Field.HEIGHT) );

        this.modified.addSource(vm.getHighRes(), newValue -> modificationUpdater (Field.HIGHRES) );
        this.modified.addSource(vm.getLowRes(), newValue -> modificationUpdater (Field.LOWRES) );
        this.modified.addSource(vm.getAvatar(), newValue -> modificationUpdater (Field.AVATAR) );

    }

    public LiveData<Boolean> getModified() {
        return modified;
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

        case COCKTAILNAME: value = compareFirstNames(); break;
        case COCKTAILPIC: value =comparePics(); break;
        case ALCOHOL: value = compareAlcohol(); break;

        case HEIGHT: value = compareHeights(); break;

        case HIGHRES: value = compareHightRes(); break;
        case LOWRES: value = compareLowRes(); break;
        case AVATAR: value = compareAvatar(); break;

        }
        states.put(key, value);
        modified.postValue(! checkStates(value));
    }

    private boolean compareFirstNames () {
        if (vm.getCocktailName().getValue() == null) {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getCocktail().getValue().getCocktailName() == null;
        } else {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getCocktailName().getValue().equals(vm.getCocktail().getValue().getCocktailName());
        }
    }

    private boolean comparePics () {
        if (vm.getCocktailPic().getValue() == null) {
            if (vm.getCocktailPic().getValue() == null) return false;
            return vm.getCocktail().getValue().getCocktailPic() == null;
        } else {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getCocktailPic().getValue().equals(vm.getCocktail().getValue().getCocktailPic());
        }
    }

    private boolean compareAlcohol () {
        if (vm.getAlcohol().getValue() == null) {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getCocktail().getValue().getAlcohol() == null;
        } else {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getAlcohol().getValue().equals(vm.getCocktail().getValue().getAlcohol());
        }
    }


    private boolean compareHeights () {
        if (vm.getHeight().getValue() == null) {
            if (vm.getCocktail().getValue() == null) return false;
            return false;
        } else {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getHeight().getValue() == vm.getCocktail().getValue().getHeight();
        }
    }



    private boolean compareHightRes () {
        if (vm.getHighRes().getValue() == null) {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getCocktail().getValue().getHighRes() == null;
        } else {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getHighRes().getValue().equals(vm.getCocktail().getValue().getHighRes());
        }
    }

    private boolean compareLowRes () {
        if (vm.getLowRes().getValue() == null) {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getCocktail().getValue().getLowRes() == null;
        } else {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getLowRes().getValue().equals(vm.getCocktail().getValue().getLowRes());
        }
    }

    private boolean compareAvatar () {
        if (vm.getAvatar().getValue() == null) {
            if (vm.getCocktail().getValue() == null) return false;
            return false;
        } else {
            if (vm.getCocktail().getValue() == null) return false;
            return vm.getAvatar().getValue() == vm.getCocktail().getValue().getAvatar();
        }
    }


    }

