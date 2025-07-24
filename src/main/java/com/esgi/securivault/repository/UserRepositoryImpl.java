package com.esgi.securivault.repository;

import com.esgi.securivault.entity.User;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserRepositoryImpl implements UserRepository {
    
    @Autowired
    private Firestore firestore;
    
    private static final String COLLECTION_NAME = "users";

    @Override
    public User findByemail(String email) {
        try {
            QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .get().get();
            
            for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
                return doc.toObject(User.class);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error finding user by email", e);
        }
        return null;
    }

    @Override
    public User findByFirebaseUid(String firebaseUid) {
        try {
            DocumentSnapshot document = firestore.collection(COLLECTION_NAME)
                .document(firebaseUid)
                .get().get();
            
            if (document.exists()) {
                return document.toObject(User.class);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error finding user by Firebase UID", e);
        }
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .get().get();
            
            return !querySnapshot.getDocuments().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error checking if user exists by email", e);
        }
    }

    @Override
    public boolean existsByFirebaseUid(String firebaseUid) {
        try {
            DocumentSnapshot document = firestore.collection(COLLECTION_NAME)
                .document(firebaseUid)
                .get().get();
            
            return document.exists();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error checking if user exists by Firebase UID", e);
        }
    }
    
    // Méthodes supplémentaires pour la gestion
    public User save(User user) {
        try {
            WriteResult result = firestore.collection(COLLECTION_NAME)
                .document(user.getFirebaseUid())
                .set(user)
                .get();
            
            return user;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error saving user", e);
        }
    }
}