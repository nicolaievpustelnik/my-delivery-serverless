package com.lib.rest.crud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lib.api.crud.UpdateController;
import com.lib.support.Main;
import com.lib.support.rest.crud.MyCrudEntity;
import com.lib.support.rest.crud.MyJpaRepository;
import com.lib.support.rest.crud.MyMapper;
import com.lib.support.rest.crud.StringToLongConverter;

import java.time.LocalDate;
import java.util.Optional;
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
    UpdateController.class,
    StringToLongConverter.class,
    MyMapper.class,
    MyJpaRepository.class,
  }
)
class UpdateControllerTest {

  @Autowired
  MockMvc mvc;

  @SpyBean
  MyJpaRepository repository;

  @Captor
  ArgumentCaptor<MyCrudEntity> myCrudEntityCaptor;

  @Test
  void should_result_http_400_on_invalid_body() throws Exception {
    var id = "1001";

    mvc
      .perform(
        put("/v1/crud/" + id)
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
  void should_result_http_404_when_id_not_found() throws Exception {
    var id = "1001";

    doReturn(Optional.empty()).when(repository).findById(eq(1001L));

    mvc
      .perform(
        put("/v1/crud/" + id)
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
      .andExpect(status().isNotFound());
  }

  @Test
  void should_result_http_204_and_update() throws Exception {

    var id = "1002";

    var entity = new MyCrudEntity();
    entity.setId(1002L);
    entity.setActive(true);
    entity.setBorn(LocalDate.of(1983, 2, 9));
    entity.setEmail("me@email.com");
    entity.setName("Me");
    entity.setScore(10.0f);
    
    doReturn(Optional.of(entity)).when(repository).findById(eq(1002L));

    mvc
      .perform(
        put("/v1/crud/" + id)
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            """
          {
            "name": "Me",
            "email":"you@gmail.com",
            "born": "1982-01-10"
          }
          """
          )
      )
      .andDo(print())
      .andExpect(status().isNoContent());

      verify(repository).save(myCrudEntityCaptor.capture());

      var actual = myCrudEntityCaptor.getValue();
      assertEquals(1002L, actual.getId());
      assertEquals("you@gmail.com", actual.getEmail());
      assertEquals(LocalDate.of(1982, 1, 10), actual.getBorn());
  }
}
