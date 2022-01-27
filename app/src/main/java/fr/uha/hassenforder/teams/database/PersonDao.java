package fr.uha.hassenforder.teams.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import fr.uha.hassenforder.teams.model.FullPerson;
import fr.uha.hassenforder.teams.model.Person;
import fr.uha.hassenforder.teams.model.PersonWithDetails;
import fr.uha.hassenforder.teams.model.SkillPersonAssociation;

@Dao
public interface PersonDao {

    @Transaction
    @Query("SELECT * "
            + ", (SELECT COUNT(*) FROM teams T WHERE T.leaderId = P.pid) AS teamLeadingCount"
            + ", (SELECT COUNT(*) FROM tpas TPA WHERE TPA.pid = P.pid) AS teamMemberCount"
            + " FROM persons AS P")
    LiveData<List<PersonWithDetails>> getAll ();

    @Transaction
    @Query("SELECT * FROM persons WHERE pid = :id")
    LiveData<FullPerson> getFullById (long id);

    @Query("SELECT * FROM persons WHERE pid = :id")
    LiveData<Person> getPersonById (long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert (Person person);

    @Delete
    void delete (Person person);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSkill(SkillPersonAssociation skill);

    @Delete
    void removeSkill(SkillPersonAssociation skill);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSkills(List<SkillPersonAssociation> skill);

    @Delete
    void removeSkills(List<SkillPersonAssociation> skill);
}
