package com.pratamawijaya.examplerealm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by : pratama - set.mnemonix@gmail.com
 * Date : 2/15/16
 * Project : ExampleRealm
 */
public class Book extends RealmObject {
  @PrimaryKey private String name;
  private String author;
  private Date createdAt;
  private Category category;

  public Book() {
  }

  public Book(String name, String author, Date createdAt, Category category) {
    this.name = name;
    this.author = author;
    this.createdAt = createdAt;
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
