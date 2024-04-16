package com.lib.api.crud.jpa;

import com.lib.model.IdentifiableEntity;

/**
 * Base abstract class with same type for request, response and IDs
 *
 * @param <D> Requets and Response type
 * @param <T> Repository entity type
 * @param <ID> Repository entity identity type
 */
public abstract class AbstractJpaAdhocProgrammaticCrudSameBodyController<
  D, T extends IdentifiableEntity<ID>, ID
>
  extends AbstractJpaAdhocProgrammaticCrudController<D, D, ID, T, ID> {}
