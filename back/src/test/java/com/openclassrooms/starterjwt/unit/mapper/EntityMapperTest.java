package com.openclassrooms.starterjwt.unit.mapper;


import org.junit.jupiter.api.BeforeEach;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.mapper.EntityMapper;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

// Implémentation fictive pour tester EntityMapper
class TestEntityMapper implements EntityMapper<TestDto, TestEntity> {

    @Override
    public TestEntity toEntity(TestDto dto) {
        return new TestEntity(dto.getId(), dto.getName());
    }

    @Override
    public TestDto toDto(TestEntity entity) {
        return new TestDto(entity.getId(), entity.getName());
    }


    @Override
    public List<TestEntity> toEntity(List<TestDto> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
    
    @Override
    public List<TestDto> toDto(List<TestEntity> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
    
}

// DTO et Entity pour le test
class TestDto {
    private Long id;
    private String name;

    public TestDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}

class TestEntity {
    private Long id;
    private String name;

    public TestEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}

// Classe de test unitaire
class EntityMapperTest {

    private TestEntityMapper testMapper;

    @BeforeEach
    void setUp() {
        testMapper = new TestEntityMapper();
    }

    @Test
    void shouldMapDtoToEntity() {
        TestDto dto = new TestDto(1L, "Test Name");

        TestEntity entity = testMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getName()).isEqualTo(dto.getName());
    }

    @Test
    void shouldMapEntityToDto() {
        TestEntity entity = new TestEntity(2L, "Another Test");

        TestDto dto = testMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getName()).isEqualTo(entity.getName());
    }

    @Test
    void shouldMapDtoListToEntityList() {
        List<TestDto> dtoList = List.of(
                new TestDto(1L, "Alice"),
                new TestDto(2L, "Bob")
        );

        List<TestEntity> entityList = testMapper.toEntity(dtoList);

        assertThat(entityList).isNotNull().hasSize(2);
        assertThat(entityList.get(0).getName()).isEqualTo("Alice");
        assertThat(entityList.get(1).getName()).isEqualTo("Bob");
    }

    @Test
    void shouldMapEntityListToDtoList() {
        List<TestEntity> entityList = List.of(
                new TestEntity(3L, "Charlie"),
                new TestEntity(4L, "David")
        );

        List<TestDto> dtoList = testMapper.toDto(entityList);

        assertThat(dtoList).isNotNull().hasSize(2);
        assertThat(dtoList.get(0).getName()).isEqualTo("Charlie");
        assertThat(dtoList.get(1).getName()).isEqualTo("David");
    }
}
