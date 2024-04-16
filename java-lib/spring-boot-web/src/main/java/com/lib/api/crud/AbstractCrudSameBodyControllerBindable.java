package com.lib.api.crud;

import com.lib.model.IdentifiableEntity;

/**
 * Abstract base controller with same type for request, response and IDs.
 * 
 * @param <D> Requets and Response type
 * @param <T> Repository entity type
 * @param <ID> Repository entity identity type
 */
public abstract class AbstractCrudSameBodyControllerBindable <
  D, T extends IdentifiableEntity<ID>, ID
>
  extends AbstractCrudControllerBindable<D, D, ID, T, ID> {}
