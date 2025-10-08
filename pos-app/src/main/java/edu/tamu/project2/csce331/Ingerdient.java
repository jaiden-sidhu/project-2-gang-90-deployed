package edu.tamu.project2.csce331;

public class Ingerdient {
    private String ingredient_name;
    private int quantity;
    private String category;
    private int ingredient_id;
    public Ingerdient(String ingredient_name, int quantity, String category){
        this.ingredient_name = ingredient_name;
        this.quantity = quantity;
        this.category = category;
    }

    public Ingerdient(String ingredient_name, int quantity, String category, int ingredient_id){
        this.ingredient_name = ingredient_name;
        this.quantity = quantity;
        this.category = category;
        this.ingredient_id = ingredient_id;
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
    public int get_ingredient_id() {
        return ingredient_id;
    }

    public void set_ingredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }
}
