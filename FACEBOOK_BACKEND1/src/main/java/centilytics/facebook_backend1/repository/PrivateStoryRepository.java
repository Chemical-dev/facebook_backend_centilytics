package centilytics.facebook_backend1.repository;

import centilytics.facebook_backend1.models.PrivateStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateStoryRepository extends JpaRepository<PrivateStory, Long> {
}
