package edu.tamu.project2.csce331;

/**
 * Represents an ingredient with a name, quantity, category, and an optional unique identifier.
 * This class provides methods to access and modify the ingredient's properties.
 * 
 * @author Daniel Zhang
 * @author Brendan Larson
 * @author Michael Ramirez
 */
public class Ingredient {
  private String ingredient_name;
  private int quantity;
  private String category;
  private int ingredient_id;

  /**
   * Constructs an Ingredient with the specified name, quantity, and category.
   * @param ingredient_name the name of the ingredient
   * @param quantity the quantity of the ingredient
   * @param category the category of the ingredient
   */
  public Ingredient(String ingredient_name, int quantity, String category) {
    this.ingredient_name = ingredient_name;
    this.quantity = quantity;
    this.category = category;
  }

  /**
   * Constructs an Ingredient with the specified name, quantity, category, and ingredient ID.
   * @param ingredient_name the name of the ingredient
   * @param quantity the quantity of the ingredient
   * @param category the category of the ingredient
   * @param ingredient_id the unique identifier of the ingredient
   */
  public Ingredient(String ingredient_name, int quantity, String category, int ingredient_id ) {
    this.ingredient_name = ingredient_name;
    this.quantity = quantity;
    this.category = category;
    this.ingredient_id = ingredient_id;
  }

  /**
   * Returns the name of the ingredient.
   * @return the ingredient's name
   */
  public String get_ingredient_name() {
    return ingredient_name;
  }

  /**
   * Sets the name of the ingredient.
   * @param ingredient_name the new name of the ingredient
   */
  public void set_ingredient_name(String ingredient_name) {
    this.ingredient_name = ingredient_name;
  }

  /**
   * Returns the quantity of the ingredient.
   * @return the ingredient's quantity
   */
  public int get_quantity() {
    return quantity;
  }

  /**
   * Sets the quantity of the ingredient.
   * @param quantity the new quantity of the ingredient
   */
  public void set_quantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * Returns the unique identifier of the ingredient.
   * @return the ingredient's ID
   */
  public int get_ingredient_id() {
    return ingredient_id;
  }

  /**
   * Sets the unique identifier of the ingredient.
   * @param ingredient_id the new ID of the ingredient
   */
  public void set_ingredient_id(int ingredient_id) {
    this.ingredient_id = ingredient_id;
  }

  /**
   * Returns the category of the ingredient.
   * @return the ingredient's category
   */
  public String get_category() {
    return category;
  }

  /**
   * Sets the category of the ingredient.
   * @param category the new category of the ingredient
   */
  public void set_category(String category) {
    this.category = category;
  }
}
