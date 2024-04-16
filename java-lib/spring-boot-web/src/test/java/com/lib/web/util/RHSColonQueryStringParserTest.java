package com.lib.web.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lib.model.OperatorType;
import com.lib.model.QueryFilter;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RHSColonQueryStringParserTest {

  @Test
  void should_throws_when_query_map_is_empty() {
    // setup
    var query = Map.<String, String>of();
    var parser = new RHSColonQueryStringParser();

    // act
    var ex = assertThrows(
      MalformedQueryStringException.class,
      () -> parser.parse(query)
    );

    // assert
    assertEquals("query is empty", ex.getMessage());
  }

  @Test
  void should_throws_when_query_string_is_empty() {
    // setup
    var query = "    ";
    var parser = new RHSColonQueryStringParser();

    // act
    var ex = assertThrows(
      MalformedQueryStringException.class,
      () -> parser.parse(query)
    );

    // assert
    assertEquals("query is empty", ex.getMessage());
  }

  @Test
  void should_throws_on_invalid_query_string() {
    // setup
    var query = "invalidQuery";
    var parser = new RHSColonQueryStringParser();

    // act
    var ex = assertThrows(
      MalformedQueryStringException.class,
      () -> parser.parse(query)
    );

    // assert
    assertEquals("query is invalid", ex.getMessage());
  }

  @Test
  @DisplayName("Should parse: field=value")
  void should_parse_eq_without_eq_operator() {
    // setup
    var query = Map.of("name", "John");
    var expected = List.of(
      QueryFilter
        .builder()
        .field("name")
        .operator(OperatorType.eq)
        .value("John")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should parse: field=eq:stringValue")
  void should_parse_eq_to_string() {
    // setup
    var query = Map.of("name", "eq:John");
    var expected = List.of(
      QueryFilter
        .builder()
        .field("name")
        .operator(OperatorType.eq)
        .value("John")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should parse: field=eq:90.88")
  void should_parse_eq_to_number() {
    // setup
    var query = Map.of("price", "eq:90.88");
    var expected = List.of(
      QueryFilter
        .builder()
        .field("price")
        .operator(OperatorType.eq)
        .value("90.88")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should parse: field=eq:2010-01-01")
  void should_parse_eq_to_date() {
    // setup
    var query = Map.of("birth", "eq:2010-01-01");
    var expected = List.of(
      QueryFilter
        .builder()
        .field("birth")
        .operator(OperatorType.eq)
        .value("2010-01-01")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should parse: field=eq:12:00:00")
  void should_parse_eq_to_time() {
    // setup
    var query = Map.of("untilTime", "eq:12:00:00");
    var expected = List.of(
      QueryFilter
        .builder()
        .field("untilTime")
        .operator(OperatorType.eq)
        .value("12:00:00")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should parse: field=eq:2010-01-01T12:00:00")
  void should_parse_eq_datetime() {
    // setup
    var query = Map.of("logTimestamp", "eq:2010-01-01T12:00:00");
    var expected = List.of(
      QueryFilter
        .builder()
        .field("logTimestamp")
        .operator(OperatorType.eq)
        .value("2010-01-01T12:00:00")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should parse: field=lt:99.888")
  void should_parse_lt_for_float() {
    // setup
    var query = Map.of("weight", "lt:99.888");
    var expected = List.of(
      QueryFilter
        .builder()
        .field("weight")
        .operator(OperatorType.lt)
        .value("99.888")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should parse: field=lt:78889")
  void should_parse_lt_for_integer() {
    // setup
    var query = Map.of("bytes", "lt:78889");
    var expected = List.of(
      QueryFilter
        .builder()
        .field("bytes")
        .operator(OperatorType.lt)
        .value("78889")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  void should_throws_when_empty_map_key() {
    // setup
    var query = Map.of(" ", "eq:John");
    var parser = new RHSColonQueryStringParser();

    // act
    var ex = assertThrows(
      MalformedQueryStringException.class,
      () -> parser.parse(query)
    );

    // assert
    assertEquals("no field name", ex.getMessage());
  }

  @Test
  void should_throws_when_empty_map_value() {
    // setup
    var query = Map.of("field", "    ");
    var parser = new RHSColonQueryStringParser();

    // act
    var ex = assertThrows(
      MalformedQueryStringException.class,
      () -> parser.parse(query)
    );

    // assert
    assertEquals("no value", ex.getMessage());
  }

  @Test
  @DisplayName("Should parse: name=John&birth=gte:1983-01-01&weight=lte:70.9")
  void should_parse_many_queries() {

    // setup
    var query = "name=John&birth=gte:1983-01-01&weight=lte:70.9";
    var expected = List.of(
      QueryFilter
        .builder()
        .field("name")
        .operator(OperatorType.eq)
        .value("John")
        .build(),
      QueryFilter.builder()
        .field("birth")
        .operator(OperatorType.gte)
        .value("1983-01-01")
        .build(),
      QueryFilter.builder()
        .field("weight")
        .operator(OperatorType.lte)
        .value("70.9")
        .build()
    );
    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should parse: @page=0&@size=100&name=John")
  void should_ignore_page_number_and_page_size() {

    var query = "@page=0&@size=100&name=John";
    
    var expected = List.of(
      QueryFilter
        .builder()
        .field("name")
        .operator(OperatorType.eq)
        .value("John")
        .build()
    );

    var parser = new RHSColonQueryStringParser();

    // act
    var actual = parser.parse(query);

    // asset
    assertEquals(expected, actual);
  }
}
