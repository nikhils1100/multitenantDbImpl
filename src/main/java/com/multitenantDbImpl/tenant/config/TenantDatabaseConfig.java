package com.multitenantDbImpl.tenant.config;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.DataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.multitenantDbImpl.tenant.repository", "com.multitenantDbImpl.tenant.entity" })
@EnableJpaRepositories(basePackages = {"com.multitenantDbImpl.tenant.repository", "com.multitenantDbImpl.tenant.service" },
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager")
public class TenantDatabaseConfig {

    @Bean(name = "tenantJpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }

    @Bean(name = "datasourceBasedMultitenantConnectionProvider")
    @ConditionalOnBean(name = "masterEntityManagerFactory")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        // Autowires the multi connection provider
        return new DataSourceBasedMultiTenantConnectionProviderImpl();
    }

    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean(name = "tenantEntityManagerFactory")
    @ConditionalOnBean(name = "datasourceBasedMultitenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("datasourceBasedMultitenantConnectionProvider")
                    MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver")
                    CurrentTenantIdentifierResolver tenantResolver) {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        //All tenant related entities, repositories and service classes must be scanned
        emfBean.setPackagesToScan("com.multitenantDbImpl.tenant");
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());
        emfBean.setPersistenceUnitName("tenantdb-persistence-unit");
        Map<String, Object> properties = new HashMap<>();
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "create");
        emfBean.setJpaPropertyMap(properties);
        return emfBean;
    }
}
