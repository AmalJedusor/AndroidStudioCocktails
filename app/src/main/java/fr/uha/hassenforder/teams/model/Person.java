package fr.uha.hassenforder.teams.model;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "persons")
public class Person {

    @PrimaryKey (autoGenerate = true)
    private long pid;
    private String firstName;
    private String lastName;
    private String phone;
    private Gender gender;
    private String highRes;
    private Bitmap lowRes;
    private int avatar;
    private int height;
    private int weight;

    public Person() {
        this.pid = 0;
    }

    @Ignore
    public Person(long pid, String firstName, String lastName, String phone, Gender gender, String highRes, Bitmap lowRes, int avatar, int height, int weight) {
        this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gender = gender;
        this.highRes = highRes;
        this.lowRes = lowRes;
        this.avatar = avatar;
        this.height = height;
        this.weight = weight;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getHighRes() {
        return highRes;
    }

    public void setHighRes(String highRes) {
        this.highRes = highRes;
    }

    public Bitmap getLowRes() {
        return lowRes;
    }

    public void setLowRes(Bitmap lowRes) {
        this.lowRes = lowRes;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        tmp.append("@");
        tmp.append(pid);
        tmp.append(" ");
        tmp.append(firstName);
        tmp.append(".");
        tmp.append(lastName);
        tmp.append(" #");
        tmp.append(phone);
        tmp.append(" ");
        tmp.append(gender != null ? gender.name() : "null");
        tmp.append(" ");
        tmp.append(highRes != null ? "highRes" : "no highRes");
        tmp.append(" ");
        tmp.append(lowRes != null ? "lowRes" : "no lowRes");
        tmp.append(" ");
        tmp.append(avatar != 0 ? "avatar" : "no avatar");
        tmp.append(" ");
        tmp.append(height);
        tmp.append(" * ");
        tmp.append(weight);
        return tmp.toString();
    }

    public static boolean compare(Person newPerson, Person oldPerson) {
        if (newPerson.getPid() != oldPerson.getPid()) return false;
        if (! CompareUtil.compare(newPerson.getFirstName(), oldPerson.getFirstName())) return false;
        if (! CompareUtil.compare(newPerson.getLastName(), oldPerson.getLastName())) return false;
        if (! CompareUtil.compare(newPerson.getPhone(), oldPerson.getPhone())) return false;
        if (! CompareUtil.compare(newPerson.getGender(), oldPerson.getGender())) return false;
        if (! CompareUtil.compare(newPerson.getHeight(), oldPerson.getHeight())) return false;
        if (! CompareUtil.compare(newPerson.getWeight(), oldPerson.getWeight())) return false;
        if (! CompareUtil.compare(newPerson.getHighRes(), oldPerson.getHighRes())) return false;
        if (! CompareUtil.compare(newPerson.getLowRes(), oldPerson.getLowRes())) return false;
        if (! CompareUtil.compare(newPerson.getAvatar(), oldPerson.getAvatar())) return false;
        return true;
    }
}
