package com.makotojava.learn.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.makotojava.learn.odot.service.CategoryService;
import com.makotojava.learn.odot.service.ItemService;

@RestController
public class SpringBootDemoController {

  @Autowired
  private ItemService itemService;

  @Autowired
  private CategoryService categoryService;

  public ItemService getItemService() {
    if (itemService == null) {
      throw new RuntimeException("ItemService not configured. Cannot continue.");
    }
    return itemService;
  }

  public CategoryService getCategoryService() {
    if (categoryService == null) {
      throw new RuntimeException("CategoryService not configured. Cannot continue.");
    }
    return categoryService;
  }

}
