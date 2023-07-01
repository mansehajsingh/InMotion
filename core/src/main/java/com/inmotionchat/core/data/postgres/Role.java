package com.inmotionchat.core.data.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.models.RoleType;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "roles", schema = Schema.IdentityPlatform)
public class Role extends AbstractDomain<Role> {

    private String name;

    @ManyToOne
    @JsonIgnore
    private Tenant tenant;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "permissions", schema = Schema.IdentityPlatform)
    private Set<String> permissions;

    private boolean restricted;

    private boolean root;

    public Role() {}

    public Role(String name, Tenant tenant, RoleType roleType, Permission ...permissions) {
        this.name = name;
        this.tenant = tenant;
        this.permissions = new HashSet<>();
        setRoleType(roleType);

        for (Permission permission : permissions)
            this.permissions.add(permission.value());
    }

    public Role(RoleDTO prototype) {
        this(prototype.name(), new Tenant(prototype.tenantId()), prototype.roleType(), prototype.permissions());
    }

    private Role(String name, Tenant tenant, RoleType roleType, Set<String> permissions) {
        this.name = name;
        this.tenant = tenant;
        this.permissions = permissions;
        setRoleType(roleType);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @JsonProperty(value = "permissions")
    public Set<String> getPermissionsAsStrings() {
        return this.permissions;
    }

    public RoleType getRoleType() {
        if (!this.root && !this.restricted)
            return RoleType.CUSTOM;
        else if (this.root && !this.restricted)
            return RoleType.ROOT;
        else if (!this.root && this.restricted)
            return RoleType.RESTRICTED;
        else
            return RoleType.RESTRICTED; // always assume restricted if unknown
    }

    public void setRoleType(RoleType roleType) {
        switch (roleType) {
            case ROOT -> {
                this.root = true;
                this.restricted = false;
            }
            case RESTRICTED -> {
                this.root = false;
                this.restricted = true;
            }
            case CUSTOM -> {
                this.root = false;
                this.restricted = false;
            }
        }
    }

    public Boolean hasPermission(Permission permission) {
        return this.permissions.contains(permission.value());
    }

    public Boolean hasPermissions(Permission... permissions) {
        return this.permissions.containsAll(Arrays.stream(permissions).map(Permission::value).toList());
    }

    public void addPermissionIfNotExists(Permission permission) {
        this.permissions.add(permission.value());
    }

    public void removePermissionIfExists(Permission permission) {
        this.permissions.remove(permission.value());
    }

    public Role copy() {
        Role copy = new Role(name, tenant, getRoleType(), permissions);
        copyTo(copy);
        return copy;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name")
                .isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>();
        violations.addAll(nameRule.collectViolations(this.name));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}
