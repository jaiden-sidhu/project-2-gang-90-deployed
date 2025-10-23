package edu.tamu.project2.csce331;

import java.util.ArrayList;


/**
 * Represents an item in the POS system, such as a menu item or product.
 * <p>
 * Each item has the following properties:
 * <ul>
 *   <li>id: Unique identifier for the item.</li>
 *   <li>name: Name of the item.</li>
 *   <li>popularity: Popularity score of the item.</li>
 *   <li>price: Price of the item.</li>
 *   <li>state: Availability status of the item (active/inactive).</li>
 *   <li>ingredients: List of ingredient IDs associated with the item.</li>
 * </ul>
 * 
 * @author Daniel Zhang
 */
public class Item {
  private int id;
  private String name;
  private int popularity;
  private double price;
  private boolean state;
  private ArrayList<Integer> ingredients;

  /**
   * Constructs a new Item with the given id, name, popularity, price, and ingredients.
   * The item state is set to active by default.
   * @param id Unique identifier for the item. Must be non-negative.
   * @param name Name of the item.
   * @param popularity Popularity score. Must be non-negative.
   * @param price Price of the item. Must be non-negative.
   * @param ingredients List of ingredient IDs. Cannot be null.
   * @throws IllegalArgumentException if id, popularity, or price is negative, or if ingredients is null.
   */
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
    this.state = true;
  }

  /**
   * Constructs a new Item with the given id, name, popularity, price, ingredients, and state.
   * @param id Unique identifier for the item. Must be non-negative.
   * @param name Name of the item.
   * @param popularity Popularity score. Must be non-negative.
   * @param price Price of the item. Must be non-negative.
   * @param ingredients List of ingredient IDs. Cannot be null.
   * @param is_active Availability status of the item.
   * @throws IllegalArgumentException if id, popularity, or price is negative, or if ingredients is null.
   */
  public Item(int id, String name, int popularity, double price, ArrayList<Integer> ingredients, boolean is_active)
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
    this.state = is_active;
  }

  /**
   * Constructs a new Item with the given id, name, popularity, and price.
   * The ingredients list is initialized as empty.
   * @param id Unique identifier for the item. Must be non-negative.
   * @param name Name of the item.
   * @param popularity Popularity score. Must be non-negative.
   * @param price Price of the item. Must be non-negative.
   * @throws IllegalArgumentException if id, popularity, or price is negative.
   */
  public Item(int id, String name, int popularity, double price) throws IllegalArgumentException {
    this(id, name, popularity, price, new ArrayList<>());
  }

  /**
   * Constructs a new Item with the given id, name, and price.
   * Popularity is set to 0. The ingredients list is initialized as empty.
   * @param id Unique identifier for the item. Must be non-negative.
   * @param name Name of the item.
   * @param price Price of the item. Must be non-negative.
   * @throws IllegalArgumentException if id or price is negative.
   */
  public Item(int id, String name, double price) throws IllegalArgumentException {
    this(id, name, 0, price, new ArrayList<>());
  }

  // Getters

  /**
   * Gets the unique identifier for the item.
   * @return The item ID.
   */
  public int get_id() {
    return id;
  }

  /**
   * Gets the name of the item.
   * @return The item name.
   */
  public String get_name() {
    return name;
  }

  /**
   * Gets the popularity score of the item.
   * @return The popularity score.
   */
  public int get_popularity() {
    return popularity;
  }

  /**
   * Gets the price of the item.
   * @return The item price.
   */
  public double get_price() {
    return price;
  }

  /**
   * Gets the availability status of the item.
   * @return True if the item is active, false otherwise.
   */
  public boolean get_status() {
    return state;
  }

  /**
   * Gets the list of ingredient IDs associated with the item.
   * @return The list of ingredient IDs.
   */
  public ArrayList<Integer> get_ingredients() {
    return ingredients;
  }

  // Setters

  /**
   * Sets the unique identifier for the item.
   * @param id The new item ID. Must be non-negative.
   * @throws IllegalArgumentException if id is negative.
   */
  public void set_id(int id) throws IllegalArgumentException {
    if (id >= 0) {
      this.id = id;
    } else {
      throw new IllegalArgumentException("ID cannot be negative.");
    }
  }

  /**
   * Sets the name of the item.
   * @param name The new item name.
   */
  public void set_name(String name) {
    this.name = name;
  }

  /**
   * Sets the popularity score of the item.
   * @param popularity The new popularity score. Must be non-negative.
   * @throws IllegalArgumentException if popularity is negative.
   */
  public void set_popularity(int popularity) throws IllegalArgumentException {
    if (popularity >= 0) {
      this.popularity = popularity;
    } else {
      throw new IllegalArgumentException("Popularity cannot be negative.");
    }
  }

  /**
   * Sets the price of the item.
   * @param price The new item price. Must be non-negative.
   * @throws IllegalArgumentException if price is negative.
   */
  public void set_price(double price) throws IllegalArgumentException {
    if (price >= 0) {
      this.price = price;
    } else {
      throw new IllegalArgumentException("Price cannot be negative.");
    }
  }

  /**
   * Sets the list of ingredient IDs associated with the item.
   * @param ingredients The new list of ingredient IDs. Cannot be null.
   * @throws IllegalArgumentException if ingredients is null.
   */
  public void set_ingredients(ArrayList<Integer> ingredients) throws IllegalArgumentException {
    if (ingredients != null) {
      this.ingredients = ingredients;
    } else {
      throw new IllegalArgumentException("Ingredients cannot be null.");
    }
  }

  /**
   * Adds an ingredient ID to the item's ingredient list.
   * @param ingredient_id The ingredient ID to add. Must be non-negative.
   * @throws IllegalArgumentException if ingredient_id is negative.
   */
  public void add_ingredient(int ingredient_id) throws IllegalArgumentException {
    if (ingredient_id < 0) {
      throw new IllegalArgumentException("Ingredient ID cannot be negative.");
    }
    this.ingredients.add(ingredient_id);
  }

  /**
   * Adds a list of ingredient IDs to the item's ingredient list.
   * @param new_ingredients The list of new ingredient IDs to add. Cannot be null.
   * @throws IllegalArgumentException if new_ingredients is null.
   */
  public void add_ingredients(ArrayList<Integer> new_ingredients) throws IllegalArgumentException {
    if (new_ingredients == null) {
      throw new IllegalArgumentException("New ingredients cannot be null.");
    }
    this.ingredients.addAll(new_ingredients);
  }
}
