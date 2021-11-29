package com.multitenantDbImpl.tenant.config;

import com.multitenantDbImpl.masterTenant.config.DbContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT_ID = "books";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = DbContextHolder.getCurrentDb();
        return StringUtils.isNotBlank(tenant) ? tenant : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
