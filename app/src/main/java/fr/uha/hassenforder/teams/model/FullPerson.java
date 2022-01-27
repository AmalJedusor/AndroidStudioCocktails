package fr.uha.hassenforder.teams.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FullPerson {

    @Embedded
    public Person person;
    @Relation(
            parentColumn = "pid",
            entityColumn = "pid"
    )
    public List<SkillPersonAssociation> skills;

}
