package centilytics.facebook_backend1.repository;

import centilytics.facebook_backend1.models.ERole;
import centilytics.facebook_backend1.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
