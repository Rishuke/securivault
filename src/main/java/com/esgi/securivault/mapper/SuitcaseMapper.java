package com.esgi.securivault.mapper;

import com.esgi.securivault.dto.SuitcaseDTO;
import com.esgi.securivault.entity.Suitcase;

class SuitcaseMapper {
    public static SuitcaseDTO toDto(Suitcase suitcase) {
        if (suitcase == null) return null;
        SuitcaseDTO dto = new SuitcaseDTO();
        dto.setId(suitcase.getId());
        dto.setName(suitcase.getName());
        dto.setisLocked(suitcase.isLocked());  // Uses isLocked()
        dto.setisOn(suitcase.isOn());           // Uses isOn()
        dto.setCode(suitcase.getCode());      // Assuming code is also part of the entity
        dto.setisMooving(suitcase.isMooving()); // Assuming isMooving is also part of the entity
        return dto;
    }

    public static Suitcase toEntity(SuitcaseDTO dto) {
        if (dto == null) return null;
        Suitcase suitcase = new Suitcase();
        suitcase.setId(dto.getId());
        suitcase.setName(dto.getName());
        suitcase.setLocked(dto.isLocked());  // Uses isLocked()
        suitcase.setisOn(dto.isOn());          // Uses isOn()
        suitcase.setCode(dto.getCode());     // Assuming code is also part of the DTO
        suitcase.setisMooving(dto.isMooving()); // Assuming isMooving is also part of the DTO
        return suitcase;
    }
}