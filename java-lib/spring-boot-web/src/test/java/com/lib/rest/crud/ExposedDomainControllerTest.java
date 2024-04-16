package com.lib.rest.crud;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

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

import com.lib.support.Main;
import com.lib.support.rest.crud.MyCrudEntity;
import com.lib.support.rest.crud.MyJpaRepository;
import com.lib.support.rest.crud.MyMapper;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = {
    Main.class,
    ExposedDomainControllerTest.class,
    MyMapper.class,
    MyJpaRepository.class,
  }
)
class ExposedDomainControllerTest {
  
  @Autowired
  MockMvc mvc;

  @SpyBean
  MyJpaRepository repository;
  
  @Test
  void should_result_http_200_with_found_resource() throws Exception {

    final var id = "1000";
    final var entity = new MyCrudEntity();
    entity.setId(1000L);
    entity.setName("Name 1000");

    doReturn(Optional.of(entity)).when(repository).findById(eq(1000L));

    mvc
      .perform(get("/v1/crud/exposed/" + id))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("1000"))
      .andExpect(jsonPath("$.name").value("Name 1000"));

  }
}
