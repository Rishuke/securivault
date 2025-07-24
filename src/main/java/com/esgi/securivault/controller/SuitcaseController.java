package com.esgi.securivault.controller;

import com.esgi.securivault.entity.Suitcase;
import com.esgi.securivault.services.SuitcaseService;
import org.springframework.web.bind.annotation.*;

import com.esgi.securivault.dto.ChangeCodeDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/suitcases")
public class SuitcaseController {

    private final SuitcaseService suitcaseService;

    
    public SuitcaseController(SuitcaseService suitcaseService) {
        this.suitcaseService = suitcaseService;
    }

    //  Créer une valise
    @PostMapping("/create")
    public Suitcase createSuitcase(@RequestBody Suitcase suitcase) {
        return suitcaseService.createSuitcase(suitcase.getName(), suitcase.getId());
    }

    //  Mettre à jour une valise
    // @PutMapping("/{id}/update")
    // public void updateSuitcase(@PathVariable String id, @RequestBody Suitcase suitcase) {
    //     suitcaseService.updateSuitcase(id, suitcase.getName(), suitcase.isLocked(), suitcase.isOn());
    // }

    // //  Vérifier le code
    // @PostMapping("/{id}/verify-code")
    // public Suitcase verifyCode(@PathVariable String id, @RequestBody Map<String, String> payload) throws Exception {
    //     String code = payload.get("code");
    //     return suitcaseService.verifCode(id, code);
    // }

    // Changer le code
    @PutMapping("/{id}/change-code")
    public Suitcase changeCode(@PathVariable String id, @RequestBody ChangeCodeDTO changeCodeDTO) {
        return suitcaseService.changeCode(id, changeCodeDTO.getNewCode());
    }

    //  Supprimer une valise
    @DeleteMapping("/{id}")
    public void deleteSuitcase(@PathVariable String id) {
        suitcaseService.deleteSuitcase(id);
    }

    //  Récupérer une valise par ID
    // @GetMapping("/{id}")
    // public Suitcase getSuitcaseById(@PathVariable String id) {
    //     return suitcaseService.getSuitcaseById(id);
    // }
    // @GetMapping("/user/{userId}")
    // public Suitcase getSuitcaseByUserId(@PathVariable String userId) {
    //     return suitcaseService.getSuitcaseByUserId(userId);
    // }
    @PutMapping("/{id}/buzzer")
    public Suitcase updateBuzzerVolume(@PathVariable String id, @RequestBody Map<String, Integer> payload) {
        return suitcaseService.updateBuzzerVolume(id, payload.get("buzzer_freq"));
    }

    @PutMapping("/{id}/led-color")
    public Suitcase updateLedColor(@PathVariable String id, @RequestBody Map<String, String> payload) {
        return suitcaseService.updateLedColor(id, payload.get("led_color"));  // Changé de "color" à "led_color"
    }

    @PutMapping("/{id}/sensitivity")
    public Suitcase updateMotionSensitivity(@PathVariable String id, @RequestBody Map<String, Double> payload) {
        return suitcaseService.updateMotionSensitivity(id, payload.get("sensitivity"));
    }

    @PutMapping("/{id}/add-user")
    public Suitcase addUserToSuitcase(@PathVariable String id, @RequestBody String email) {
        return suitcaseService.addUserSuitcase(id, email);
    }

    @GetMapping("/{id}/code")
    public Map<String, String> getCode(@PathVariable String id) {
        return suitcaseService.getCode(id);
    }

}
