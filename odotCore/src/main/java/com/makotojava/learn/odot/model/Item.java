/*
 * Copyright 2017 Makoto Consulting Group, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.makotojava.learn.odot.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = Item.TABLE_NAME)
public class Item extends AbstractEntity {

  public static final String TABLE_NAME = "odot_item";

  private String description;
  private Date dueDate;
  private Boolean finished;
  private Category category;

  public String getDescription() {
    return description;
  }

  public Item withDescription(String description) {
    this.description = description;
    return this;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public Item withDueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  public Boolean getFinished() {
    return finished;
  }

  public Item withFinished(Boolean finished) {
    this.finished = finished;
    return this;
  }

  @ManyToOne
  @JoinColumn(name = "category_id")
  public Category getCategory() {
    return category;
  }

  public Item withCategory(Category category) {
    this.category = category;
    return this;
  }

  @Override
  public Item withId(Long id) {
    super.withId(id);
    return this;
  }

  @Override
  public Item withWhenCreated(Date whenCreated) {
    super.withWhenCreated(whenCreated);
    return this;
  }

  @Override
  public Item withWhenLastUpdated(Date whenLastUpdated) {
    super.withWhenLastUpdated(whenLastUpdated);
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
    result = prime * result + ((finished == null) ? 0 : finished.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Item other = (Item) obj;
    if (category == null) {
      if (other.category != null)
        return false;
    } else if (!category.equals(other.category))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (dueDate == null) {
      if (other.dueDate != null)
        return false;
    } else if (!dueDate.equals(other.dueDate))
      return false;
    if (finished == null) {
      if (other.finished != null)
        return false;
    } else if (!finished.equals(other.finished))
      return false;
    return true;
  }

}
