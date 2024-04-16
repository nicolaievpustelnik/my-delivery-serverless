package com.lib.api.crud;

import com.lib.model.IdentifiableEntity;

/**
 * <p>
 * Abstract base implementation with same type for request, response and IDs.
 * </p>
 * 
 * <p>
 * Disclaimer: This class does not have the capability to automatically bind controllers.
 * </p>
 * 
 * @param <D> Requets and Response type
 * @param <T> Repository entity type
 * @param <ID> Repository entity identity type
 */
public abstract class AbstractCrudSameBodyController<
  D, T extends IdentifiableEntity<ID>, ID
>
  extends AbstractCrudController<D, D, ID, T, ID> {}
