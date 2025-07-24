package com.esgi.securivault.repository;




import com.esgi.securivault.entity.User;


public interface UserRepository  {
    User findByemail(String email);

    User findByFirebaseUid(String firebaseId);

    boolean existsByEmail(String email);

    boolean existsByFirebaseUid(String firebaseId);
}

