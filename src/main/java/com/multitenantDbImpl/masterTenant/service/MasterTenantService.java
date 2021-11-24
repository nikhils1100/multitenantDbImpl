package com.multitenantDbImpl.masterTenant.service;

import com.multitenantDbImpl.masterTenant.entity.MasterTenant;

public interface MasterTenantService {
    MasterTenant findByClientId(Integer clientId);
}
