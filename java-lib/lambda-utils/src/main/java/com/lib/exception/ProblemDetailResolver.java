package com.lib.exception;

import java.util.List;

public class ProblemDetailResolver extends AbstractProblemDetailResolver {

  public ProblemDetailResolver(List<AbstractExceptionHandler<? extends Throwable>> exceptionHandlers) {
    super(exceptionHandlers);

  }

}
