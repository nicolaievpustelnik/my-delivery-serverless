package com.lib.rest.crud;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lib.api.crud.ReadAllController;
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
    ReadAllController.class,
    StringToLongConverter.class,
    MyMapper.class,
    MyJpaRepository.class,
  }
)
class ReadAllControllerTest {

  @Autowired
  MockMvc mvc;

  @SpyBean
  MyJpaRepository repository;

  @BeforeEach
  void beforeEach() {
    repository.deleteAll();
  }

  @Test
  void should_result_http_200_and_empty_when_no_data() throws Exception {
    mvc
      .perform(get("/v1/crud").queryParam("page", "1").queryParam("size", "10"))
      .andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  void should_result_http_400_on_invalid_page() throws Exception {
    mvc
      .perform(
        get("/v1/crud").queryParam("page", "-1").queryParam("size", "10")
      )
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void should_result_http_400_on_invalid_size() throws Exception {
    mvc
      .perform(get("/v1/crud").queryParam("page", "1").queryParam("size", "0"))
      .andDo(print())
      .andExpect(status().isBadRequest());
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
  void should_result_http_200_and_paged() throws Exception {
    createEntities(10);

    mvc
      .perform(get("/v1/crud").queryParam("page", "0").queryParam("size", "5"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value("5"))
      .andExpect(jsonPath("$.totalPages").value("2"))
      .andExpect(jsonPath("$.totalElements").value("10"))
      .andExpect(jsonPath("$.number").value("0"))
      .andExpect(jsonPath("$.numberOfElements").value("5"))
      .andExpect(jsonPath("$.first").value("true"));

    mvc
      .perform(get("/v1/crud").queryParam("page", "1").queryParam("size", "5"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value("5"))
      .andExpect(jsonPath("$.totalPages").value("2"))
      .andExpect(jsonPath("$.totalElements").value("10"))
      .andExpect(jsonPath("$.number").value("1"))
      .andExpect(jsonPath("$.numberOfElements").value("5"))
      .andExpect(jsonPath("$.first").value("false"));
  }

  @Test
  void should_result_http_200_and_paged_even_without_page_and_size() throws Exception {
    createEntities(300);

    mvc
      .perform(get("/v1/crud"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value("100"))
      .andExpect(jsonPath("$.totalPages").value("3"))
      .andExpect(jsonPath("$.totalElements").value("290"))
      .andExpect(jsonPath("$.number").value("0"))
      .andExpect(jsonPath("$.numberOfElements").value("100"))
      .andExpect(jsonPath("$.first").value("true"));

    mvc
      .perform(get("/v1/crud").queryParam("page", "1"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value("100"))
      .andExpect(jsonPath("$.totalPages").value("3"))
      .andExpect(jsonPath("$.totalElements").value("290"))
      .andExpect(jsonPath("$.number").value("1"))
      .andExpect(jsonPath("$.numberOfElements").value("100"))
      .andExpect(jsonPath("$.first").value("false"));
  }
}
