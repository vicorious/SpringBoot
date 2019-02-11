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

public class CategoryTest {

  private static final Date now = new Date();

  public static final Category CATEGORY_1 =
      new Category().withName("CATEGORY_1").withDescription("CATEGORY_1").withId(100L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_2 =
      new Category().withName("CATEGORY_2").withDescription("CATEGORY_2").withId(200L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_3 =
      new Category().withName("CATEGORY_3").withDescription("CATEGORY_3").withId(300L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_4 =
      new Category().withName("CATEGORY_4").withDescription("CATEGORY_4").withId(400L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_5 =
      new Category().withName("CATEGORY_5").withDescription("CATEGORY_5").withId(500L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_6 =
      new Category().withName("CATEGORY_6").withDescription("CATEGORY_6").withId(600L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_7 =
      new Category().withName("CATEGORY_7").withDescription("CATEGORY_7").withId(700L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_8 =
      new Category().withName("CATEGORY_8").withDescription("CATEGORY_8").withId(800L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_9 =
      new Category().withName("CATEGORY_9").withDescription("CATEGORY_9").withId(900L)
          .withWhenCreated(now).withWhenLastUpdated(now);
  public static final Category CATEGORY_10 =
      new Category().withName("CATEGORY_10").withDescription("CATEGORY_10").withId(1000L)
          .withWhenCreated(now).withWhenLastUpdated(now);

}
