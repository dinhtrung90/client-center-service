package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.UserProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String>, JpaSpecificationExecutor<UserProfile> {}