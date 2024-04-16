package com.lib.support.rest.crud;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface MyJpaRepository
  extends JpaRepositoryImplementation<MyCrudEntity, Long> {}
