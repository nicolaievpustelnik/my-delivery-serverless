package com.lib.web.util;

import com.lib.model.OperatorType;
import com.lib.model.QueryFilter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * To parse query strings like: {@code ?name=eq:John&age=lt:30}. Will translated as: name EQUALS TO John AND age LESS THAN OR EQUAL TO 30
 *
 */
@Component
public class RHSColonQueryStringParser {

  public static final String PAGE_NUMBER_PARAM = "@page";
  public static final String PAGE_SIZE_PARAM = "@size";

  /**
   * Read about {@link https://www.moesif.com/blog/technical/api-design/REST-API-Design-Filtering-Sorting-and-Pagination/#rhs-colon}
   * @param queryString Formatted as {@code name=eq:Jonh&age=lte:30}
   * @throws MalformedQueryStringException When the query string does not follow the pattern or is empty
   * @throws NullPointerException When the query string is null
   */
  public List<QueryFilter> parse(final String queryString) {
    var queryList = Optional
      .ofNullable(queryString)
      .filter(Objects::nonNull)
      .map(String::trim)
      .filter(q -> !q.isBlank())
      .map(q -> q.split("&"))
      .map(List::of)
      .orElseThrow(() -> new MalformedQueryStringException("query is empty"));

    try {
      return parse(
        queryList
          .stream()
          .map(q -> q.split("="))
          .collect(Collectors.toMap(q -> q[0], q -> q[1]))
      );
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new MalformedQueryStringException("query is invalid");
    }
  }

  private void validate(Map<String, String> query) {
    Optional
      .ofNullable(query)
      .map(Map::size)
      .filter(s -> s > 0)
      .orElseThrow(() -> new MalformedQueryStringException("query is empty"));
  }

  public boolean containsSomeOperator(String value) {
    for (var symbol : OperatorType.values()) {
      if (value.contains(symbol.name() + ":")) {
        return true;
      }
    }

    return false;
  }

  /**
   * @param query Query string splitted by {@code &} separator
   * @throws MalformedQueryStringException When the query string does not follow the pattern or is empty
   * @throws NullPointerException When the query string is null
   */
  public List<QueryFilter> parse(final Map<String, String> query) {
    validate(Objects.requireNonNull(query));

    return query
      .entrySet()
      .stream()
      .filter(kv -> !kv.getKey().equals(PAGE_NUMBER_PARAM))
      .filter(kv -> !kv.getKey().equals(PAGE_SIZE_PARAM))
      .map(kv -> Map.entry(kv.getKey().trim(), kv.getValue().trim()))
      .peek(kv -> {
        if (kv.getValue().isBlank()) {
          throw new MalformedQueryStringException("no value");
        }

        if (kv.getKey().isBlank()) {
          throw new MalformedQueryStringException("no field name");
        }
      })
      .map(kv -> parse(kv.getKey(), kv.getValue()))
      .collect(Collectors.toList());
  }

  /**
   * To cleanup the query.
   * @param query
   * @return New map without unnecessary entries.
   */
  public Map<String, String> sanitize(Map<String, String> query) {
    return query
      .entrySet()
      .stream()
      .filter(kv -> !kv.getKey().equals(PAGE_NUMBER_PARAM))
      .filter(kv -> !kv.getKey().equals(PAGE_SIZE_PARAM))
      .map(kv -> Map.entry(kv.getKey().trim(), kv.getValue().trim()))
      .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  /**
   * @param field The field name. Example {@code name}
   * @param value The raw value that may contain the operator. Example {@code eq:John}
   * @throws NullPointerException When field or value are null
   */
  public QueryFilter parse(String field, String value) {
    // no operator, means equals by default

    var operator = "eq";
    var queryValue = value;
    if (containsSomeOperator(value)) {
      var firstColon = value.indexOf(":");
      operator = value.substring(0, firstColon);
      queryValue = value.substring(firstColon + 1);
    }

    return QueryFilter
      .builder()
      .field(field)
      .operator(OperatorType.valueOf(operator))
      .value(queryValue)
      .build();
  }
}
