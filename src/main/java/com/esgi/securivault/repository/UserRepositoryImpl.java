package com.esgi.securivault.repository;

import com.esgi.securivault.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {
    @Override
    public User findByemail(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'findByemail'");
    }
}