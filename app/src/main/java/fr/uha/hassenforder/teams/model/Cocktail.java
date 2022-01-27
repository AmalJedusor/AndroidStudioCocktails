package fr.uha.hassenforder.teams.model;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cocktails")
public class Cocktail {
    @PrimaryKey(autoGenerate = true)
    private long cid;

    private String cocktailName;
    private String cocktailPic;
    private Boolean alcohol;
    private String highRes;
    private Bitmap lowRes;
    private int avatar;
    private int height;

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

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private int weight;
    public Cocktail() {
        this.cid = 0;
    }

    @Ignore
    public Cocktail(long pid, String cocktailName, String cocktailPic, Boolean alcohol) {
        this.cid = pid;
        this.cocktailName = cocktailName;
        this.cocktailPic = cocktailPic;
        this.alcohol = alcohol;

    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public void setCocktailName(String cocktailName) {
        this.cocktailName = cocktailName;
    }

    public String getCocktailPic() {
        return cocktailPic;
    }

    public void setCocktailPic(String cocktailPic) {
        this.cocktailPic = cocktailPic;
    }

    public Boolean getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Boolean alcohol) {
        this.alcohol = alcohol;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        tmp.append("< @");
        tmp.append(cid);
        tmp.append(" ");
        tmp.append(cocktailName);
        tmp.append(".");
        tmp.append(cocktailPic);
        tmp.append(" alcohol:");
        tmp.append(alcohol);
        tmp.append(" >");

        return tmp.toString();
    }
    public static boolean compare(Cocktail newCocktail, Cocktail oldCocktail) {
        if (newCocktail.getCid() != oldCocktail.getCid()) return false;
        if (! CompareUtil.compare(newCocktail.getCocktailName(), oldCocktail.getCocktailName())) return false;
        if (! CompareUtil.compare(newCocktail.getCocktailPic(), oldCocktail.getCocktailPic())) return false;
        if (! CompareUtil.compare(newCocktail.getAlcohol(), oldCocktail.getAlcohol())) return false;
        return true;
    }
}
