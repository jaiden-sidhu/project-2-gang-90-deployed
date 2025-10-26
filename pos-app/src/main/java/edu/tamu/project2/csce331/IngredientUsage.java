package edu.tamu.project2.csce331;

/**
 * Represents the usage of an ingredient with its name and the amount used.
 * Contains the ingredient name and the amount required or used.
 * 
 * @author Daniel Zhang
 * @author Brendan Larson
 */
public class IngredientUsage {
    /**
     * this is the name of the ingreits
     */
    public String name;
    /**
     * This Intetger repersents how much of the ingredits was used
     */
    public Integer amount;

    /**
     * Constructs an IngredientUsage object with the specified name and amount.
     * @param name the name of the ingredient
     * @param amount the amount of the ingredient used
     */
    IngredientUsage(String name, Integer amount){
        this.name = name;
        this.amount = amount;
    }

    /**
     * Gets the name of the ingredient.
     * @return the ingredient name
     */
     public String getName() {
        return name;
    }

    /**
     * Sets the name of the ingredient.
     * @param name the ingredient name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the amount of the ingredient used.
     * @return the amount of the ingredient
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the ingredient used.
     * @param amount the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}