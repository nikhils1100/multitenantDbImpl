package com.multitenantDbImpl.tenant.repository;

import com.multitenantDbImpl.tenant.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
