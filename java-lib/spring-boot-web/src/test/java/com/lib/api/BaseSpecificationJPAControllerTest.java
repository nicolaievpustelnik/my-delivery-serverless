package com.lib.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class BaseSpecificationJPAControllerTest {
  
  @Autowired
  MyRepository repository;

  @Autowired
  MockMvc mock;

  @Nested
  @DisplayName("GetAll method")
  class GetAll {
    
    @BeforeEach
    void beforeEach(){
      Instances.deleteAll(repository);
    }

    @Test
    void should_respond_empty() throws Exception {

      mock.perform(
        get("/even")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void should_respond_matched_even_entities() throws Exception {

      // setup
      Instances.counterToEven();
      Instances.create("Marie", repository); //odd
      Instances.create("Ada", repository); //even
      Instances.create("Alice", repository); //odd
      Instances.create("Grace", repository); //even

      // act & assert
      mock.perform(
        get("/even")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[?(@.name == 'Ada')]", hasSize(1)))
        .andExpect(jsonPath("$[?(@.name == 'Grace')]", hasSize(1)));
    }

    @Test
    void should_respond_matched_odd_entities() throws Exception {

      // setup
      Instances.counterToOdd();
      Instances.create("Mayana", repository); //even
      Instances.create("Enedina", repository); //odd
      Instances.create("Mellanie", repository); //even
      Instances.create("Katemari", repository); //odd

      // act & assert
      mock.perform(
        get("/odd")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[?(@.name == 'Enedina')]", hasSize(1)))
        .andExpect(jsonPath("$[?(@.name == 'Katemari')]", hasSize(1)));
    }
  }

  @Nested
  @DisplayName("Get method")
  class Get {

    @BeforeEach
    void beforeEach(){
      Instances.deleteAll(repository);
    }

    @Test
    void should_respond_404() throws Exception {

      mock.perform(
        get("/even/100")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());

    }

    @Test
    void should_respond_with_found_entity() throws Exception {

      // setup
      Instances.counterToEven();
      Instances.create("odd", repository);
      var expected = Instances.create("Santos", repository);

      // act & assert
      mock.perform(get("/even/" + expected.getId())
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(expected.getName())));
    }
  }
}
