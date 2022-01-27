package fr.uha.hassenforder.teams.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class FullTeam {

    @Embedded
    public Team team;
    @Relation(parentColumn = "leaderId", entityColumn = "pid")
    public Person leader;
    @Relation(
            parentColumn = "tid",
            entityColumn = "pid",
            associateBy = @Junction(TeamPersonAssociation.class)
    )
    public List<Person> members;

}
