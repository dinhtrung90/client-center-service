package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.UserAddress;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long>, JpaSpecificationExecutor<UserAddress> {
    List<UserAddress> findAllByUserId(String userId);

    void deleteAllByIdNotIn(List<Long> ides);
    void deleteAllByIdIn(List<Long> ides);
}
