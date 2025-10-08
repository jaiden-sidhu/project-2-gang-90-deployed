package edu.tamu.project2.csce331;

import java.util.Set;

public class Employee {
  private int id;
  private String name;
  private String role;
  private double pay;
  private static final Set<String> VALID_ROLES = Set.of("cashier", "manager");

  // Constructor
  public Employee(int id, String name, String role, double pay) throws IllegalArgumentException {
    // Quick luh checks
    if (id < 0) {
      throw new IllegalArgumentException("ID cannot be negative.");
    }
    if (pay < 0) {
      throw new IllegalArgumentException("Pay cannot be negative.");
    }
    if (!VALID_ROLES.contains(role)) {
      throw new IllegalArgumentException(
          "Invalid role" + role + ". Valid roles are: " + VALID_ROLES);
    }

    this.id = id;
    this.name = name;
    this.role = role;
    this.pay = pay;
  }

  // Getters
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getRole() {
    return role;
  }

  public double getPay() {
    return pay;
  }

  // Setters
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

  public void setRole(String role) throws IllegalArgumentException {
    if (VALID_ROLES.contains(role)) {
      this.role = role;
    } else {
      throw new IllegalArgumentException(
          "Invalid role" + role + ". Valid roles are: " + VALID_ROLES);
    }
  }

  public void setPay(double pay) throws IllegalArgumentException {
    if (pay >= 0) {
      this.pay = pay;
    } else {
      throw new IllegalArgumentException("Pay cannot be negative.");
    }
  }
}
