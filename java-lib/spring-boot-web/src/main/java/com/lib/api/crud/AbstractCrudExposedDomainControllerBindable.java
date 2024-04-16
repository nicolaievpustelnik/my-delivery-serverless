package com.lib.api.crud;

import com.lib.model.IdentifiableEntity;

/**
 * Abstract base controller to directly expose the domain entities.
 *
 * @param <T> Repository entity type.
 * @param <ID> Repository entity identity type. 
 */
public abstract class AbstractCrudExposedDomainControllerBindable<
  T extends IdentifiableEntity<ID>, ID
>
  extends AbstractCrudSameBodyControllerBindable<T, T, ID> {}
