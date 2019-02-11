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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.opentest4j.MultipleFailuresError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.makotojava.learn.odot.TestConfiguration;
import com.makotojava.learn.odot.TestConfigurationEmptyDb;
import com.makotojava.learn.odot.exception.EntityPersistenceException;
import com.makotojava.learn.odot.model.Item;

@RunWith(JUnitPlatform.class)
@DisplayName("Testing ItemDao DAO class")
public class ItemDaoTest {

  private static final Logger log = LoggerFactory.getLogger(ItemDaoTest.class);

  private ItemDao classUnderTest;

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
  class ItemDaoEmptyDbScenariosTest {
    private ApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfigurationEmptyDb.class);

    @BeforeEach
    public void setUp() throws Exception {
      classUnderTest = ctx.getBean(ItemDao.class);
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
    public void testFindAll() {
      List<Item> items = classUnderTest.findAll();
      assertNotNull(items);
      assertTrue(items.isEmpty());
    }
  }

  @Nested
  @DisplayName("OPTIMISTIC Scenarios")
  class ItemDaoOptimisticScenariosTest {

    private ApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfiguration.class);

    @BeforeEach
    public void setUp() throws Exception {
      classUnderTest = ctx.getBean(ItemDao.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
      DataSource dataSource = (DataSource) ctx.getBean("dataSource");
      if (dataSource instanceof EmbeddedDatabase) {
        ((EmbeddedDatabase) dataSource).shutdown();
      }
    }

    @Test
    @DisplayName("Find All Items")
    public void testFindAll() {
      List<Item> items = classUnderTest.findAll();
      assertNotNull(items);
      assertAll(
          () -> assertFalse(items.isEmpty()),
          () -> assertEquals(7, items.size()));
      if (log.isDebugEnabled()) {
        log.debug("Items found: ");
        for (Item item : items) {
          log.debug(ReflectionToStringBuilder.toString(item, new RecursiveToStringStyle()));
        }
      }
    }

    @Test
    @DisplayName("Find specific Item by ID")
    public void testFindById() {
      // Derby has known gaps in auto-generated IDs, so we
      /// really don't know the IDs for sure.
      // So let's findAll(), then search for all the Ids
      /// from that known list.
      List<Item> items = classUnderTest.findAll();
      assertNotNull(items);
      assertFalse(items.isEmpty());
      for (Item item : items) {
        Item itemById = classUnderTest.findById(item.getId());
        assertNotNull(itemById);
        doFieldByFieldAssertEquals(item, itemById);
      }
    }

    @Test
    @DisplayName("Find specific Item by Description")
    public void testFindByDescription() {
      List<Item> items = classUnderTest.findAll();
      assertNotNull(items);
      assertFalse(items.isEmpty());
      for (Item item : items) {
        Item itemByDescription = classUnderTest.findByDescription(item.getDescription());
        assertNotNull(itemByDescription);
        doFieldByFieldAssertEquals(item, itemByDescription);
      }
    }

    @Test
    @DisplayName("Add Item")
    public void testAdd() {
      Item item = new Item()
          .withDescription("Unit test item #1");
      try {
        Item itemAdded = classUnderTest.add(item);
        assertNotNull(itemAdded);
        assertAll("Fields must be equal",
            () -> assertEquals(item.getDescription(), itemAdded.getDescription()));
      } catch (EntityPersistenceException e) {
        fail("Exception thrown. Unit test failed: " + e.getLocalizedMessage());
      }
    }

    @Nested
    @DisplayName("Add tests with category")
    class ItemDaoOptimisticAddWithCategoryScenariosTest {

      @Test
      @DisplayName("Add Item")
      public void testAdd() {
        CategoryDao categoryDao = ctx.getBean(CategoryDao.class);
        Item item = new Item()
            .withDescription("Unit test w/ Category item #1")
            .withCategory(categoryDao.findByName("TEST_CATEGORY_3"));
        try {
          Item itemAdded = classUnderTest.add(item);
          assertNotNull(itemAdded);
          assertNotNull(itemAdded.getId());
          assertNotNull(itemAdded.getWhenCreated());
          assertNotNull(itemAdded.getWhenLastUpdated());
          assertAll("Fields must be equal",
              () -> assertEquals(item.getDescription(), itemAdded.getDescription()),
              () -> assertEquals(item.getCategory(), itemAdded.getCategory()));
        } catch (EntityPersistenceException e) {
          fail("Exception thrown. Unit test failed: " + e.getLocalizedMessage());
        }
      }

    }

    @Test
    @DisplayName("Update Item")
    public void testUpdate() {
      // Not sure what IDs are there, so let's grab
      /// them all, update one and make sure the update works
      List<Item> items = classUnderTest.findAll();
      assertNotNull(items);
      assertFalse(items.isEmpty());
      Item item0 = items.get(0);
      item0.withDescription(item0.getDescription() + "_UPDATED");
      boolean succeeded = classUnderTest.update(item0);
      assertTrue(succeeded);
      Item itemUpdated = classUnderTest.findById(item0.getId());
      assertNotNull(itemUpdated);
      doFieldByFieldAssertEquals(item0, itemUpdated);
    }

    @Nested
    @DisplayName("Update tests with category")
    class ItemDaoOptimisticUpdateWithCategoryScenariosTest {

      @Test
      @DisplayName("Update Item")
      public void testUpdate() {
        CategoryDao categoryDao = ctx.getBean(CategoryDao.class);
        // Not sure what IDs are there, so let's grab
        /// them all, update one and make sure the update works
        List<Item> items = classUnderTest.findAll();
        assertNotNull(items);
        assertFalse(items.isEmpty());
        Item item0 = items.get(0);
        item0.withDescription(item0.getDescription() + "_UPDATED")
            .withCategory(categoryDao.findByName("TEST_CATEGORY_4"));
        boolean succeeded = classUnderTest.update(item0);
        assertTrue(succeeded);
        Item itemUpdated = classUnderTest.findById(item0.getId());
        assertNotNull(itemUpdated);
        doFieldByFieldAssertEquals(item0, itemUpdated);
      }

    }

    @Test
    @DisplayName("Delete Item")
    public void testDelete() {
      List<Item> items = classUnderTest.findAll();
      assertNotNull(items);
      assertFalse(items.isEmpty());
      Item item0 = items.get(0);
      int index = 0;
      try {
        Item itemDeleted = classUnderTest.delete(item0);
        assertNotNull(itemDeleted);
        doFieldByFieldAssertEquals(item0, itemDeleted);
      } catch (EntityPersistenceException e) {
        fail("Exception thrown processing index " + index + ". Unit test failed: " + e.getLocalizedMessage());
      }
      index++;
    }

  }

  @Nested
  @DisplayName("PESSIMISTIC Scenarios")
  class ItemDaoPessimisticScenariosTest {

    private ApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfiguration.class);

    @BeforeEach
    public void setUp() throws Exception {
      classUnderTest = ctx.getBean(ItemDao.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
      DataSource dataSource = (DataSource) ctx.getBean("dataSource");
      if (dataSource instanceof EmbeddedDatabase) {
        ((EmbeddedDatabase) dataSource).shutdown();
      }
    }

    @Test
    @DisplayName("Add existing Item should fail")
    public void testAdd() {
      List<Item> items = classUnderTest.findAll();
      assertNotNull(items);
      assertFalse(items.isEmpty());
      Item item0 = items.get(0);
      // Attempt to add, throws exception
      assertThrows(EntityPersistenceException.class, () -> classUnderTest.add(item0));
    }

    @Test
    @DisplayName("Update non-existent Item should fail")
    public void testUpdate() {
      Item item = new Item().withDescription("DESCRIPTION");
      // Attempt to update, fails
      boolean succeeded = classUnderTest.update(item);
      assertFalse(succeeded);
    }

    @Test
    @DisplayName("Delete non-existent Item should fail")
    public void testDelete() {
      Item item = new Item().withDescription("DESCRIPTION");
      // Attempt to delete, throws Exception
      assertThrows(EntityPersistenceException.class, () -> classUnderTest.delete(item));
    }

  }

  private void doFieldByFieldAssertEquals(Item expectedItem, Item actualItem) throws MultipleFailuresError {
    assertAll("Fields must be equal",
        () -> assertEquals(expectedItem.getId(), actualItem.getId()),
        () -> assertEquals(expectedItem.getCategory(), actualItem.getCategory()),
        () -> assertEquals(expectedItem.getDescription(), actualItem.getDescription()),
        () -> assertEquals(expectedItem.getDueDate(), actualItem.getDueDate()),
        () -> assertEquals(expectedItem.getFinished(), actualItem.getFinished()),
        () -> assertEquals(expectedItem.getWhenCreated(), actualItem.getWhenCreated()));
  }

}
