package com.lib.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.lib.support.Instances;
import com.lib.support.Main;
import com.lib.support.MyRepository;

@SpringBootTest(classes = {
  Main.class
})
@AutoConfigureMockMvc
class BaseFilteredJPAControllerTest {
  
  @Autowired
  MyRepository repository;

  @Autowired
  MockMvc mock;

  @BeforeEach
  void beforeEach(){
    Instances.deleteAll(repository);
  }

  @Test
  void should_respond_empty() throws Exception {

    mock.perform(get("/test")
      .param("name", "eq:Peppa"))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(0)));

  }

  @Test
  void should_respond_matched_entities_equals_symbol() throws Exception {

    // setup
    var expected = Instances.create("Charles", repository);

    // act & assert
    mock.perform(get("/test")
      .param("name", "Charles"))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].name", is(expected.getName())));

  }

  @Test
  void should_respond_matched_entities_eq() throws Exception {

    // setup
    var expected = Instances.create("Mind Flower", repository);

    // act & assert
    mock.perform(get("/test")
      .param("name", "eq:Mind Flower"))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].name", is(expected.getName())));

  }

  @Test
  void should_respond_matched_entities_gt() throws Exception {

    // setup
    Instances.create("Dustin", repository);
    Instances.create("Max", repository);

    var currentId = Instances.currentCounter();

    // act & assert
    mock.perform(get("/test")
      .param("id", "gt:" + (currentId - 2)))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[?(@.name == 'Dustin')]", hasSize(1)))
      .andExpect(jsonPath("$[?(@.name == 'Max')]", hasSize(1)));

  }

  @Test
  void should_respond_matched_entities_gte() throws Exception {

    // setup
    Instances.create("Jurassic", repository);
    Instances.create("Park", repository);

    var currentId = Instances.currentCounter();

    // act & assert
    mock.perform(get("/test")
      .param("id", "gte:" + (currentId - 1)))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[?(@.name == 'Jurassic')]", hasSize(1)))
      .andExpect(jsonPath("$[?(@.name == 'Park')]", hasSize(1)));
    
  }

  @Test
  void should_respond_matched_entities_lt() throws Exception {

    // setup
    Instances.create("Athos", repository);
    Instances.create("Porthos", repository);
    Instances.create("Aramis", repository);

    var currentId = Instances.currentCounter();

    // act & assert
    mock.perform(get("/test")
      .param("id", "lt:" + currentId))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[?(@.name == 'Athos')]", hasSize(1)))
      .andExpect(jsonPath("$[?(@.name == 'Porthos')]", hasSize(1)));
  }

  @Test
  void should_respond_matched_entities_lte() throws Exception {

    // setup
    Instances.create("Athos", repository);
    Instances.create("Porthos", repository);
    Instances.create("Aramis", repository);

    var currentId = Instances.currentCounter();

    // act & assert
    mock.perform(get("/test")
      .param("id", "lte:" + currentId))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(3)))
      .andExpect(jsonPath("$[?(@.name == 'Athos')]", hasSize(1)))
      .andExpect(jsonPath("$[?(@.name == 'Porthos')]", hasSize(1)))
      .andExpect(jsonPath("$[?(@.name == 'Aramis')]", hasSize(1)));
  }

  @Test
  void should_respond_400_error_on_invalid_operator() throws Exception {

    mock.perform(get("/test")
      .param("id", "in:(10,30)"))
      .andExpect(status().isBadRequest());
  }
}
