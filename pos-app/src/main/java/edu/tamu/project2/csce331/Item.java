package edu.tamu.project2.csce331;

import java.util.ArrayList;

public class Item {
  private int id;
  private String name;
  private int popularity;
  private double price;
  private ArrayList<Integer> ingredients;

  // Constructor
  public Item(int id, String name, int popularity, double price, ArrayList<Integer> ingredients)
      throws IllegalArgumentException {
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
    this(id, name, popularity, price, new ArrayList<>());
  }

  public Item(int id, String name, double price) throws IllegalArgumentException {
    this(id, name, 0, price, new ArrayList<>());
  }

  // Getters
  public int get_id() {
    return id;
  }

  public String get_name() {
    return name;
  }

  public int get_popularity() {
    return popularity;
  }

  public double get_price() {
    return price;
  }

  public ArrayList<String> get_ingredients() {
    return ingredients;
  }

  // Setters
  public void set_id(int id) throws IllegalArgumentException {
    if (id >= 0) {
      this.id = id;
    } else {
      throw new IllegalArgumentException("ID cannot be negative.");
    }
  }

  public void set_name(String name) {
    this.name = name;
  }

  public void set_popularity(int popularity) throws IllegalArgumentException {
    if (popularity >= 0) {
      this.popularity = popularity;
    } else {
      throw new IllegalArgumentException("Popularity cannot be negative.");
    }
  }

  public void set_price(double price) throws IllegalArgumentException {
    if (price >= 0) {
      this.price = price;
    } else {
      throw new IllegalArgumentException("Price cannot be negative.");
    }
  }

  public void set_ingredients(ArrayList<Integer> ingredients) throws IllegalArgumentException {
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
    this.ingredients.add(ingredient);
  }

  public void add_ingredients(ArrayList<String> new_ingredients) throws IllegalArgumentException {
    if (new_ingredients == null) {
      throw new IllegalArgumentException("New ingredients cannot be null.");
    }
    this.ingredients.addAll(new_ingredients);
  }
}
