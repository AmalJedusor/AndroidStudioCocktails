package fr.uha.hassenforder.teams.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class Savable {

    private LiveData<Boolean> modified;
    private LiveData<Boolean> validated;

    private MediatorLiveData<Boolean> savable;

    public Savable(LiveData<Boolean> modified, LiveData<Boolean> validated) {
        this.modified = modified;
        this.validated = validated;

        this.savable = new MediatorLiveData<>();
        this.savable.setValue(Boolean.FALSE);
        if (modified != null) {
            this.savable.addSource(modified, savableObserver);
        }
        if (validated != null) {
            this.savable.addSource(validated, savableObserver);
        }
    }

    public LiveData<Boolean> getSavable() {
        return savable;
    }

    private Observer<Boolean> savableObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean o) {
            boolean valid = true;
            if (modified != null) {
                valid = valid && modified.getValue() != null && modified.getValue();
            }
            if (validated != null) {
                valid = valid && validated.getValue() != null && validated.getValue();
            }
            savable.postValue(valid);
        }
    };

}
