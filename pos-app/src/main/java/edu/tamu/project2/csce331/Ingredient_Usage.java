package edu.tamu.project2.csce331;

public class Ingredient_Usage {
    public String name;
    public Integer amount;
    Ingredient_Usage(String name, Integer amount){
        this.name = name;
        this.amount = amount;
    }

     public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public Integer get_amount() {
        return amount;
    }

    public void set_amount(Integer amount) {
        this.amount = amount;
    }

}