package com.lib.api.crud;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

/**
 * Base abstract class with awareness of {@link RepositoryAware}, {@link MapperAware} and {@link ConverterAware}
 *
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <E> Repository entity type
 * @param <EID> Repository entity identity type
 */
public abstract class AbstractAwareCrudController<TREQ, TRES, ID, E, EID>
  implements
    RepositoryAware<E, EID>,
    MapperAware<TREQ, TRES, E>,
    ConverterAware<ID, EID> {

  @Autowired
  CrudRepository<E, EID> repository;

  @Autowired(required = false)
  CrudMapper<TREQ, TRES, E> mapper;

  @Autowired(required = false)
  Converter<ID, EID> converter;

  @Override
  @SuppressWarnings({ "unchecked" })
  public <R extends CrudRepository<E, EID>> R repository() {
    return (R) repository;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public CrudMapper<TREQ, TRES, E> mapper() {
    return Optional
      .ofNullable(
        Optional
          .ofNullable(
            GenericTypeResolver.resolveTypeArguments(
              getClass(),
              AbstractAwareCrudController.class
            )
          )
          .filter(types -> types.length == 5)
          .filter(types ->
            types[0].equals(types[1]) && types[1].equals(types[3])
          )
          .map(types -> (CrudMapper) new IdentityMapper())
          .orElse(mapper)
      )
      .orElseThrow(() ->
        new IllegalStateException(
          "Have different types and there is no suitable CrudMapper"
        )
      );
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Converter<ID, EID> converter() {
    return Optional
      .ofNullable(
        Optional
          .ofNullable(
            GenericTypeResolver.resolveTypeArguments(
              getClass(),
              AbstractAwareCrudController.class
            )
          )
          .filter(types -> types.length == 5)
          .filter(types -> types[2].equals(types[4]))
          .map(types -> (Converter) new IdentityConverter())
          .orElse(converter)
      )
      .orElseThrow(() ->
        new IllegalStateException(
          "IDs have different types and there is no suitable Converter"
        )
      );
  }

  /**
   * Always returns the value that was used as its argument, unchanged.
   */
  @SuppressWarnings("rawtypes")
  public static class IdentityConverter implements Converter {

    @Override
    @Nullable
    public Object convert(Object source) {
      return source;
    }
  }

  /**
   * Always returns the value that was used as its argument, unchanged.
   */
  @SuppressWarnings("rawtypes")
  public static class IdentityMapper implements CrudMapper {

    @Override
    public Object toEntity(Object request) {
      return request;
    }

    @Override
    public Object toResponse(Object entity) {
      return entity;
    }
  }
}
