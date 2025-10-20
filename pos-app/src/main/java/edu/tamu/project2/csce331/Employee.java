package edu.tamu.project2.csce331;

import java.util.Set;
import java.sql.Timestamp;

public class Employee {
  private int id;
  private String name;
  private String role;
  private double pay;
  private boolean status;
  private static final Set<String> VALID_ROLES = Set.of("cashier", "manager");

  // Constructor
  public Employee(int id, String name, String role, double pay, boolean state) throws IllegalArgumentException {
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
    this.status = state;
  }

  // Getters
  public int get_id() {
    return id;
  }

  public String get_name() {
    return name;
  }

  public String get_role() {
    return role;
  }

  public double get_pay() {
    return pay;
  }

  public boolean get_status() {
    return status;
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

  public void set_role(String role) throws IllegalArgumentException {
    if (VALID_ROLES.contains(role)) {
      this.role = role;
    } else {
      throw new IllegalArgumentException(
          "Invalid role" + role + ". Valid roles are: " + VALID_ROLES);
    }
  }

  public void set_pay(double pay) throws IllegalArgumentException {
    if (pay >= 0) {
      this.pay = pay;
    } else {
      throw new IllegalArgumentException("Pay cannot be negative.");
    }
  }
}
