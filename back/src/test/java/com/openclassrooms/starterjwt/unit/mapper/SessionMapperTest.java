package com.openclassrooms.starterjwt.unit.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Enables Mockito for testing
class SessionMapperTest {

    @Mock
    private TeacherService teacherService; // Mock for TeacherService

    @Mock
    private UserService userService; // Mock for UserService

    @InjectMocks
    private SessionMapperImpl sessionMapper; // Injects dependencies into SessionMapperImpl

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mock objects
    }

    @Test
    void shouldMapSessionDtoToSession() {
        // GIVEN: A SessionDto with valid data
        Teacher teacher = new Teacher();
        teacher.setId(3L);

        User user = new User();
        user.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(List.of(user.getId()));

        // Mocking service behavior
        when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(userService.findById(anyLong())).thenReturn(user);

        // WHEN: Converting DTO to Entity
        Session mappedSession = sessionMapper.toEntity(sessionDto);

        // THEN: Verify correct mapping
        assertThat(mappedSession).isNotNull();
        assertThat(mappedSession.getId()).isEqualTo(sessionDto.getId());
        assertThat(mappedSession.getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(mappedSession.getTeacher()).isEqualTo(teacher);
        assertThat(mappedSession.getUsers()).containsExactly(user);
    }

    @Test
    void shouldReturnNullWhenSessionHasNoTeacher() {
        // GIVEN: A Session with no teacher assigned
        Session session = new Session();
        session.setTeacher(null);

        // WHEN: Converting to DTO
        SessionDto sessionDto = sessionMapper.toDto(session);

        // THEN: Verify teacher_id is null
        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getTeacher_id()).isNull(); 
    }

    @Test
    void shouldMapSessionToSessionDto() {
        // GIVEN: A Session object with teacher and users
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user = new User();
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        session.setDescription("Test Session");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        // WHEN: Converting to DTO
        SessionDto mappedDto = sessionMapper.toDto(session);

        // THEN: Verify correct mapping
        assertThat(mappedDto).isNotNull();
        assertThat(mappedDto.getId()).isEqualTo(session.getId());
        assertThat(mappedDto.getDescription()).isEqualTo(session.getDescription());
        assertThat(mappedDto.getTeacher_id()).isEqualTo(session.getTeacher().getId());
        assertThat(mappedDto.getUsers()).containsExactly(user.getId());
    }

    @Test
    void shouldMapSessionDtoListToSessionList() {
        // GIVEN: A list of SessionDto objects
        Teacher teacher = new Teacher();
        teacher.setId(3L);

        User user = new User();
        user.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(List.of(user.getId()));

        // Mocking services
        when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(userService.findById(anyLong())).thenReturn(user);

        // WHEN: Converting list of DTOs to list of entities
        List<Session> mappedSessions = sessionMapper.toEntity(List.of(sessionDto));

        // THEN: Verify correct mapping
        assertThat(mappedSessions).isNotNull();
        assertThat(mappedSessions).hasSize(1);
        assertThat(mappedSessions.get(0).getId()).isEqualTo(sessionDto.getId());
        assertThat(mappedSessions.get(0).getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(mappedSessions.get(0).getTeacher()).isEqualTo(teacher);
        assertThat(mappedSessions.get(0).getUsers()).containsExactly(user);
    }

    @Test
    void shouldMapEmptySessionDtoListToEmptySessionList() {
        // GIVEN: An empty list of SessionDto
        List<SessionDto> dtoList = Collections.emptyList();

        // WHEN: Converting to entity list
        List<Session> sessions = sessionMapper.toEntity(dtoList);

        // THEN: The result should be an empty list
        assertThat(sessions).isNotNull();
        assertThat(sessions).isEmpty();
    }

    @Test
    void shouldMapSessionListToSessionDtoList() {
        // GIVEN: A list of Session entities
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user = new User();
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        session.setDescription("Test Session");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        // WHEN: Converting to DTO list
        List<SessionDto> mappedDtos = sessionMapper.toDto(List.of(session));

        // THEN: Verify correct mapping
        assertThat(mappedDtos).isNotNull();
        assertThat(mappedDtos).hasSize(1);
        assertThat(mappedDtos.get(0).getId()).isEqualTo(session.getId());
        assertThat(mappedDtos.get(0).getDescription()).isEqualTo(session.getDescription());
        assertThat(mappedDtos.get(0).getTeacher_id()).isEqualTo(session.getTeacher().getId());
        assertThat(mappedDtos.get(0).getUsers()).containsExactly(user.getId());
    }

    @Test
    void shouldReturnNullWhenTeacherIsNull() throws Exception {
        // GIVEN: A Session without a teacher
        Session session = new Session();
        session.setTeacher(null);

        // Access private method using reflection
        Method sessionTeacherIdMethod = SessionMapperImpl.class.getDeclaredMethod("sessionTeacherId", Session.class);
        sessionTeacherIdMethod.setAccessible(true); 

        // WHEN: Invoking the method
        Long teacherId = (Long) sessionTeacherIdMethod.invoke(new SessionMapperImpl(), session);

        // THEN: Verify teacherId is null
        assertThat(teacherId).isNull();
    }

    @Test
    void shouldReturnNullWhenDtoListIsNull() {
        // GIVEN: A null list of DTOs
        List<SessionDto> dtoList = null;

        // WHEN: Converting null list
        List<Session> result = sessionMapper.toEntity(dtoList);

        // THEN: Result should be null
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullWhenSessionDtoIsNull() {
        // GIVEN: A null DTO
        SessionDto sessionDto = null;

        // WHEN: Converting to entity
        Session result = sessionMapper.toEntity(sessionDto);

        // THEN: Result should be null
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullWhenSessionIsNull() {
        // GIVEN: A null session
        Session session = null;

        // WHEN: Converting to DTO
        SessionDto result = sessionMapper.toDto(session);

        // THEN: Result should be null
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullWhenEntityListIsNull() {
        // GIVEN: A null list of entities
        List<Session> entityList = null;

        // WHEN: Converting to DTO list
        List<SessionDto> result = sessionMapper.toDto(entityList);

        // THEN: Result should be null
        assertThat(result).isNull();
    }
}
