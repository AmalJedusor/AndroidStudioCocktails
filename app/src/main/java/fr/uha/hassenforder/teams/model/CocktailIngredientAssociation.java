package fr.uha.hassenforder.teams.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
        tableName = "steps",
        primaryKeys = { "cid", "ingid" },
        indices = { @Index("cid"), @Index("ingid") }
)
public class CocktailIngredientAssociation {

    public long cid;
    public long ingid;
    public String step;
    public long quantity;
    public QuantityType quantityType;
    public CocktailIngredientAssociation() {

    }

    @Ignore
    public CocktailIngredientAssociation(long cid, long ingid) {
        this.cid = cid;
        this.ingid = ingid;
    }
}
