package centilytics.facebook_backend1.repository;

import centilytics.facebook_backend1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

}
