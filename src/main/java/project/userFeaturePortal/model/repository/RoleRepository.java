package project.userFeaturePortal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.userFeaturePortal.model.entity.Role;
import project.userFeaturePortal.model.entity.User;

@Repository
public interface RoleRepository extends JpaRepository<User, Integer> {
    Role findByName(String name);
}
