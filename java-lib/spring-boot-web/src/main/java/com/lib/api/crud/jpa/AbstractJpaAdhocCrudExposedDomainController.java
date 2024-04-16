package com.lib.api.crud.jpa;

import com.lib.model.IdentifiableEntity;

/**
 * Abstract base class to directly expose the domain entities.
 *
 * @param <T> Repository entity type.
 * @param <ID> Repository entity identity type.
 */
public abstract class AbstractJpaAdhocCrudExposedDomainController<
  T extends IdentifiableEntity<ID>, ID
>
  extends AbstractJpaAdhocCrudSameBodyController<T, T, ID> {}
