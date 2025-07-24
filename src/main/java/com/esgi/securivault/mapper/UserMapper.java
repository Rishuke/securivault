// package com.esgi.securivault.mapper;

// import com.esgi.securivault.dto.UserDTO;
// import com.esgi.securivault.entity.User;

// class UserMapper{
//     public static UserDTO toDto(User user) {
//         if (user == null) {
//             return null;
//         }
//         UserDTO userDto = new UserDTO();
//         userDto.setUserId(user.getUserId());
//         userDto.setEmail(user.getEmail());
//         userDto.setPassword(user.getPassword());
//         return userDto;
//     }

//     public static User toEntity(UserDTO userDto) {
//         if (userDto == null) {
//             return null;
//         }
//         User user = new User();
//         user.setUserId(userDto.getUserId());
//         user.setEmail(userDto.getEmail());
//         user.setPassword(userDto.getPassword());
//         return user;
//     }
// }