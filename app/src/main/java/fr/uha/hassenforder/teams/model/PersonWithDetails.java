package fr.uha.hassenforder.teams.model;

import androidx.room.Embedded;

public class PersonWithDetails {

    @Embedded
    public Person person;
    public int teamLeadingCount;
    public int teamMemberCount;

    public static boolean compare(PersonWithDetails newPerson, PersonWithDetails oldPerson) {
        if (! Person.compare (newPerson.person, oldPerson.person)) return false;
        if (! CompareUtil.compare(newPerson.teamLeadingCount, oldPerson.teamLeadingCount)) return false;
        if (! CompareUtil.compare(newPerson.teamMemberCount, oldPerson.teamMemberCount)) return false;
        return true;
    }

}
