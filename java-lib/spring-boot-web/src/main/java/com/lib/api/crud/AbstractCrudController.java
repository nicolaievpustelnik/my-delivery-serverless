package com.lib.api.crud;

import com.lib.model.IdentifiableEntity;

/**
 * <p>
 * Abstract base implementation with separated types for request, response and IDs.
 * <p>
 * 
 * <p>
 * Disclaimer: This class does not have the capability to automatically bind controllers.
 * </p>
 * 
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <E> Repository entity type
 * @param <EID> Repository entity identity type
 */
public abstract class AbstractCrudController<
  TREQ, TRES, ID, E extends IdentifiableEntity<EID>, EID
>
  extends AbstractAwareCrudController<TREQ, TRES, ID, E, EID>
  implements
    CreateController<TREQ, TRES, ID, E, EID>,
    UpdateController<TREQ, TRES, ID, E, EID>,
    ReadAllController<TREQ, TRES, E, EID>,
    ReadController<TREQ, TRES, ID, E, EID>,
    DeleteController<TREQ, TRES, ID, E, EID> {}
