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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.makotojava.learn.odot.TestCategoryDaoConfiguration;
import com.makotojava.learn.odot.TestConfiguration;
import com.makotojava.learn.odot.TestConfigurationEmptyDb;
import com.makotojava.learn.odot.exception.EntityPersistenceException;
import com.makotojava.learn.odot.model.Category;

/**
 * Not really a unit test. Uses a live embedded Derby database
 * to do real testing, so more of an integration test.
 * 
 * @author J Steven Perry
 *
 */
@RunWith(JUnitPlatform.class)
@DisplayName("Testing CategoryDao")
public class CategoryDaoTest {

  private static final Logger log = LoggerFactory.getLogger(CategoryDaoTest.class);

  private CategoryDao classUnderTest;

  @BeforeAll
  static void init() {
    log.info("Tests starting...");
  }

  @BeforeEach
  void setUp() {
    log.info("Test beginning...");
  }

  @AfterEach
  void tearDown() {
    log.info("Test complete.");
  }

  @AfterAll
  static void done() {
    log.info("Tests complete.");
  }

  @Nested
  @DisplayName("Empty DB Scenarios")
  class CategoryDaoEmptyDbScenariosTest {
    private ApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfigurationEmptyDb.class);

    @BeforeEach
    public void setUp() throws Exception {
      classUnderTest = ctx.getBean(CategoryDao.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
      DataSource dataSource = (DataSource) ctx.getBean("dataSource");
      if (dataSource instanceof EmbeddedDatabase) {
        ((EmbeddedDatabase) dataSource).shutdown();
      }
    }

    @Test
    @DisplayName("findAll() returns empty list")
    public void findAll() {
      List<Category> categories = classUnderTest.findAll();
      assertNotNull(categories);
      assertTrue(categories.isEmpty());
    }

    @Test
    @DisplayName("FindById() returns null")
    public void findById() {
      Category category = classUnderTest.findById(1L);
      assertNull(category);
    }

    @Test
    @DisplayName("FindByName() returns null")
    public void findByName() {
      Category category = classUnderTest.findByName("TEST_CATEGORY1");
      assertNull(category);
    }

  }

  @Nested
  @DisplayName("OPTIMISTIC Scenarios")
  class CategoryDaoOptimisticScenariosTest {

    private ApplicationContext ctx = new AnnotationConfigApplicationContext(TestCategoryDaoConfiguration.class);

    @BeforeEach
    public void setUp() throws Exception {
      classUnderTest = ctx.getBean(CategoryDao.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
      DataSource dataSource = (DataSource) ctx.getBean("dataSource");
      if (dataSource instanceof EmbeddedDatabase) {
        ((EmbeddedDatabase) dataSource).shutdown();
      }
    }

    @Test
    @DisplayName("Find All Categories")
    public void testFindAll() {
      List<Category> categories = classUnderTest.findAll();
      assertNotNull(categories, () -> "List returned from findAll() cannot be null!");
      assertAll(
          () -> assertFalse(categories.isEmpty()),
          () -> assertEquals(4, categories.size()));
    }

    @Test
    @DisplayName("Find Category by ID")
    public void testFindById() {
      // Derby has known gaps in auto-generated IDs, so we
      /// really don't know the IDs for sure.
      // So let's findAll(), then search for all the Ids
      /// from that known list.
      List<Category> categories = classUnderTest.findAll();
      assertNotNull(categories);
      for (Category category : categories) {
        Category categoryById = classUnderTest.findById(category.getId());
        assertNotNull(categoryById);
        doFieldByFieldAssertEquals(category, categoryById);
      }
    }

    @Test
    @DisplayName("Find Category by Name")
    public void testFindByName() {
      List<Category> categories = classUnderTest.findAll();
      assertNotNull(categories);
      assertFalse(categories.isEmpty());
      for (Category category : categories) {
        Category categoryByName = classUnderTest.findByName(category.getName());
        assertNotNull(categoryByName);
        doFieldByFieldAssertEquals(category, categoryByName);
      }
    }

    @Test
    @DisplayName("Add Category")
    public void testAdd() {
      Category category = new Category().withDescription("Test Category #1").withName("TC1");
      try {
        Category categoryAdded = classUnderTest.add(category);
        assertNotNull(categoryAdded);
        assertAll("Fields must be equal",
            () -> assertEquals(category.getName(), categoryAdded.getName()),
            () -> assertEquals(category.getDescription(), categoryAdded.getDescription()));
      } catch (EntityPersistenceException e) {
        fail("Exception thrown. Unit test failed: " + e.getLocalizedMessage());
      }
    }

    @Test
    @DisplayName("Update Category")
    public void testUpdate() {
      // Not sure what IDs are there, so let's grab
      /// them all, update one and make sure the update works
      List<Category> categories = classUnderTest.findAll();
      assertNotNull(categories, "List cannot be null!");
      assertFalse(categories.isEmpty());
      Category cat0 = categories.get(0);
      cat0.withDescription(cat0.getDescription() + "_UPDATED");
      boolean succeeded = classUnderTest.update(cat0);
      assertTrue(succeeded);// , "Update should succeed");
      Category catUpdated = classUnderTest.findById(cat0.getId());
      assertNotNull(catUpdated);
      doFieldByFieldAssertEquals(cat0, catUpdated);
    }

    @Test
    @DisplayName("Delete Category")
    public void testDelete() {
      List<Category> categories = classUnderTest.findAll();
      assertNotNull(categories);
      assertFalse(categories.isEmpty());
      int index = 0;
      for (Category category : categories) {
        try {
          Category catDeleted = classUnderTest.delete(category);
          assertNotNull(catDeleted);
          doFieldByFieldAssertEquals(category, catDeleted);
        } catch (EntityPersistenceException e) {
          fail("Exception thrown processing index " + index + ". Unit test failed: " + e.getLocalizedMessage());
        }
        index++;
      }
    }
  }

  @Nested
  @DisplayName("PESSIMISTIC Scenarios")
  class CategoryDaoPessimisticScenariosTest {

    private ApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfiguration.class);

    @BeforeEach
    public void setUp() throws Exception {
      classUnderTest = ctx.getBean(CategoryDao.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
      DataSource dataSource = (DataSource) ctx.getBean("dataSource");
      if (dataSource instanceof EmbeddedDatabase) {
        ((EmbeddedDatabase) dataSource).shutdown();
      }
    }

    @Test
    @DisplayName("Add existing Category should throw exception")
    public void testAdd() throws Exception {
      List<Category> categories = classUnderTest.findAll();
      assertNotNull(categories, () -> "List returned from findAll() cannot be null!");
      assertFalse(categories.isEmpty(), () -> "List returned from findAll() cannot be empty!");
      Category cat0 = categories.get(0);
      // Try and add the Category that exists already, expect exception...
      assertThrows(EntityPersistenceException.class, () -> classUnderTest.add(cat0));
    }

    @Test
    @DisplayName("Update non-existent Category should fail")
    public void testUpdate() {
      Category category = new Category().withDescription("DESCRIPTION").withName("NAME");
      boolean succeeded = classUnderTest.update(category);
      assertFalse(succeeded, "Update non-existent Category should fail!");
    }

    @Test
    @DisplayName("Delete non-existent Category should fail")
    public void testDelete() {
      Category category = new Category().withDescription("DESCRIPTION").withName("NAME");
      assertThrows(EntityPersistenceException.class, () -> classUnderTest.delete(category));
    }

  }

  private void doFieldByFieldAssertEquals(Category expected, Category actual) {
    assertAll("Fields must be equal",
        () -> assertEquals(expected.getDescription(), actual.getDescription()),
        () -> assertEquals(expected.getId(), actual.getId()),
        () -> assertEquals(expected.getName(), actual.getName()),
        () -> assertEquals(expected.getWhenCreated().getTime(), actual.getWhenCreated().getTime()));
  }

}
