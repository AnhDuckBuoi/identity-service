package com.crud.democrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.democrud.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsById(String id);

    void deleteById(String name);
}
