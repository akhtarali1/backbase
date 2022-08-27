package com.backbase.assesment.movies.persistance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backbase.assesment.movies.persistance.entity.AcademyAward;

/**
 * Academy award entity repository
 *
 * @author Akhtar
 */
@Repository
public interface AcademyAwardRepository extends JpaRepository<AcademyAward, Long> {

    /**
     * @param category nominated category
     * @param nominee nominee name
     * @return academy award nominee details
     */
    Optional<AcademyAward> findByCategoryAndNomineeEqualsIgnoreCase(String category, String nominee);
}
