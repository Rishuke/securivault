package com.esgi.securivault.repository;

import com.esgi.securivault.model.Suitcase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuitcaseRepository extends JpaRepository<Suitcase, Long> {

    // Trouver une valise par son code
    Optional<Suitcase> findByCode(String code);

    // Trouver toutes les valises d'un utilisateur
    @Query("SELECT s FROM Suitcase s JOIN s.users u WHERE u.id = :userId")
    List<Suitcase> findAllByUserId(@Param("userId") Long userId);

    // Trouver toutes les valises où l'utilisateur a un niveau d'accès spécifique
    @Query("SELECT s FROM Suitcase s " +
            "JOIN UserSuitcase us ON us.suitcase = s " +
            "WHERE us.user.id = :userId " +
            "AND us.accessLevel = :accessLevel")
    List<Suitcase> findAllByUserIdAndAccessLevel(
            @Param("userId") Long userId,
            @Param("accessLevel") String accessLevel
    );

    // Trouver les valises dans un périmètre géographique
    @Query("SELECT s FROM Suitcase s " +
            "WHERE s.latitude BETWEEN :minLat AND :maxLat " +
            "AND s.longitude BETWEEN :minLong AND :maxLong")
    List<Suitcase> findAllInArea(
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLong") Double minLong,
            @Param("maxLong") Double maxLong
    );

    // Trouver les valises déverrouillées
    List<Suitcase> findByIsLockedFalse();

    // Trouver les valises avec détection de mouvement
    List<Suitcase> findByMotionDetectedTrue();
}