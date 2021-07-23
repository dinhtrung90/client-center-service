package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Eligibility;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Eligibility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EligibilityRepository extends JpaRepository<Eligibility, String>, JpaSpecificationExecutor<Eligibility> {

    Optional<Eligibility> findByEmail(String email);

    Optional<Eligibility> findByPhone(String phone);

    Optional<Eligibility> findByEmployeeId(String employeeId);

    @Query("select e from Eligibility e left join fetch e.eligibilityMetadata m" +
        " left join fetch e.eligibilityPresentStatuses" +
        " where e.id = :id")
    Optional<Eligibility> findByPrimaryId(@Param("id") String id);

    Optional<Eligibility> findByPhoneOrEmployeeId(String phone, String employeeId);


}
