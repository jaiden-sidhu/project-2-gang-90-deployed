package edu.tamu.project2.csce331;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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
  public void put(String ingredientName, int count) {
    String normalized = normalize(ingredientName);
    counts.merge(normalized, count, Integer::sum);
  }

  /** Returns the usage count for a given ingredient (0 if absent). */
  public int get(String ingredientName) {
    return counts.getOrDefault(normalize(ingredientName), 0);
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
