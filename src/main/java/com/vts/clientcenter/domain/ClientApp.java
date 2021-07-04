package com.vts.clientcenter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tv_client_app")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClientApp extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Size(max = 50)
    @Column(name = "app_name", length = 50)
    private String name;

    @Column(name = "app_desc")
    private String desc;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
        name = "tv_client_authority",
        joinColumns = { @JoinColumn(name = "client_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
    )
    private Set<Authority> authorities = new HashSet<>();

    public void  addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public ClientApp  removeAuthority(Authority authority) {
        this.authorities.remove(authority);
        return this;
    }
}
