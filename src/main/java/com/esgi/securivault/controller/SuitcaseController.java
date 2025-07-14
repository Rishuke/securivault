package com.esgi.securivault.controller;

import com.esgi.securivault.entity.Suitcase;
import com.esgi.securivault.services.SuitcaseService;
import org.springframework.web.bind.annotation.*;

import com.esgi.securivault.dto.ChangeCodeDTO;

import java.util.Map;

@RestController
@RequestMapping("/suitcases")
public class SuitcaseController {

    private final SuitcaseService suitcaseService;

    
    public SuitcaseController(SuitcaseService suitcaseService) {
        this.suitcaseService = suitcaseService;
    }

    // ✅ Créer une valise
    @PostMapping("/create")
    public Suitcase createSuitcase(@RequestBody Suitcase suitcase) {
        return suitcaseService.createSuitcase(suitcase.getName(), suitcase.getId());
    }

    // ✅ Mettre à jour une valise
    @PutMapping("/{id}/update")
    public void updateSuitcase(@PathVariable String id, @RequestBody Suitcase suitcase) {
        suitcaseService.updateSuitcase(id, suitcase.getName(), suitcase.isLocked(), suitcase.isOn());
    }

    // ✅ Vérifier le code
    @PostMapping("/{id}/verify-code")
    public Suitcase verifyCode(@PathVariable String id, @RequestBody Map<String, String> payload) throws Exception {
        String code = payload.get("code");
        return suitcaseService.verifCode(id, code);
    }

    // ✅ Changer le code
    @PutMapping("/{id}/change-code")
    public Suitcase changeCode(@PathVariable String id, @RequestBody ChangeCodeDTO changeCodeDTO) {
        return suitcaseService.changeCode(id, changeCodeDTO.getNewCode());
    }

    // ✅ Supprimer une valise
    @DeleteMapping("/{id}")
    public void deleteSuitcase(@PathVariable String id) {
        suitcaseService.deleteSuitcase(id);
    }

    // ✅ Récupérer une valise par ID
    @GetMapping("/{id}")
    public Suitcase getSuitcaseById(@PathVariable String id) {
        return suitcaseService.getSuitcaseById(id);
    }
    @GetMapping("/user/{userId}")
    public Suitcase getSuitcaseByUserId(@PathVariable String userId) {
        return suitcaseService.getSuitcaseByUserId(userId);
    }
}
