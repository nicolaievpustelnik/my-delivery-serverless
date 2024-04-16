package com.lib.support;

import java.util.ArrayList;
import java.util.List;

public class Instances {
  private Instances(){}

  private static int counter = 0;
  private static List<Integer> created = new ArrayList<>();

  public static MyEntity create(String name, MyRepository repository) {

    var entity = createTransient(name);

    repository.save(entity);

    created.add(entity.getId());
    
    return entity;
  }

  public static void deleteAll(MyRepository repository) {
    repository.deleteAllById(created);
    created.clear();
  }

  public static MyEntity createTransient(String name) {
    
    var entity = new MyEntity();
    entity.setId(++counter);
    entity.setName(name);

    return entity;
  }

  public static void delete(Integer id, MyRepository repository) {
    repository.deleteById(id);

    var found = created.indexOf(id);
    if(found > -1) {
      created.remove(found);
    }
  }

  public static int currentCounter() {
    return counter;
  }

  public static void incrementCounter() {
    counter++;
  }

  public static void counterToOdd() {

    while((++counter % 2) == 0){}

  }

  public static void counterToEven() {

    while((++counter % 2) != 0){}

  }
}
