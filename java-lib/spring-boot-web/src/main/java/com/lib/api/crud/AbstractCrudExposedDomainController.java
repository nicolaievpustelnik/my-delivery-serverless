package com.lib.api.crud;

import com.lib.model.IdentifiableEntity;

/**
 * <p>
 * Abstract base implementation to directly expose the domain entities.
 * </p>
 * 
 * <p>
 * Disclaimer: This class does not have the capability to automatically bind controllers.
 * </p>
 * 
 * @param <T> Repository entity type.
 * @param <ID> Repository entity identity type. 
 */
public abstract class AbstractCrudExposedDomainController<
  T extends IdentifiableEntity<ID>, ID
>
  extends AbstractCrudSameBodyController<T, T, ID> {}
