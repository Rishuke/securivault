package com.esgi.securivault.services;

import com.esgi.securivault.entity.User;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServices {

    @Autowired
    private Firestore firestore;

    public UserServices(Firestore firestore) {
        this.firestore = firestore;
    }

    public User findByEmail(String email) {
        try {
            QuerySnapshot querySnapshot = firestore.collection("users")
                .whereEqualTo("email", email)
                .get().get();
            for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
                return doc.toObject(User.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
