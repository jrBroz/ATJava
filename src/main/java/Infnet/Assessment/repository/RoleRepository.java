package Infnet.Assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Infnet.Assessment.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}