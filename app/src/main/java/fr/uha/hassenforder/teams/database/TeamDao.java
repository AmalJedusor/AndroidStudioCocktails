package fr.uha.hassenforder.teams.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import fr.uha.hassenforder.teams.model.FullTeam;
import fr.uha.hassenforder.teams.model.Team;
import fr.uha.hassenforder.teams.model.TeamPersonAssociation;

@Dao
public interface TeamDao {

    @Query("SELECT * FROM teams")
    public LiveData<List<Team>> getAll ();

    @Transaction
    @Query("SELECT * FROM teams WHERE tid = :id")
    public LiveData<FullTeam> getById (long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long upsert (Team team);

    @Delete
    public void delete (Team team);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTeamPerson(TeamPersonAssociation member);

    @Delete
    void removeTeamPerson(TeamPersonAssociation member);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTeamPerson(List<TeamPersonAssociation> members);

    @Delete
    void removeTeamPerson(List<TeamPersonAssociation> members);

}
