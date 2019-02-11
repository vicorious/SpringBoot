package com.makotojava.learn.springboot.web;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.makotojava.learn.odot.exception.ServiceException;
import com.makotojava.learn.odot.model.Item;
import com.makotojava.learn.springboot.SpringBootDemoController;

@RestController
@RequestMapping("/ItemRestService")
public class ItemRestService extends SpringBootDemoController {

  @RequestMapping("/FindAll")
  public List<Item> findAllItems() {
    return getItemService().findAll();
  }

  @RequestMapping("/FindById/{id}")
  public Item findById(@PathVariable Long id) {
    return getItemService().findById(id);
  }

  @RequestMapping("/FindByDescription/{description}")
  public Item findByDescription(@PathVariable String description) {
    return getItemService().findByDescription(description);
  }

  @RequestMapping(value = "/Add", method = RequestMethod.PUT)
  public Item add(@RequestBody Item item) {
    Item ret;
    try {
      ret = getItemService().add(item);
    } catch (ServiceException e) {
      throw new RuntimeException("Could not add Item: " + ReflectionToStringBuilder.toString(item), e);
    }
    return ret;
  }

  @RequestMapping(value = "/Update", method = RequestMethod.POST)
  public String update(@RequestBody Item item) {
    String ret = "UPDATE FAILED";
    try {
      boolean updated = getItemService().update(item);
      if (updated) {
        ret = "UPDATE SUCCESSFUL";
      }
    } catch (ServiceException e) {
      throw new RuntimeException("Could not update Item: " + ReflectionToStringBuilder.toString(item), e);
    }
    return ret;
  }

  @RequestMapping(value = "/Delete", method = RequestMethod.DELETE)
  public String delete(@RequestBody Item item) {
    String ret = "DELETE FAILED";
    try {
      getItemService().delete(item);
      ret = "DELETE SUCCESSFUL";
    } catch (ServiceException e) {
      throw new RuntimeException("Could not delete Item: " + ReflectionToStringBuilder.toString(item), e);
    }
    return ret;
  }

}
