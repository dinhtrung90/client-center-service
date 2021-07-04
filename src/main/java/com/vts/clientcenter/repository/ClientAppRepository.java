package com.vts.clientcenter.repository;

import com.okta.sdk.client.Client;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.ClientApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Spring Data JPA repository for the {@link ClientApp} entity.
 */
@Repository
public interface ClientAppRepository extends JpaRepository<ClientApp, String>, JpaSpecificationExecutor<ClientApp> {

    Optional<ClientApp> findByName(String roleName);

    Stream<ClientApp> findByIdIn(List<String> ids);
}
