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

@Component
/**
 * Category Data Access Object.
 * Yep, that's a comment alright.
 *
 */
public class CategoryDao {

  private static final Logger log = LoggerFactory.getLogger(CategoryDao.class);

  @Autowired
  private DataSource dataSource;

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
   * Finds All Category records in the DB.
   * 
   * @return List<Category> - the List of Category objects in the DB.
   *         Will be empty if none found.
   */
  public List<Category> findAll() {
    List<Category> ret;
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    String sql = "SELECT * FROM " + Category.TABLE_NAME;
    ret = jdbc.query(sql, new CategoryRowMapper());
    log.info("Found " + ret.size() + " rows from query");
    return ret;
  }

  /**
   * Finds the single Category record having the specified ID,
   * and returns it.
   * 
   * @param id
   *          The ID of the record to find.
   * 
   * @return Category - the Category object whose id matches the
   *         specified ID, or null if no match was found.
   */
  public Category findById(Long id) {
    Category ret = null;
    String sql = "SELECT * FROM " + Category.TABLE_NAME + " WHERE id = ?";
    Object[] paramValues = { id };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    List<Category> categories = jdbc.query(sql, paramValues, new CategoryRowMapper());
    if (categories.size() > 1) {
      throw new RuntimeException("Expected 1 result from findById(), instead found " + categories.size()
          + " (DB configuration error, maybe?)");
    }
    if (!categories.isEmpty()) {
      ret = categories.get(0);
    }
    return ret;
  }

  /**
   * Finds the single Category record having the specified Name,
   * and returns it.
   * 
   * @param name
   *          The Name column value of the record to find.
   * @return Category - the Category object whose Name matches
   *         the specified Name, or null if no match was found.
   */
  public Category findByName(String name) {
    Category ret = null;
    String sql = "SELECT * FROM " + Category.TABLE_NAME + " WHERE name = ?";
    Object[] paramValues = { name };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    List<Category> categories = jdbc.query(sql, paramValues, new CategoryRowMapper());
    if (categories.size() > 1) {
      throw new RuntimeException("Expected 1 result from findByName(), instead found " + categories.size()
          + " (DB configuration error, maybe?)");
    }
    if (!categories.isEmpty()) {
      ret = categories.get(0);
    }
    return ret;
  }

  /**
   * Adds the specified Category object to the DB.
   * 
   * @param category
   *          The Category object to add.
   * @return Category - The Category object that was added.
   * @throws EntityPersistenceException
   *           If the specified Category already exists in the DB
   *           then this exception will be thrown.
   */
  public Category add(Category category) throws EntityPersistenceException {
    Category ret = null;
    //
    String sql = "INSERT INTO " + Category.TABLE_NAME + "(NAME, DESCRIPTION) VALUES(?, ?)";
    Object[] paramValues = { category.getName(), category.getDescription() };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    try {
      int numRowsAffected = jdbc.update(sql, paramValues);
      if (numRowsAffected == 1) {
        ret = findByName(category.getName());
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
   * Updates the specified Category object in the DB.
   * 
   * @param category
   *          The Category object to update.
   * @return Category - The Category object that was updated.
   * @throws EntityPersistenceException
   *           If the specified Category does not exist in the DB
   *           then this exception will be thrown.
   */
  public boolean update(Category category) {
    boolean ret = false;
    //
    String sql = "UPDATE " + Category.TABLE_NAME + " SET description=? WHERE id=?";
    Object[] paramValues = { category.getDescription(), category.getId() };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    try {
      log.info("Attempting to update " + Category.TABLE_NAME + " table with object "
          + ReflectionToStringBuilder.toString(category));
      int numRowsAffected = jdbc.update(sql, paramValues);
      if (numRowsAffected == 1) {
        ret = true;
      } else {
        String message = "Expected 1 row to be affected by UPDATE, instead " + numRowsAffected
            + " were affected (bad ID, maybe?)";
        log.error(message);
      }
    } catch (DataAccessException e) {
      String message = "Exception occurred while updating record";
      log.error(message, e);
    }
    return ret;
  }

  /**
   * Deletes the specified Category object from the DB.
   * 
   * @param category
   *          The Category object to delete.
   * @return Category - The Category object that was deleted.
   * @throws EntityPersistenceException
   *           If the specified Category does not exist in the DB
   *           then this exception will be thrown.
   */
  public Category delete(Category category) throws EntityPersistenceException {
    Category ret = null;
    //
    String sql = "DELETE FROM " + Category.TABLE_NAME + " WHERE id = ?";
    Object[] paramValues = { category.getId() };
    JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
    try {
      int numRowsAffected = jdbc.update(sql, paramValues);
      if (numRowsAffected != 1) {
        String message = "Expected 1 row to be affected by DELETE, instead " + numRowsAffected
            + " were affected (bad ID, maybe?)";
        log.error(message);
        throw new EntityPersistenceException(message);
      }
      ret = category;
    } catch (DataAccessException e) {
      String message = "Exception occurred while deleting record";
      log.error(message, e);
      throw new EntityPersistenceException(e);
    }

    return ret;
  }

  /**
   * RowMapper for Category objects.
   * Used in conjection with JdbcTemplate.
   */
  public static class CategoryRowMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
      return new Category()
          .withId(resultSet.getLong("id"))
          .withWhenCreated(resultSet.getDate("when_created"))
          .withWhenLastUpdated(resultSet.getDate("when_last_updated"))
          //
          .withDescription(resultSet.getString("description"))
          .withName(resultSet.getString("name"));
    }
  }

}
