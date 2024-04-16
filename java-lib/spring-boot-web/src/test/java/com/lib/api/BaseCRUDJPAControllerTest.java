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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.support.Instances;
import com.lib.support.Main;
import com.lib.support.MyRepository;

@SpringBootTest(classes = {
  Main.class
})
@AutoConfigureMockMvc
class BaseCRUDJPAControllerTest {
  
  @Autowired
  MyRepository repository;

  @Autowired
  MockMvc mock;

  @Autowired
  ObjectMapper json;

  @Nested
  @DisplayName("GetAll method")
  class GetAll {

    @BeforeEach
    void beforeEach(){
      Instances.deleteAll(repository);
    }

    @Test
    void should_respond_all_entities() throws Exception {

      // setup
      Instances.create("deb", repository);
      Instances.create("ian", repository);

      // act & assert
      mock.perform(get("/test").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void should_respond_the_entity() throws Exception {

      // setup
      var actual = Instances.create("deb", repository);

      // act & assert
      mock.perform(get("/test").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].name", is(actual.getName())));

    }

    @Test
    void should_respond_empty() throws Exception {

      mock.perform(
        get("/test")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(0)));

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
    void should_respond_not_found() throws Exception {

      mock.perform(
        get("/test/99")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());

    }

    @Test
    void should_respond_the_entity() throws Exception {

      // setup
      var expected = Instances.create("michael", repository);

      // act & assert
      mock.perform(get("/test/" + expected.getId()).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(expected.getName())));
    }
  }

  @Nested
  @DisplayName("Post method")
  class Post {

    @BeforeEach
    void beforeEach(){
      Instances.deleteAll(repository);
    }

    @Test
    void should_save_the_entity() throws Exception {

      // setup
      var expected = Instances.createTransient("mary");

      // act
      mock.perform(post("/test")
        .content(json.writeValueAsString(expected))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(expected.getName())));

      // assert
      mock.perform(get("/test/" + expected.getId()).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(expected.getName())));

      // cleanup
      Instances.delete(expected.getId(), repository);

    }

    @Test
    void should_respond_400_on_invalid_body() throws Exception {

      // setup
      var invalid = Instances.createTransient("  ");

      // act & assert
      mock.perform(post("/test")
        .content(json.writeValueAsString(invalid))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
      
    }
  }

  @Nested
  @DisplayName("Put method")
  class Put {

    @Test
    void should_respond_not_found() throws Exception {

      var entity = Instances.createTransient("mike");

      // act & assert
      mock.perform(put("/test/" + entity.getId())
        .content(json.writeValueAsString(entity))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    void should_respond_400_on_invalid_body() throws Exception {

      // setup
      var invalid = Instances.createTransient("  ");

      // act & assert
      mock.perform(put("/test/" + invalid.getId())
        .content(json.writeValueAsString(invalid))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_the_entity() throws Exception {

      // setup
      var expected = Instances.createTransient("eleven");

      var toSave = Instances.createTransient("el");
      toSave.setId(expected.getId());

      mock.perform(post("/test")
        .content(json.writeValueAsString(toSave))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(toSave.getName())));

      // act
      mock.perform(put("/test/" + expected.getId())
        .content(json.writeValueAsString(expected))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNoContent());

      // assert
      mock.perform(get("/test/" + expected.getId()).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(expected.getName())));

      // cleanup
      Instances.delete(expected.getId(), repository);

    }

    @Test
    void should_respond_405_when_not_id_in_the_path() throws Exception {

      var entity = Instances.createTransient("lucas");

      // act & assert
      mock.perform(put("/test")
        .content(json.writeValueAsString(entity))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
    }
  }

  @Nested
  @DisplayName("Delete method")
  class Delete {

    @Test
    void should_delete_the_entity() throws Exception {

      var toSave = Instances.createTransient("will");

      mock.perform(post("/test")
        .content(json.writeValueAsString(toSave))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(toSave.getName())));

      // act
      mock.perform(delete("/test/" + toSave.getId())
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNoContent());

      // assert
      mock.perform(
        get("/test/" + toSave.getId())
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    void should_respond_not_found() throws Exception {

      var entity = Instances.createTransient("mike");

      // act & assert
      mock.perform(delete("/test/" + entity.getId())
        .content(json.writeValueAsString(entity))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    void should_respond_405_when_not_id_in_the_path() throws Exception {

      var entity = Instances.createTransient("lucas");

      // act & assert
      mock.perform(delete("/test")
        .content(json.writeValueAsString(entity))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
    }

  }
}
