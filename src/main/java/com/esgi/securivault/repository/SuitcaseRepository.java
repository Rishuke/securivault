package com.esgi.securivault.repository;



import com.esgi.securivault.entity.Suitcase;



public interface SuitcaseRepository{

    Suitcase findById(String suitcaseId);

    void save(Suitcase suitcase);

    void deleteById(String suitcaseId);

    Suitcase findSuitcaseByUserId(String userId);

}