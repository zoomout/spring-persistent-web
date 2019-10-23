package com.bogdan.persistentweb.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

  private String title;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @ManyToMany(mappedBy = "products")
  private Set<Customer> customers = Collections.newSetFromMap(new ConcurrentHashMap<>());

  public Set<Customer> getCustomers() {
    return customers;
  }

  public void addCustomer(Customer customer) {
    customer.getProducts().add(this);
  }

  public void removeCustomer(Customer customer) {
    customer.getProducts().remove(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Product customer = (Product) o;
    return Objects.equals(title, customer.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), title);
  }
}
