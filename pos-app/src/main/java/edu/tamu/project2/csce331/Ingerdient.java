package edu.tamu.project2.csce331;

public class Ingerdient {
    private String ingredient_name;
    private int quantity;
    private String category;
    
    public Ingerdient(String ingredient_name, int quantity, String category){
        this.ingredient_name = ingredient_name;
        this.quantity = quantity;
        this.category = category;
    }
    public String get_ingredient_name() {
        return ingredient_name;
    }

    public void set_ingredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    
    public int get_quantity() {
        return quantity;
    }

    public void set_quantity(int quantity) {
        this.quantity = quantity;
    }

    
    public String get_category() {
        return category;
    }

    public void set_category(String category) {
        this.category = category;
    }
}
