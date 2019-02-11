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

/**
 * A bunch of Category objects expressed as an enum.
 * 
 * Brilliant!
 */
public enum Categories {
  CATEGORY_1(CategoryTest.CATEGORY_1),
  CATEGORY_2(CategoryTest.CATEGORY_2),
  CATEGORY_3(CategoryTest.CATEGORY_3),
  CATEGORY_4(CategoryTest.CATEGORY_4),
  CATEGORY_5(CategoryTest.CATEGORY_5),
  CATEGORY_6(CategoryTest.CATEGORY_6),
  CATEGORY_7(CategoryTest.CATEGORY_7),
  CATEGORY_8(CategoryTest.CATEGORY_8),
  CATEGORY_9(CategoryTest.CATEGORY_9),
  CATEGORY_10(CategoryTest.CATEGORY_10),
  ;

  private Category category;

  private Categories(Category category) {
    this.category = category;
  }

  /**
   * Returns the underlying Category object.
   * 
   * @return Category - the underlying Category object
   *         wrapped by this enum member.
   */
  public Category value() {
    return this.category;
  }
}