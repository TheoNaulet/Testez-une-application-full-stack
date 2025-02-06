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

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    void setup() {
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
        UserDto dto = new UserDto();
        dto.setId(15L);
        dto.setEmail("alice@example.com");
        dto.setFirstName("Alice");
        dto.setLastName("Johnson");
        dto.setAdmin(false);
        dto.setPassword("mypassword");

        User mappedUser = userMapper.toEntity(dto);

        assertThat(mappedUser).isNotNull();
        assertThat(mappedUser.getId()).isEqualTo(dto.getId());
        assertThat(mappedUser.getEmail()).isEqualTo(dto.getEmail());
        assertThat(mappedUser.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(mappedUser.getLastName()).isEqualTo(dto.getLastName());
        assertThat(mappedUser.isAdmin()).isEqualTo(dto.isAdmin());
    }

    @Test
    void shouldConvertUserEntityToDto() {
        UserDto mappedDto = userMapper.toDto(testUser);

        assertThat(mappedDto).isNotNull();
        assertThat(mappedDto.getId()).isEqualTo(testUser.getId());
        assertThat(mappedDto.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(mappedDto.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(mappedDto.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(mappedDto.isAdmin()).isEqualTo(testUser.isAdmin());
    }

    @Test
    void shouldConvertUserDtoListToUserEntityList() {
        UserDto dto = new UserDto();
        dto.setId(30L);
        dto.setEmail("bob@example.com");
        dto.setFirstName("Bob");
        dto.setLastName("Smith");
        dto.setAdmin(false);
        dto.setPassword("password123");

        List<UserDto> dtoList = List.of(dto);
        List<User> userList = userMapper.toEntity(dtoList);

        assertThat(userList).isNotNull().hasSize(1);
        assertThat(userList.get(0).getId()).isEqualTo(dto.getId());
        assertThat(userList.get(0).getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    void shouldConvertUserEntityListToUserDtoList() {
        List<User> userList = List.of(testUser);
        List<UserDto> dtoList = userMapper.toDto(userList);

        assertThat(dtoList).isNotNull().hasSize(1);
        assertThat(dtoList.get(0).getId()).isEqualTo(testUser.getId());
        assertThat(dtoList.get(0).getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void shouldReturnNullWhenUserDtoIsNull() {
        UserDto dto = null;
        User mappedUser = userMapper.toEntity(dto);
        assertThat(mappedUser).isNull();
    }
    @Test
    void shouldReturnNullWhenUserIsNull() {
        User user = null;
        UserDto mappedDto = userMapper.toDto(user);
        assertThat(mappedDto).isNull();
    }

    @Test
    void shouldReturnNullWhenUserListIsNull() {
        List<User> userList = null;
        List<UserDto> dtoList = userMapper.toDto(userList);
        assertThat(dtoList).isNull();
    }
}
