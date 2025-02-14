package com.openclassrooms.starterjwt.unit.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Loads the Spring context for testing the mapper
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper; // Injects the TeacherMapper instance

    @Test
    void shouldMapTeacherDtoToEntity() {
        // GIVEN: A TeacherDto instance
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        // WHEN: Mapping from DTO to Entity
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // THEN: Validate the mapping result
        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
        assertThat(teacher.getCreatedAt()).isEqualTo(teacherDto.getCreatedAt());
        assertThat(teacher.getUpdatedAt()).isEqualTo(teacherDto.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherToDto() {
        // GIVEN: A Teacher instance
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        // WHEN: Mapping from Entity to DTO
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // THEN: Validate the mapping result
        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
        assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
        assertThat(teacherDto.getCreatedAt()).isEqualTo(teacher.getCreatedAt());
        assertThat(teacherDto.getUpdatedAt()).isEqualTo(teacher.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherDtoListToEntityList() {
        // GIVEN: A list of TeacherDto instances
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(3L);
        teacherDto.setFirstName("firstname");
        teacherDto.setLastName("lastname");

        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto);

        // WHEN: Mapping from DTO list to Entity list
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // THEN: Validate the mapping result
        assertThat(teacherList).isNotNull();
        assertThat(teacherList).hasSize(1);
        assertThat(teacherList.get(0).getId()).isEqualTo(teacherDtoList.get(0).getId());
    }

    @Test
    void shouldMapTeacherEntityListToDtoList() {
        // GIVEN: A list of Teacher entities
        Teacher teacher = new Teacher();
        teacher.setId(3L);
        teacher.setFirstName("firstname");
        teacher.setLastName("lastname");

        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        // WHEN: Mapping from Entity list to DTO list
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        // THEN: Validate the mapping result
        assertThat(teacherDtoList).isNotNull();
        assertThat(teacherDtoList).hasSize(1);
        assertThat(teacherDtoList.get(0).getId()).isEqualTo(teacherList.get(0).getId());
    }

    @Test
    void shouldMapTeacherDtoWithNullFieldsToEntity() {
        // GIVEN: A TeacherDto instance with null fields
        TeacherDto teacherDto = new TeacherDto();

        // WHEN: Mapping to Entity
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // THEN: Validate that the entity has null values
        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isNull();
        assertThat(teacher.getFirstName()).isNull();
        assertThat(teacher.getLastName()).isNull();
        assertThat(teacher.getCreatedAt()).isNull();
        assertThat(teacher.getUpdatedAt()).isNull();
    }

    @Test
    void shouldMapTeacherWithNullFieldsToDto() {
        // GIVEN: A Teacher instance with null fields
        Teacher teacher = new Teacher();

        // WHEN: Mapping to DTO
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // THEN: Validate that the DTO has null values
        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isNull();
        assertThat(teacherDto.getFirstName()).isNull();
        assertThat(teacherDto.getLastName()).isNull();
        assertThat(teacherDto.getCreatedAt()).isNull();
        assertThat(teacherDto.getUpdatedAt()).isNull();
    }

    @Test
    void shouldReturnEmptyList_WhenMappingEmptyDtoListToEntityList() {
        // GIVEN: An empty list of TeacherDto
        List<TeacherDto> emptyList = Collections.emptyList();

        // WHEN: Mapping to Entity list
        List<Teacher> teacherList = teacherMapper.toEntity(emptyList);

        // THEN: Validate that the result is an empty list
        assertThat(teacherList).isNotNull().isEmpty();
    }

    @Test
    void shouldReturnEmptyList_WhenMappingEmptyEntityListToDtoList() {
        // GIVEN: An empty list of Teacher entities
        List<Teacher> emptyList = Collections.emptyList();

        // WHEN: Mapping to DTO list
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(emptyList);

        // THEN: Validate that the result is an empty list
        assertThat(teacherDtoList).isNotNull().isEmpty();
    }

    @Test
    void shouldReturnNull_WhenMappingNullDtoListToEntityList() {
        // GIVEN: A null list of DTOs
        List<TeacherDto> dtoList = null;

        // WHEN: Mapping to Entity list
        List<Teacher> teacherList = teacherMapper.toEntity(dtoList);

        // THEN: Validate that the result is null
        assertThat(teacherList).isNull();
    }

    @Test
    void shouldReturnNull_WhenMappingNullEntityListToDtoList() {
        // GIVEN: A null list of Entities
        List<Teacher> entityList = null;

        // WHEN: Mapping to DTO list
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(entityList);

        // THEN: Validate that the result is null
        assertThat(teacherDtoList).isNull();
    }
}
