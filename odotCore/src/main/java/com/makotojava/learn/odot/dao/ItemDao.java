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
package com.makotojava.learn.odot.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.makotojava.learn.odot.exception.EntityPersistenceException;
import com.makotojava.learn.odot.model.Category;
import com.makotojava.learn.odot.model.Item;

@Component
/**
 * Item Data Access Object.
 * Yep, that's a comment alright.
 *
 */
public class ItemDao {

  private static final Logger log = LoggerFactory.getLogger(ItemDao.class);

  @Autowired
  private DataSource dataSource;

  @Autowired
  /**
   * Let Spring wire us up with a CategoryDao bean
   * so we can fetch nested instances of Category.
   */
  private CategoryDao categoryDao;

  /**
   * NPE preventer. NEVER use a raw class-level reference. Use
   * this getter instead.
   */
  private DataSource getDataSource() {
    if (dataSource == null) {
      throw new RuntimeException("DataSource is null (configuration error, perhaps?)");
    }
    return dataSource;
  }

  /**
   * Not everyone uses Spring, and this method is for those weirdos.
   * Hey, weirdos are people too.
   */
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Finds All Item records in the DB.
   * 
   * @return List<Item> - the List of item objects in the DB.
   *         Will be empty if none found.
   */
  public List<Item> findAll() {
    List<Item> ret;
    String sql = "SELECT * FROM " + Item.TABLE_NAME;
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    ret = jdbc.query(sql, new ItemRowMapper());
    if (ret.size() > 1) {
      log.info("Found " + ret.size() + " rows from query");
    }
    return ret;
  }

  /**
   * Finds the single Item record having the specified ID,
   * and returns it.
   * 
   * @param id
   *          The ID of the record to find.
   * 
   * @return Item - the Item object whose id matches the
   *         specified ID, or null if no match was found.
   */
  public Item findById(Long id) {
    Item ret = null;
    String sql = "SELECT * FROM " + Item.TABLE_NAME + " WHERE id = ?";
    Object[] paramValues = { id };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    List<Item> items = jdbc.query(sql, paramValues, new ItemRowMapper());
    if (items.size() > 1) {
      throw new RuntimeException("Expected 1 result from findById(), instead found " + items.size()
          + " (DB configuration error, maybe?)");
    }
    if (!items.isEmpty()) {
      ret = items.get(0);
    }
    return ret;
  }

  /**
   * Finds the single Item record having the specified
   * Description, and returns it.
   * 
   * @param description
   *          The Description of the record to find.
   * 
   * @return Item - the Item object whose id matches the
   *         specified description, or null if no match was
   *         found.
   */
  public Item findByDescription(String description) {
    Item ret = null;
    String sql = "SELECT * FROM " + Item.TABLE_NAME + " WHERE description = ?";
    Object[] paramValues = { description };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    List<Item> items = jdbc.query(sql, paramValues, new ItemRowMapper());
    if (items.size() > 1) {
      throw new RuntimeException("Expected 1 result from findById(), instead found " + items.size()
          + " (DB configuration error, maybe?)");
    }
    if (!items.isEmpty()) {
      ret = items.get(0);
    }
    return ret;
  }

  /**
   * Adds the specified Item object to the DB.
   * 
   * @param item
   *          The Item object to add.
   * @return Item - The Item object that was added.
   * @throws EntityPersistenceException
   *           If the specified Item already exists in the DB
   *           then this exception will be thrown.
   */
  public Item add(Item item) throws EntityPersistenceException {
    Item ret = null;
    //
    String sql = "INSERT INTO " + Item.TABLE_NAME + "(description, due_date, finished, category_id) VALUES(?, ?, ?, ?)";
    Object[] paramValues = {
        item.getDescription(),
        (item.getDueDate() == null) ? new Date() : item.getDueDate(),
        (item.getFinished() == null) ? false : item.getFinished(),
        (item.getCategory() == null) ? null : item.getCategory().getId()
    };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    try {
      int numRowsAffected = jdbc.update(sql, paramValues);
      if (numRowsAffected == 1) {
        ret = findByDescription(item.getDescription());
      } else {
        String message = "Expected 1 row to be affected by INSERT, instead " + numRowsAffected
            + " were affected (DB configuration error, maybe?)";
        log.error(message);
        throw new EntityPersistenceException(message);
      }
    } catch (DataAccessException e) {
      String message = "Exception occurred while inserting record";
      log.error(message, e);
      throw new EntityPersistenceException(e);
    }
    return ret;
  }

  /**
   * Updates the specified Item object in the DB.
   * 
   * @param item
   *          The Item object to update.
   * @return Item - The Item object that was updated.
   * @throws EntityPersistenceException
   *           If the specified Item does not exist in the DB
   *           then this exception will be thrown.
   */
  public boolean update(Item item) {
    boolean ret = false;
    //
    String sql = "UPDATE " + Item.TABLE_NAME + " SET description=?, category_id=?, due_date=?, finished=? WHERE id=?";
    Object[] paramValues = {
        item.getDescription(),
        (item.getCategory() == null) ? null : item.getCategory().getId(),
        (item.getDueDate() == null) ? null : item.getDueDate(),
        (item.getFinished() == null) ? null : item.getFinished(),
        item.getId()
    };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    try {
      log.info("Attempting to update " + Item.TABLE_NAME + " table with object "
          + ReflectionToStringBuilder.toString(item));
      int numRowsAffected = jdbc.update(sql, paramValues);
      if (numRowsAffected == 1) {
        ret = true;
      } else {
        String message = "Expected 1 row to be affected by UPDATE, instead " + numRowsAffected
            + " were affected (bad ID, maybe?)";
        log.error(message);
      }
    } catch (DataAccessException e) {
      String message = "Exception occurred while inserting record";
      log.error(message, e);
    }
    return ret;
  }

  /**
   * Deletes the specified Item object from the DB.
   * 
   * @param item
   *          The Item object to delete.
   * @return Item - The Item object that was deleted.
   * @throws EntityPersistenceException
   *           If the specified Item does not exist in the DB
   *           then this exception will be thrown.
   */
  public Item delete(Item item) throws EntityPersistenceException {
    Item ret = null;
    //
    String sql = "DELETE FROM " + Item.TABLE_NAME + " WHERE id = ?";
    Object[] paramValues = { item.getId() };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    try {
      int numRowsAffected = jdbc.update(sql, paramValues);
      if (numRowsAffected != 1) {
        String message = "Expected 1 row to be affected by DELETE, instead " + numRowsAffected
            + " were affected (bad ID, maybe?)";
        log.error(message);
        throw new EntityPersistenceException(message);
      }
      ret = item;
    } catch (DataAccessException e) {
      String message = "Exception occurred while inserting record";
      log.error(message, e);
      throw new EntityPersistenceException(e);
    }

    return ret;
  }

  /**
   * RowMapper for Item objects.
   * Used in conjection with JdbcTemplate.
   */
  public class ItemRowMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
      return new Item()
          .withId(resultSet.getLong("id"))
          .withWhenCreated(resultSet.getDate("when_created"))
          .withWhenLastUpdated(resultSet.getDate("when_last_updated"))
          //
          .withDescription(resultSet.getString("description"))
          .withDueDate(resultSet.getDate("due_date"))
          .withFinished(resultSet.getBoolean("finished"))
          .withCategory(mapCategory(resultSet.getLong("category_id")));
    }

    private Category mapCategory(Long categoryId) {
      return categoryDao.findById(categoryId);
    }

  }

}
