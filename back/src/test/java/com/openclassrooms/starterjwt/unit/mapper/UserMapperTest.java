package com.openclassrooms.starterjwt.unit.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Loads the Spring context to test UserMapper
class UserMapperTest {

    @Autowired
    private UserMapper userMapper; // Injects the UserMapper instance

    private User testUser;

    @BeforeEach
    void setup() {
        // GIVEN: Initializing a test User instance before each test
        testUser = new User();
        testUser.setId(10L);
        testUser.setEmail("jane.doe@test.com");
        testUser.setPassword("securePass");
        testUser.setFirstName("Jane");
        testUser.setLastName("Doe");
        testUser.setAdmin(true);
    }

    @Test
    void shouldConvertUserDtoToEntity() {
        // GIVEN: A UserDto instance
        UserDto dto = new UserDto();
        dto.setId(15L);
        dto.setEmail("alice@example.com");
        dto.setFirstName("Alice");
        dto.setLastName("Johnson");
        dto.setAdmin(false);
        dto.setPassword("mypassword");

        // WHEN: Mapping UserDto to User entity
        User mappedUser = userMapper.toEntity(dto);

        // THEN: Verify the mapping correctness
        assertThat(mappedUser).isNotNull();
        assertThat(mappedUser.getId()).isEqualTo(dto.getId());
        assertThat(mappedUser.getEmail()).isEqualTo(dto.getEmail());
        assertThat(mappedUser.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(mappedUser.getLastName()).isEqualTo(dto.getLastName());
        assertThat(mappedUser.isAdmin()).isEqualTo(dto.isAdmin());
    }

    @Test
    void shouldConvertUserEntityToDto() {
        // WHEN: Mapping User entity to UserDto
        UserDto mappedDto = userMapper.toDto(testUser);

        // THEN: Verify the mapping correctness
        assertThat(mappedDto).isNotNull();
        assertThat(mappedDto.getId()).isEqualTo(testUser.getId());
        assertThat(mappedDto.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(mappedDto.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(mappedDto.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(mappedDto.isAdmin()).isEqualTo(testUser.isAdmin());
    }

    @Test
    void shouldConvertUserDtoListToUserEntityList() {
        // GIVEN: A list of UserDto instances
        UserDto dto = new UserDto();
        dto.setId(30L);
        dto.setEmail("bob@example.com");
        dto.setFirstName("Bob");
        dto.setLastName("Smith");
        dto.setAdmin(false);
        dto.setPassword("password123");

        List<UserDto> dtoList = List.of(dto);

        // WHEN: Mapping list of DTOs to list of Entities
        List<User> userList = userMapper.toEntity(dtoList);

        // THEN: Verify correct mapping of list
        assertThat(userList).isNotNull().hasSize(1);
        assertThat(userList.get(0).getId()).isEqualTo(dto.getId());
        assertThat(userList.get(0).getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    void shouldConvertUserEntityListToUserDtoList() {
        // GIVEN: A list of User entities
        List<User> userList = List.of(testUser);

        // WHEN: Mapping list of Entities to list of DTOs
        List<UserDto> dtoList = userMapper.toDto(userList);

        // THEN: Verify correct mapping of list
        assertThat(dtoList).isNotNull().hasSize(1);
        assertThat(dtoList.get(0).getId()).isEqualTo(testUser.getId());
        assertThat(dtoList.get(0).getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void shouldReturnNullWhenUserDtoIsNull() {
        // GIVEN: A null UserDto
        UserDto dto = null;

        // WHEN: Mapping null DTO to Entity
        User mappedUser = userMapper.toEntity(dto);

        // THEN: The result should be null
        assertThat(mappedUser).isNull();
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        // GIVEN: A null User entity
        User user = null;

        // WHEN: Mapping null Entity to DTO
        UserDto mappedDto = userMapper.toDto(user);

        // THEN: The result should be null
        assertThat(mappedDto).isNull();
    }

    @Test
    void shouldReturnNullWhenUserListIsNull() {
        // GIVEN: A null list of User entities
        List<User> userList = null;

        // WHEN: Mapping null list of Entities to list of DTOs
        List<UserDto> dtoList = userMapper.toDto(userList);

        // THEN: The result should be null
        assertThat(dtoList).isNull();
    }
}
