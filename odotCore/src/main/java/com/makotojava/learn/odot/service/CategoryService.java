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
package com.makotojava.learn.odot.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.makotojava.learn.odot.dao.CategoryDao;
import com.makotojava.learn.odot.exception.EntityPersistenceException;
import com.makotojava.learn.odot.exception.ServiceException;
import com.makotojava.learn.odot.model.Category;

@Service
/**
 * Service POJO for Category.
 * Nailed the comment!
 *
 */
public class CategoryService {

  private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

  static final String NOT_INITIALIZED_MESSAGE = "Category DAO has not been initialized, cannot continue.";

  @Autowired
  private CategoryDao categoryDao;

  CategoryDao getCategoryDao() {
    if (categoryDao == null) {
      throw new RuntimeException(NOT_INITIALIZED_MESSAGE);
    }
    return categoryDao;
  }

  /**
   * Not everyone uses Spring, and this method is for those weirdos.
   * Hey, weirdos are people too.
   */
  public void setCategoryDao(CategoryDao categoryDao) {
    this.categoryDao = categoryDao;
  }

  /**
   * Finds all Category objects and returns a List of them.
   * 
   * @return List<Category> - the List of Category objects, will
   *         be empty if none found.
   */
  public List<Category> findAll() {
    return getCategoryDao().findAll();
  }

  /**
   * Find the lone Category matching the specified ID, and
   * return it.
   * 
   * @param id
   *          The ID of the Category to find.
   * 
   * @return Category - the Category whose ID matches the
   *         one specified, or null if none matched.
   */
  public Category findById(Long id) {
    return getCategoryDao().findById(id);
  }

  /**
   * Find the lone Category matching the specified Name,
   * and return it.
   * 
   * @param name
   *          The Name of the Category to find.
   * 
   * @return Category - the Category whose Name matches the
   *         one specified, or null if none matched.
   */
  public Category findByName(String name) {
    return getCategoryDao().findByName(name);
  }

  /**
   * Adds the specified Category object to the system of record.
   * 
   * @param category
   *          The Category object to add.
   * @return Category - The Category object that was added.
   * @throws ServiceException
   *           If something goes wrong, the
   *           underlying Exception will be the cause, wrapped by the
   *           ServiceException.
   */
  public Category add(Category category) throws ServiceException {
    Category ret;
    try {
      ret = getCategoryDao().add(category);
    } catch (EntityPersistenceException e) {
      String message = "Exception thrown while adding Category object";
      log.error(message, e);
      throw new ServiceException(message, e);
    }
    return ret;
  }

  /**
   * Update the specified Category object in the system of record.
   * 
   * @param category
   *          The Category object to update.
   * 
   * @return Boolean - true if the update succeeded, false otherwise.
   */
  public boolean update(Category category) throws ServiceException {
    boolean ret;
    ret = getCategoryDao().update(category);
    if (ret == false) {
      String message = "Update FAILED";
      log.error(message);
      throw new ServiceException(message);
    }
    return ret;
  }

  /**
   * Removes the specified Category object from the system of record.
   * 
   * @param category
   *          The Category object to delete.
   * @return Category - The Category object that was deleted.
   * @throws ServiceException
   *           If something goes wrong, the
   *           underlying Exception will be the cause, wrapped by the
   *           ServiceException.
   */
  public Category delete(Category category) throws ServiceException {
    Category ret;
    try {
      ret = getCategoryDao().delete(category);
    } catch (EntityPersistenceException e) {
      String message = "Exception thrown while deleting Category object";
      log.error(message, e);
      throw new ServiceException(message, e);
    }
    return ret;
  }

}
