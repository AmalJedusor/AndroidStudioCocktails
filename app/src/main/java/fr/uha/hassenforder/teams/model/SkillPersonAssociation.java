package fr.uha.hassenforder.teams.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "spas",
        indices = { @Index("pid") }
)
public class SkillPersonAssociation implements Cloneable {

    @PrimaryKey(autoGenerate = true)
    private long sid;
    private long pid;
    private Skill skill;
    private float level;

    public SkillPersonAssociation() {
        this.sid = 0;
    }

    @Ignore
    public SkillPersonAssociation(long sid, long pid, Skill skill, float level) {
        this.sid = sid;
        this.pid = pid;
        this.skill = skill;
        this.level = level;
    }

    public SkillPersonAssociation clone () {
        try {
            return (SkillPersonAssociation) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public static boolean compare(SkillPersonAssociation newAssociation, SkillPersonAssociation oldAssociation) {
        if (newAssociation.getSid() != oldAssociation.getSid()) return false;
        if (newAssociation.getPid() != oldAssociation.getPid()) return false;
        if (! CompareUtil.compare(newAssociation.getSkill(), oldAssociation.getSkill())) return false;
        if (! CompareUtil.compare(newAssociation.getLevel(), oldAssociation.getLevel())) return false;
        return true;
    }
}
