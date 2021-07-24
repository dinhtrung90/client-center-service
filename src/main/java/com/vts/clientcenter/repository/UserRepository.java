package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.User;
import java.util.List;
import java.util.Optional;

import com.vts.clientcenter.domain.UserProfile;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    Optional<User> findOneByEmailIgnoreCase(String email);

    @Query("select u from User u " +
        "left join fetch u.userProfile " +
        " left join fetch u.userAddresses " +
        "where u.id =:userId")
    Optional<User> findByUserId(@Param("userId") String userId);

    @Query("select u from User u left " +
        "join fetch u.userProfile " +
        "left join fetch u.authorities " +
        "where u.id =:userId")
    Optional<User> findByUserIdEagerAuthority(@Param("userId") String userId);


    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from UserOrganizationMembership m where m.organization.id = ?1")
    int deleteBulkByOrganizationIdentifier(String organizationId);

}
