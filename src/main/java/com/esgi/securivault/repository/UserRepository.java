package com.esgi.securivault.repository;

import com.esgi.securivault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Rechercher un utilisateur par email
    Optional<User> findByEmail(String email);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

    // Trouver tous les utilisateurs qui ont accès à une valise spécifique
    @Query("SELECT u FROM User u JOIN u.suitcases s WHERE s.id = :suitcaseId")
    List<User> findAllBySuitcaseId(@Param("suitcaseId") Long suitcaseId);

    // Trouver tous les utilisateurs avec un niveau d'accès spécifique pour une valise
    @Query("SELECT u FROM User u " +
            "JOIN UserSuitcase us ON us.user = u " +
            "WHERE us.suitcase.id = :suitcaseId " +
            "AND us.accessLevel = :accessLevel")
    List<User> findAllBySuitcaseIdAndAccessLevel(
            @Param("suitcaseId") Long suitcaseId,
            @Param("accessLevel") String accessLevel
    );
}