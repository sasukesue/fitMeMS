package com.proj.fitnessclass.repository;

import com.proj.fitnessclass.domain.FitnessClass;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FitnessClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FitnessClassRepository extends JpaRepository<FitnessClass, Long> {
}
