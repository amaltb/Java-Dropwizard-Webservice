package com.ab.example.metastore.commons.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author _amal
 *
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "created_on", updatable = false)
    protected Timestamp createdOn = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_on")
    protected Timestamp updatedOn = new Timestamp(System.currentTimeMillis());
}