package com.esgi.securivault.services;

import com.esgi.securivault.entity.Suitcase;
import com.esgi.securivault.repository.SuitcaseRepository;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SuitcaseService {

    @Autowired
    private SuitcaseRepository suitcaseRepository;

    @Autowired
    private Firestore firestore;

    public Suitcase createSuitcase(String name, String userId) {
        Suitcase suitcase = new Suitcase();
        suitcase.setName(name);
        suitcase.setId(userId);
        suitcase.setLocked(true);
        suitcase.setisOn(false);
        suitcase.setCode("0000");
        suitcase.setColor("red");
        suitcase.setMotionSensitivity(20.2);
        suitcase.setVolume(20);

        suitcaseRepository.save(suitcase);
        firestore.collection("suitcases").document(userId).set(suitcase);
        return suitcase;
    }

    public void deleteSuitcase(String suitcaseId) {
        suitcaseRepository.deleteById(suitcaseId);
        firestore.collection("suitcases").document(suitcaseId).delete();
    }

    public void updateSuitcase(String suitcaseId, String name, boolean locked, boolean on) {
        Suitcase suitcase = null;
        try {
            suitcase = suitcaseRepository.findById(suitcaseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        suitcase.setName(name);
        suitcase.setLocked(locked);
        suitcase.setisOn(on);
        suitcaseRepository.save(suitcase);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("locked", locked);
        updates.put("on", on);
        firestore.collection("suitcases").document(suitcaseId).update(updates);
    }

    public Suitcase verifCode(String suitcaseId, String inputCode) throws Exception {
        DocumentSnapshot snapshot = firestore.collection("suitcases").document(suitcaseId).get().get();
        if (!snapshot.exists()) {
            throw new RuntimeException("Suitcase not found");
        }
        Suitcase suitcase = snapshot.toObject(Suitcase.class);
        if (!suitcase.getCode().equals(inputCode)) {
            suitcase.setLocked(true);
            firestore.collection("suitcases").document(suitcaseId).set(suitcase);
            throw new RuntimeException("Invalid code");
        }
        suitcase.setLocked(false);
        firestore.collection("suitcases").document(suitcaseId).set(suitcase);
        return suitcase;
    }

    public Suitcase getSuitcaseById(String suitcaseId) {
        Suitcase suitcase = null;
        try{
            suitcase = suitcaseRepository.findById(suitcaseId);
                    
        }catch (Exception e) {
            e.printStackTrace();
        }
        return suitcase;
    }

    public Suitcase changeCode(String suitcaseId, String newCode) {
        Suitcase suitcase = null;
        try{
            suitcase = suitcaseRepository.findById(suitcaseId); 
                    
        }catch (Exception e) {
            e.printStackTrace();
        }

        suitcase.setCode(newCode);
        suitcaseRepository.save(suitcase);
        firestore.collection("suitcases").document(suitcaseId).update("code", newCode);
        return suitcase;
    }

    public Suitcase getSuitcaseByUserId(String userId) {
        Suitcase s = null;
        try {
            s = suitcaseRepository.findSuitcaseByUserId(userId); // .get() bloque jusqu’à résultat
            
        } catch (Exception e) {
            e.printStackTrace();
        }   
        return s;
    }
    public Suitcase updateBuzzerVolume(String suitcaseId, int volume) {
        Suitcase suitcase = suitcaseRepository.findById(suitcaseId);
        suitcase.setVolume(volume);
        suitcaseRepository.save(suitcase);
        firestore.collection("suitcases").document(suitcaseId).update("buzzer_freq", volume);
        return suitcase;
    }

    public Suitcase updateLedColor(String suitcaseId, String color) {
        Suitcase suitcase = suitcaseRepository.findById(suitcaseId);
        suitcase.setColor(color);  // Gardez setColor() si c'est le nom dans votre entité
        suitcaseRepository.save(suitcase);
        // Les deux doivent utiliser le même nom de champ
        firestore.collection("suitcases").document(suitcaseId).update("led_color", color);
        return suitcase;
    }

    public Suitcase updateMotionSensitivity(String suitcaseId, double sensitivity) {
        Suitcase suitcase = suitcaseRepository.findById(suitcaseId);
        suitcase.setMotionSensitivity(sensitivity);
        suitcaseRepository.save(suitcase);
        firestore.collection("suitcases").document(suitcaseId).update("motionSensitivity", sensitivity);
        return suitcase;
    }
}
