package edu.tamu.project2.csce331;

import java.util.Set;

/**
 * Represents an employee with an ID, name, role, pay, and status.
 * Valid roles are "cashier" and "manager".
 * This class provides getters and setters for each field with validation.
 *  
 * @author Daniel Zhang
 */
public class Employee {
  private int id;
  private String name;
  private String role;
  private double pay;
  private boolean status;
  private static final Set<String> VALID_ROLES = Set.of("cashier", "manager");

  /**
   * Constructs an Employee object with the specified id, name, role, pay, and status.
   * @param id the employee's ID, must be non-negative
   * @param name the employee's name
   * @param role the employee's role, must be one of the valid roles
   * @param pay the employee's pay, must be non-negative
   * @param state the employee's status (active or inactive)
   * @throws IllegalArgumentException if id or pay is negative, or if role is invalid
   */
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

  /**
   * Returns the employee's ID.
   * @return the employee's ID
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the employee's name.
   * @return the employee's name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the employee's role.
   * @return the employee's role
   */
  public String getRole() {
    return role;
  }

  /**
   * Returns the employee's pay.
   * @return the employee's pay
   */
  public double getPay() {
    return pay;
  }

  /**
   * Returns the employee's status.
   * @return the employee's status
   */
  public boolean getStatus() {
    return status;
  }

  /**
   * Sets the employee's ID.
   * @param id the new ID, must be non-negative
   * @throws IllegalArgumentException if id is negative
   */
  public void setId(int id) throws IllegalArgumentException {
    if (id >= 0) {
      this.id = id;
    } else {
      throw new IllegalArgumentException("ID cannot be negative.");
    }
  }

  /**
   * Sets the employee's name.
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the employee's role.
   * @param role the new role, must be one of the valid roles
   * @throws IllegalArgumentException if role is invalid
   */
  public void setRole(String role) throws IllegalArgumentException {
    if (VALID_ROLES.contains(role)) {
      this.role = role;
    } else {
      throw new IllegalArgumentException(
          "Invalid role" + role + ". Valid roles are: " + VALID_ROLES);
    }
  }

  /**
   * Sets the employee's pay.
   * @param pay the new pay, must be non-negative
   * @throws IllegalArgumentException if pay is negative
   */
  public void setPay(double pay) throws IllegalArgumentException {
    if (pay >= 0) {
      this.pay = pay;
    } else {
      throw new IllegalArgumentException("Pay cannot be negative.");
    }
  }
}
