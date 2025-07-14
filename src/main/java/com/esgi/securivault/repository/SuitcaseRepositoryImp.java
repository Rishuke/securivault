package com.esgi.securivault.repository;


import org.springframework.stereotype.Service;

import com.esgi.securivault.entity.Suitcase;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class SuitcaseRepositoryImp implements SuitcaseRepository {

    @Autowired
    private Firestore firestore;

    public Suitcase findSuitcaseByUserId(String userId) {
        try {
            QuerySnapshot querySnapshot = firestore.collection("suitcases")
                    .whereArrayContains("userIds", userId)
                    .get().get();
            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                return querySnapshot.getDocuments().get(0).toObject(Suitcase.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Suitcase findById(String suitcaseId) {
        try {
            DocumentSnapshot documentSnapshot = firestore.collection("suitcases")
                    .document(suitcaseId)
                    .get().get();
            if (documentSnapshot != null && documentSnapshot.exists()) {
                return documentSnapshot.toObject(Suitcase.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void save(Suitcase suitcase) {
        firestore.collection("suitcases").document(suitcase.getId()).set(suitcase);
    }
    public void deleteById(String suitcaseId) {
        firestore.collection("suitcases").document(suitcaseId).delete();
    }
}
