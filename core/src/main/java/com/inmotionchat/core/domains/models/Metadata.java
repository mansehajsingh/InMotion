package com.inmotionchat.core.domains.models;

import com.inmotionchat.core.domains.User;

import java.time.ZonedDateTime;

public class Metadata {

    public final ZonedDateTime createdAt;

    public final User createdBy;

    public final ZonedDateTime lastUpdatedAt;

    public final User lastUpdatedBy;

    public Metadata(ZonedDateTime createdAt, User createdBy, ZonedDateTime lastUpdatedAt, User lastUpdatedBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastUpdatedAt = lastUpdatedAt;
        this.lastUpdatedBy = lastUpdatedBy;
    }

}
