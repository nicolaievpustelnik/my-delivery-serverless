package com.lib.support;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface MyRepository extends JpaRepositoryImplementation<MyEntity, Integer> {
  
}
