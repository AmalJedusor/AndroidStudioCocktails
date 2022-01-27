package fr.uha.hassenforder.teams.ui.person;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.hassenforder.teams.database.PersonDao;
import fr.uha.hassenforder.teams.model.Person;
import fr.uha.hassenforder.teams.model.PersonWithDetails;

public class PersonListViewModel extends ViewModel {

    private PersonDao personDao;
    private MediatorLiveData<List<PersonWithDetails>> persons;

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
        this.persons = new MediatorLiveData<>();
        this.persons.addSource(personDao.getAll(), persons::setValue);
    }

    public LiveData<List<PersonWithDetails>> getPersons() {
        return persons;
    }

    public void deletePerson(Person person) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                personDao.delete(person);
            }
        });
    }

}