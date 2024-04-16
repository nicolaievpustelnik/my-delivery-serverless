package com.lib.api.crud.jpa;

import com.lib.api.crud.CreateController;
import com.lib.api.crud.DeleteController;
import com.lib.api.crud.ReadAllController;
import com.lib.api.crud.ReadController;
import com.lib.api.crud.UpdateController;
import com.lib.model.IdentifiableEntity;

/**
 * Adhoc Jpa query using programmatic {@link org.springframework.data.jpa.domain.Specification}.
 * and custom request filters.
 *
 * <p>
 * Crud operations.
 * </p>
 * <p>
 * Warning! Que queries may be slow due the lack of indexes for the attributes.
 * </p>
 * <p>
 * Your repository must implements the {@link JpaRepositoryImplementation} interface.
 * </p>
 * <p>
 * To avoid name collision, the params for pagination are <code>@page</code> for page number and <code>@size</code> for page size.
 * </p>
 *
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <RT> Repository entity type
 * @param <RID> Repository entity identity type
 */
public abstract class AbstractJpaAdhocProgrammaticCrudController<
  TREQ, TRES, ID, E extends IdentifiableEntity<EID>, EID
>
  extends AbstractJpaAdhocController<TREQ, TRES, ID, E, EID>
  implements
    CreateController<TREQ, TRES, ID, E, EID>,
    UpdateController<TREQ, TRES, ID, E, EID>,
    ReadAllController<TREQ, TRES, E, EID>,
    ReadController<TREQ, TRES, ID, E, EID>,
    DeleteController<TREQ, TRES, ID, E, EID>,
    JpaAdhocProgrammaticController<TREQ, TRES, ID, E, EID> {}
