package fr.uha.hassenforder.teams.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "teams")
public class Team {
    @PrimaryKey(autoGenerate = true)
    private long tid;
    private String name;
    private String objective;

    private Date startDay;
    private int duration;
    private int min;
    private int max;

    private long leaderId;

    public Team() {
        this.tid = 0;
    }

    @Ignore
    public Team(long tid, String name, String objective, Date startDay, int duration, int min, int max, long leaderId) {
        this.tid = tid;
        this.name = name;
        this.objective = objective;
        this.startDay = startDay;
        this.duration = duration;
        this.min = min;
        this.max = max;
        this.leaderId = leaderId;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(long leaderId) {
        this.leaderId = leaderId;
    }

    public static boolean compare(Team newTeam, Team oldTeam) {
        if (newTeam.getTid() != oldTeam.getTid()) return false;
        if (! CompareUtil.compare(newTeam.getName(), oldTeam.getName())) return false;
        if (! CompareUtil.compare(newTeam.getObjective(), oldTeam.getObjective())) return false;
        if (! CompareUtil.compare(newTeam.getStartDay(), oldTeam.getStartDay())) return false;
        if (! CompareUtil.compare(newTeam.getDuration(), oldTeam.getDuration())) return false;
        if (! CompareUtil.compare(newTeam.getMin(), oldTeam.getMin())) return false;
        if (! CompareUtil.compare(newTeam.getMax(), oldTeam.getMax())) return false;
        if (! CompareUtil.compare(newTeam.getLeaderId(), oldTeam.getLeaderId())) return false;
        return true;
    }
}
