package com.lib.jpa.util;

import static org.springframework.data.jpa.domain.Specification.where;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.lib.model.QueryFilter;

/**
 * Used to create a specification to generate a query via JPA based on a list of {@link QueryFilter}
 * 
 * @param <T> Type of the entity that you want to query.
 */
@ConditionalOnClass(name = {
  "javax.persistence.criteria.Expression",
  "org.springframework.data.jpa.domain.Specification"
})
@Component
public class SpecificationParser<T> {

  /**
   * To converts a {@link QueryFilter} into a {@link Predicate} depending on the operator: eq ->
   * equal; gt -> greaterThan; gte -> greaterThanOrEqualTo; lt -> lessThan; lte -> lessThanOrEqualTo
   * 
   * @param queryFilter
   * @return
   */
  public Specification<T> parse(QueryFilter queryFilter) {

    switch (queryFilter.getOperator()) {
      case eq:
        return (root, query, criteriaBuilder) -> criteriaBuilder
            .equal(root.get(queryFilter.getField()), queryFilter.getValue());
      case gt:
        return (root, query, criteriaBuilder) -> criteriaBuilder
            .greaterThan(root.get(queryFilter.getField()), queryFilter.getValue());
      case gte:
        return (root, query, criteriaBuilder) -> criteriaBuilder
            .greaterThanOrEqualTo(root.get(queryFilter.getField()), queryFilter.getValue());
      case lt:
        return (root, query, criteriaBuilder) -> criteriaBuilder
            .lessThan(root.get(queryFilter.getField()), queryFilter.getValue());
      default:
        return (root, query, criteriaBuilder) -> criteriaBuilder
            .lessThanOrEqualTo(root.get(queryFilter.getField()), queryFilter.getValue());
    }
  }

  /**
   * To converts each {@link QueryFilter} into a {@link Predicate} depending on the operator: eq ->
   * equal; gt -> greaterThan; gte -> greaterThanOrEqualTo; lt -> lessThan; lte -> lessThanOrEqualTo
   * 
   * @param queryFilters
   * @return
   */
  public Specification<T> parse(List<QueryFilter> queryFilters) {
    Specification<T> specification = where(parse(queryFilters.remove(0)));
    for (QueryFilter queryFilter : queryFilters) {
      specification = specification.and(parse(queryFilter));
    }
    return specification;
  }
}
