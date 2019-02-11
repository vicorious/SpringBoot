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

public class ItemTest {

  private static final Date now = new Date();

  public static final Item ITEM_1 = new Item().withCategory(CategoryTest.CATEGORY_1).withDescription("ITEM_1")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_2 = new Item().withCategory(CategoryTest.CATEGORY_2).withDescription("ITEM_2")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_3 = new Item().withCategory(CategoryTest.CATEGORY_3).withDescription("ITEM_3")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_4 = new Item().withCategory(CategoryTest.CATEGORY_4).withDescription("ITEM_4")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_5 = new Item().withCategory(CategoryTest.CATEGORY_5).withDescription("ITEM_5")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_6 = new Item().withCategory(CategoryTest.CATEGORY_6).withDescription("ITEM_6")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_7 = new Item().withCategory(CategoryTest.CATEGORY_7).withDescription("ITEM_7")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_8 = new Item().withCategory(CategoryTest.CATEGORY_8).withDescription("ITEM_8")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_9 = new Item().withCategory(CategoryTest.CATEGORY_9).withDescription("ITEM_9")
      .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
      .withWhenLastUpdated(now);
  public static final Item ITEM_10 =
      new Item().withCategory(CategoryTest.CATEGORY_10).withDescription("ITEM_10")
          .withDueDate(now).withFinished(false).withId(100L).withWhenCreated(now)
          .withWhenLastUpdated(now);
}
