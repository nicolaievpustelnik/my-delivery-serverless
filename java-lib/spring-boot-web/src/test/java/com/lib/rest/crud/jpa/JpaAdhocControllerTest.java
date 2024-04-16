package com.lib.rest.crud.jpa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lib.api.crud.jpa.JpaAdhocController;
import com.lib.support.Main;
import com.lib.support.rest.crud.MyCrudEntity;
import com.lib.support.rest.crud.MyJpaRepository;
import com.lib.support.rest.crud.MyMapper;
import com.lib.support.rest.crud.StringToLongConverter;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = {
    Main.class,
    JpaAdhocController.class,
    StringToLongConverter.class,
    MyMapper.class,
    MyJpaRepository.class,
  }
)
class JpaAdhocControllerTest {

  @Autowired
  MockMvc mvc;

  @SpyBean
  MyJpaRepository repository;

  @BeforeEach
  void beforeEach() {
    repository.deleteAll();
  }

  private void createEntities(int size) {
    for (int i = 0; i < size; i++) {
      var e = new MyCrudEntity();
      e.setActive(true);
      e.setBorn(LocalDate.now());
      e.setEmail("me@email.com");
      e.setId(i + 1L);
      e.setName("Name " + i);
      e.setScore(10.0f);

      repository.save(e);
    }
  }

  @Test
  void should_result_http_200_whith_all_when_filter_empty() throws Exception {
    createEntities(10);

    mvc
      .perform(
        get("/v1/crud/adhoc").queryParam("@page", "0").queryParam("@size", "10")
      )
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value("10"))
      .andExpect(jsonPath("$.totalPages").value("1"))
      .andExpect(jsonPath("$.totalElements").value("10"))
      .andExpect(jsonPath("$.number").value("0"))
      .andExpect(jsonPath("$.numberOfElements").value("10"))
      .andExpect(jsonPath("$.first").value("true"));
  }

  @Test
  void should_result_http_200_when_no_matched_data() throws Exception {
    createEntities(10);

    mvc
      .perform(
        get("/v1/crud/adhoc")
          .queryParam("@page", "0")
          .queryParam("@size", "10")
          .queryParam("name", "eq:Turin")
      )
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value("0"));
  }

  @Test
  void should_result_http_200_filtered() throws Exception {
    createEntities(10);

    var e = new MyCrudEntity();
    e.setActive(true);
    e.setBorn(LocalDate.now());
    e.setEmail("me@email.com");
    e.setId(10010L);
    e.setName("Ozzy");
    e.setScore(10.0f);
    repository.save(e);

    e.setActive(true);
    e.setBorn(LocalDate.now());
    e.setEmail("me@email.com");
    e.setId(10010L);
    e.setName("John");
    e.setScore(10.0f);
    repository.save(e);

    mvc
      .perform(
        get("/v1/crud/adhoc")
          .queryParam("@page", "0")
          .queryParam("@size", "10")
          .queryParam("name", "eq:Ozzy")
      )
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value("1"));
  }
}
