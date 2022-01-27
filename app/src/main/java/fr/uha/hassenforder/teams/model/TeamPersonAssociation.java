package fr.uha.hassenforder.teams.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
        tableName = "tpas",
        primaryKeys = { "tid", "pid" },
        indices = { @Index("tid"), @Index("pid") }
)
public class TeamPersonAssociation {

    public long tid;
    public long pid;

    public TeamPersonAssociation() {

    }

    @Ignore
    public TeamPersonAssociation(long tid, long pid) {
        this.tid = tid;
        this.pid = pid;
    }

}
