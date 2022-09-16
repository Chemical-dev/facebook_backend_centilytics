package centilytics.facebook_backend1.security.services;


import centilytics.facebook_backend1.dtos.PrivateStoryDto;
import centilytics.facebook_backend1.dtos.StoryResponse;
import centilytics.facebook_backend1.models.Story;

public interface StoryService {
    Story createStory(PrivateStoryDto privateStoryDto, Long userId) throws Exception;
    String deleteStory(Long id, Long userId) throws Exception;
    StoryResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
}
