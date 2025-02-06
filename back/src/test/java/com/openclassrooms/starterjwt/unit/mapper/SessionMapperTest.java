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

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldMapSessionDtoToSession() {
        Teacher teacher = new Teacher();
        teacher.setId(3L);

        User user = new User();
        user.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(List.of(user.getId()));

        when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(userService.findById(anyLong())).thenReturn(user);

        Session mappedSession = sessionMapper.toEntity(sessionDto);

        assertThat(mappedSession).isNotNull();
        assertThat(mappedSession.getId()).isEqualTo(sessionDto.getId());
        assertThat(mappedSession.getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(mappedSession.getTeacher()).isEqualTo(teacher);
        assertThat(mappedSession.getUsers()).containsExactly(user);
    }

    @Test
    void shouldReturnNullWhenSessionHasNoTeacher() {
        Session session = new Session();
        session.setTeacher(null);
    
        SessionDto sessionDto = sessionMapper.toDto(session);
    
        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getTeacher_id()).isNull(); 
    }
    

    @Test
    void shouldMapSessionToSessionDto() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user = new User();
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        session.setDescription("Test Session");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        SessionDto mappedDto = sessionMapper.toDto(session);

        assertThat(mappedDto).isNotNull();
        assertThat(mappedDto.getId()).isEqualTo(session.getId());
        assertThat(mappedDto.getDescription()).isEqualTo(session.getDescription());
        assertThat(mappedDto.getTeacher_id()).isEqualTo(session.getTeacher().getId());
        assertThat(mappedDto.getUsers()).containsExactly(user.getId());
    }

    @Test
    void shouldMapSessionDtoListToSessionList() {
        Teacher teacher = new Teacher();
        teacher.setId(3L);

        User user = new User();
        user.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(List.of(user.getId()));

        when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(userService.findById(anyLong())).thenReturn(user);

        List<Session> mappedSessions = sessionMapper.toEntity(List.of(sessionDto));

        assertThat(mappedSessions).isNotNull();
        assertThat(mappedSessions).hasSize(1);
        assertThat(mappedSessions.get(0).getId()).isEqualTo(sessionDto.getId());
        assertThat(mappedSessions.get(0).getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(mappedSessions.get(0).getTeacher()).isEqualTo(teacher);
        assertThat(mappedSessions.get(0).getUsers()).containsExactly(user);
    }

    @Test
    void shouldMapEmptySessionDtoListToEmptySessionList() {
        List<SessionDto> dtoList = Collections.emptyList();

        List<Session> sessions = sessionMapper.toEntity(dtoList);

        assertThat(sessions).isNotNull();
        assertThat(sessions).isEmpty();
    }


    @Test
    void shouldMapSessionListToSessionDtoList() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user = new User();
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        session.setDescription("Test Session");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        List<SessionDto> mappedDtos = sessionMapper.toDto(List.of(session));

        assertThat(mappedDtos).isNotNull();
        assertThat(mappedDtos).hasSize(1);
        assertThat(mappedDtos.get(0).getId()).isEqualTo(session.getId());
        assertThat(mappedDtos.get(0).getDescription()).isEqualTo(session.getDescription());
        assertThat(mappedDtos.get(0).getTeacher_id()).isEqualTo(session.getTeacher().getId());
        assertThat(mappedDtos.get(0).getUsers()).containsExactly(user.getId());
    }


    @Test
    void shouldReturnNullWhenTeacherIsNull() throws Exception {
        // Given
        Session session = new Session();
        session.setTeacher(null);
    
        // Accéder à la méthode privée via réflexion
        Method sessionTeacherIdMethod = SessionMapperImpl.class.getDeclaredMethod("sessionTeacherId", Session.class);
        sessionTeacherIdMethod.setAccessible(true); 
    
        // When
        Long teacherId = (Long) sessionTeacherIdMethod.invoke(new SessionMapperImpl(), session);
    
        // Then
        assertThat(teacherId).isNull();
    }

    @Test
void shouldReturnNullWhenDtoListIsNull() {
    // Given
    List<SessionDto> dtoList = null;

    // When
    List<Session> result = sessionMapper.toEntity(dtoList);

    // Then
    assertThat(result).isNull();
}

@Test
void shouldReturnNullWhenSessionDtoIsNull() {
    // Given
    SessionDto sessionDto = null;

    // When
    Session result = sessionMapper.toEntity(sessionDto);

    // Then
    assertThat(result).isNull();
}

@Test
void shouldReturnNullWhenSessionIsNull() {
    // Given
    Session session = null;

    // When
    SessionDto result = sessionMapper.toDto(session);

    // Then
    assertThat(result).isNull();
}

@Test
void shouldReturnNullWhenEntityListIsNull() {
    // Given
    List<Session> entityList = null;

    // When
    List<SessionDto> result = sessionMapper.toDto(entityList);

    // Then
    assertThat(result).isNull();
}
}