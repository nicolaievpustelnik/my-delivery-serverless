package com.lib.api.crud.jpa;

import com.lib.api.crud.CreateControllerBindable;
import com.lib.api.crud.DeleteControllerBindable;
import com.lib.api.crud.ReadControllerBindable;
import com.lib.api.crud.UpdateControllerBindable;
import com.lib.model.IdentifiableEntity;

/**
 * <p>
 * Adhoc queries over jpa that may be slow due the lack of indexes for the attributes.
 * </p>
 * <p>
 * Crud operations
 * </p>
 * <p>
 * Your repository must implements the {@link JpaRepositoryImplementation} interface.
 * </p>
 * <p>
 * To avoid name collision, the params for pagination are <code>@page</code> for page number and <code>@size</code> for page size.
 * </p>
 */
public abstract class AbstractJpaAdhocCrudController<
  TREQ, TRES, ID, E extends IdentifiableEntity<EID>, EID
>
  extends AbstractJpaAdhocController<TREQ, TRES, ID, E, EID>
  implements
    CreateControllerBindable<TREQ, TRES, ID, E, EID>,
    UpdateControllerBindable<TREQ, TRES, ID, E, EID>,
    ReadControllerBindable<TREQ, TRES, ID, E, EID>,
    DeleteControllerBindable<TREQ, TRES, ID, E, EID>,
    JpaAdhocController<TREQ, TRES, ID, E, EID> {}
