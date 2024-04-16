package com.lib.api.crud;

import com.lib.model.IdentifiableEntity;

/**
 * Abstract base controller with separated types for request, response and IDs.
 * 
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <E> Repository entity type
 * @param <EID> Repository entity identity type
 */
public abstract class AbstractCrudControllerBindable<
  TREQ, TRES, ID, E extends IdentifiableEntity<EID>, EID
>
  extends AbstractAwareCrudController<TREQ, TRES, ID, E, EID>
  implements
    CreateControllerBindable<TREQ, TRES, ID, E, EID>,
    UpdateControllerBindable<TREQ, TRES, ID, E, EID>,
    ReadAllControllerBindable<TREQ, TRES, E, EID>,
    ReadControllerBindable<TREQ, TRES, ID, E, EID>,
    DeleteControllerBindable<TREQ, TRES, ID, E, EID> {}
