package com.lib.rest.crud;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lib.api.crud.CreateController;
import com.lib.support.Main;
import com.lib.support.rest.crud.MyCrudEntity;
import com.lib.support.rest.crud.MyJpaRepository;
import com.lib.support.rest.crud.MyMapper;
import com.lib.support.rest.crud.StringToLongConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
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
    CreateController.class,
    StringToLongConverter.class,
    MyMapper.class,
    MyJpaRepository.class,
  }
)
class CreateControllerTest {

  @Autowired
  MockMvc mvc;

  @SpyBean
  MyJpaRepository repository;

  @Captor
  ArgumentCaptor<MyCrudEntity> myCrudEntityCaptor;

  @Test
  void should_result_http_400_on_invalid_body() throws Exception {
    mvc
      .perform(
        post("/v1/crud")
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            """
          {
            "name": "the name",
            "email":"me@email.com",
            "born": ""
          }
          """
          )
      )
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void should_result_http_200_on_success() throws Exception {

    mvc
      .perform(
        post("/v1/crud")
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            """
          {
            "name": "the name",
            "email":"me@email.com",
            "born": "1983-02-09"
          }
          """
          )
      )
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").exists());

  }
}
