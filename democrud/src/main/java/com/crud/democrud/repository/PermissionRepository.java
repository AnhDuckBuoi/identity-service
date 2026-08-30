package com.crud.democrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.democrud.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsById(String id);

    void deleteById(String name);
}
