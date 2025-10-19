package edu.tamu.project2.csce331;

public class IngredientUsage {
    public String name;
    public Integer amount;
    IngredientUsage(String name, Integer amount){
        this.name = name;
        this.amount = amount;
    }

     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}