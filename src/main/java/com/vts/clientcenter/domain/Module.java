package com.vts.clientcenter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tv_modules")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Module {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;
}
