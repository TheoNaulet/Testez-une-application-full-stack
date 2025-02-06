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

@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void shouldMapTeacherDtoToEntity() {
        // Arrange
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        // Act
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Assert
        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
        assertThat(teacher.getCreatedAt()).isEqualTo(teacherDto.getCreatedAt());
        assertThat(teacher.getUpdatedAt()).isEqualTo(teacherDto.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherToDto() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        // Act
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Assert
        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
        assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
        assertThat(teacherDto.getCreatedAt()).isEqualTo(teacher.getCreatedAt());
        assertThat(teacherDto.getUpdatedAt()).isEqualTo(teacher.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherDtoListToEntityList() {
        // Arrange
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(3L);
        teacherDto.setFirstName("firstname");
        teacherDto.setLastName("lastname");

        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto);

        // Act
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // Assert
        assertThat(teacherList).isNotNull();
        assertThat(teacherList).hasSize(1);
        assertThat(teacherList.get(0).getId()).isEqualTo(teacherDtoList.get(0).getId());
    }

    @Test
    void shouldMapTeacherEntityListToDtoList() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(3L);
        teacher.setFirstName("firstname");
        teacher.setLastName("lastname");

        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        // Act
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        // Assert
        assertThat(teacherDtoList).isNotNull();
        assertThat(teacherDtoList).hasSize(1);
        assertThat(teacherDtoList.get(0).getId()).isEqualTo(teacherList.get(0).getId());
    }

    @Test
    void shouldMapTeacherDtoWithNullFieldsToEntity() {
        // Arrange
        TeacherDto teacherDto = new TeacherDto();

        // Act
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Assert
        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isNull();
        assertThat(teacher.getFirstName()).isNull();
        assertThat(teacher.getLastName()).isNull();
        assertThat(teacher.getCreatedAt()).isNull();
        assertThat(teacher.getUpdatedAt()).isNull();
    }

    @Test
    void shouldMapTeacherWithNullFieldsToDto() {
        // Arrange
        Teacher teacher = new Teacher();

        // Act
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Assert
        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isNull();
        assertThat(teacherDto.getFirstName()).isNull();
        assertThat(teacherDto.getLastName()).isNull();
        assertThat(teacherDto.getCreatedAt()).isNull();
        assertThat(teacherDto.getUpdatedAt()).isNull();
    }

    // ✅ Test conversion liste vide
    @Test
    void shouldReturnEmptyList_WhenMappingEmptyDtoListToEntityList() {
        // Arrange
        List<TeacherDto> emptyList = Collections.emptyList();

        // Act
        List<Teacher> teacherList = teacherMapper.toEntity(emptyList);

        // Assert
        assertThat(teacherList).isNotNull().isEmpty();
    }

    @Test
    void shouldReturnEmptyList_WhenMappingEmptyEntityListToDtoList() {
        // Arrange
        List<Teacher> emptyList = Collections.emptyList();

        // Act
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(emptyList);

        // Assert
        assertThat(teacherDtoList).isNotNull().isEmpty();
    }

    @Test
    void shouldReturnNull_WhenMappingNullDtoListToEntityList() {
        // Act
        List<Teacher> teacherList = teacherMapper.toEntity((List<TeacherDto>) null);

        // Assert
        assertThat(teacherList).isNull();
    }

    @Test
    void shouldReturnNull_WhenMappingNullEntityListToDtoList() {
        // Act
        List<TeacherDto> teacherDtoList = teacherMapper.toDto((List<Teacher>) null);

        // Assert
        assertThat(teacherDtoList).isNull();
    }
}
