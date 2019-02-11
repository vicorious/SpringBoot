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
 * A bunch of Items objects expressed as an Enumeration.
 * 
 * Brilliant!
 * 
 */
public enum Items {
  ITEM_1(ItemTest.ITEM_1),
  ITEM_2(ItemTest.ITEM_2),
  ITEM_3(ItemTest.ITEM_3),
  ITEM_4(ItemTest.ITEM_4),
  ITEM_5(ItemTest.ITEM_5),
  ITEM_6(ItemTest.ITEM_6),
  ITEM_7(ItemTest.ITEM_7),
  ITEM_8(ItemTest.ITEM_8),
  ITEM_9(ItemTest.ITEM_9),
  ITEM_10(ItemTest.ITEM_10);

  private Item item;

  private Items(Item item) {
    this.item = item;
  }

  /**
   * Returns the underlying Item object for this enum
   * member.
   * 
   * @return Item - the Item represented by this enum member.
   */
  public Item value() {
    return this.item;
  }
}