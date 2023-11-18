package kz.almaty.moneytransferservice.mapper;

import kz.almaty.moneytransferservice.dto.UserDto;
import kz.almaty.moneytransferservice.model.User;

import java.math.BigDecimal;

public class UserMapper {
    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .accountNumber(user.getAccountNumber())
                .accountBalance(user.getAccountBalance())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .accountNumber(userDto.getAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userDto.getEmail())
                .createdAt(userDto.getCreatedAt())
                .updatedAt(userDto.getUpdatedAt())
                .build();
    }
}
