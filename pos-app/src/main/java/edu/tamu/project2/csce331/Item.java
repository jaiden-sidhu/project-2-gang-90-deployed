package edu.tamu.project2.csce331;

import java.util.Arrays;

public class Item {
    private int id;
    private String name;
    private int popularity;
    private double price;
    private String[] ingredients;

    // Constructor
    public Item(int id, String name, int popularity, double price, String[] ingredients) throws IllegalArgumentException {
        // Quick luh checks
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative.");
        }
        if (popularity < 0) {
            throw new IllegalArgumentException("Popularity cannot be negative.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (ingredients == null) {
            throw new IllegalArgumentException("Ingredients cannot be null.");
        }
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.price = price;
        this.ingredients = ingredients;
    }
    public Item(int id, String name, int popularity, double price) throws IllegalArgumentException {
        this(id, name, popularity, price, new String[]{});
    }

    public Item(int id, String name, double price) throws IllegalArgumentException {
        this(id, name, 0, price, new String[]{});
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPopularity() {
        return popularity;
    }

    public double getPrice() {
        return price;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    //Setters
    public void setId(int id) throws IllegalArgumentException {
        if (id >= 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID cannot be negative.");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopularity(int popularity) throws IllegalArgumentException {
        if (popularity >= 0) {
            this.popularity = popularity;
        } else {
            throw new IllegalArgumentException("Popularity cannot be negative.");
        }
    }

    public void setPrice(double price) throws IllegalArgumentException {
        if (price >= 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }

    public void setIngredients(String[] ingredients) throws IllegalArgumentException {
        if (ingredients != null) {
            this.ingredients = ingredients;
        } else {
            throw new IllegalArgumentException("Ingredients cannot be null.");
        }
    }

    public void add_ingredient(String ingredient) throws IllegalArgumentException {
        if (ingredient == null || ingredient.isEmpty()) {
            throw new IllegalArgumentException("Ingredient cannot be null or empty.");
        }
        String[] new_ingredients = Arrays.copyOf(ingredients, ingredients.length + 1);
        new_ingredients[new_ingredients.length - 1] = ingredient;
        this.ingredients = new_ingredients;
    }

    public void add_ingredients(String[] new_ingredients) throws IllegalArgumentException {
        if (new_ingredients == null) {
            throw new IllegalArgumentException("New ingredients cannot be null.");
        }
        String[] combined_ingredients = Arrays.copyOf(ingredients, ingredients.length + new_ingredients.length);
        System.arraycopy(new_ingredients, 0, combined_ingredients, ingredients.length, new_ingredients.length);
        this.ingredients = combined_ingredients;
    }

}
