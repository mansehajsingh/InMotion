package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.domains.Domain;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.domains.models.Metadata;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.ZonedDateTime;

@MappedSuperclass
public class AbstractDomain implements Domain {

    @Id
    @GeneratedValue
    protected Long id;

    @CreationTimestamp
    protected ZonedDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy
    protected SQLUser createdBy;

    @UpdateTimestamp
    protected ZonedDateTime lastUpdatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_updated_by")
    @LastModifiedBy
    protected SQLUser lastUpdatedBy;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Boolean isNew() {
        return this.id == null;
    }

    @Override
    public Metadata metadata() {
        return new Metadata(createdAt, createdBy, lastUpdatedAt, lastUpdatedBy);
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = SQLUser.fromId(createdBy.getId());
    }

    public void setLastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = SQLUser.fromId(lastUpdatedBy.getId());
    }

}
