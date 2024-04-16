package com.lib.rest.crud;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.lib.api.crud.DeleteController;
import com.lib.support.Main;
import com.lib.support.rest.crud.MyCrudEntity;
import com.lib.support.rest.crud.MyJpaRepository;
import com.lib.support.rest.crud.MyMapper;
import com.lib.support.rest.crud.StringToLongConverter;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = {
    Main.class,
    DeleteController.class,
    StringToLongConverter.class,
    MyMapper.class,
    MyJpaRepository.class,
  }
)
class DeleteControllerTest {
  
  @Autowired
  MockMvc mvc;

  @SpyBean
  MyJpaRepository repository;

  @Captor
  ArgumentCaptor<MyCrudEntity> myCrudEntityCaptor;
  
  @Test
  void should_result_http_404_when_not_found() throws Exception {

    final var id = "900";

    mvc
      .perform(delete("/v1/crud/" + id))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  void should_result_http_204_and_delete() throws Exception {

    var id = "1004";

    var entity = new MyCrudEntity();
    entity.setId(1004L);
    entity.setActive(true);
    entity.setBorn(LocalDate.of(1983, 2, 9));
    entity.setEmail("me@email.com");
    entity.setName("Me");
    entity.setScore(10.0f);
    
    doReturn(Optional.of(entity)).when(repository).findById(eq(1004L));

    mvc
      .perform(
        delete("/v1/crud/" + id)
      )
      .andExpect(status().isNoContent());

    verify(repository).delete(myCrudEntityCaptor.capture());
    var actual = myCrudEntityCaptor.getValue();

    assertNotNull(actual);
    assertEquals(1004L, actual.getId());
  }
}
