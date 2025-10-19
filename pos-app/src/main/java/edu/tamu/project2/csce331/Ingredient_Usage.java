package edu.tamu.project2.csce331;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of ingredient usage counts, preserving insertion order. Provides
 * normalized access and convenient merging logic.
 */
public final class Ingredient_Usage {
  private final Map<String, Integer> counts = new LinkedHashMap<>();

  /**
   * Adds or updates the usage count for a given ingredient. Ingredient names are normalized
   * (trimmed + lowercased).
   */
  public void put(String ingredient_name, int count) {
    String normalized = normalize(ingredient_name);
    counts.merge(normalized, count, Integer::sum);
  }

  /** Returns the usage count for a given ingredient (0 if absent). */
  public int get(String ingredient_name) {
    return counts.getOrDefault(normalize(ingredient_name), 0);
  }

  /** Returns all ingredients currently in the linked hash map as a list */
  public List<String> get_ingredients() {
    return Collections.unmodifiableList(new ArrayList<>(counts.keySet()));
  }

  /** Returns an unmodifiable view of the underlying usage map. */
  public Map<String, Integer> asMap() {
    return Collections.unmodifiableMap(counts);
  }

  private static String normalize(String name) {
    return name.trim().toLowerCase();
  }

  @Override
  public String toString() {
    return counts.toString();
  }
}
