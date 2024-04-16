package com.lib.api.crud.jpa;

import com.lib.api.crud.AbstractAwareCrudController;
import com.lib.jpa.util.SpecificationParser;
import com.lib.web.util.RHSColonQueryStringParser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Adhoc queries over jpa that may be slow due the lack of indexes for the attributes.
 */
public abstract class AbstractJpaAdhocController<TREQ, TRES, ID, E, EID>
  extends AbstractAwareCrudController<TREQ, TRES, ID, E, EID>
  implements RHSParserAware, SpecificationParserAware<E> {

  @Autowired
  protected SpecificationParser<E> parser;

  @Autowired
  protected RHSColonQueryStringParser rhs;

  @Override
  public SpecificationParser<E> parser() {
    return parser;
  }

  @Override
  public RHSColonQueryStringParser rhs() {
    return rhs;
  }
}
