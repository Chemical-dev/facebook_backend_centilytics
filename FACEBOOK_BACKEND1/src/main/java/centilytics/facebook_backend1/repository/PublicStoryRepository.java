package centilytics.facebook_backend1.repository;


import centilytics.facebook_backend1.models.PublicStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicStoryRepository extends JpaRepository<PublicStory, Long> {
}
