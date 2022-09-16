package centilytics.facebook_backend1.security.services;

import centilytics.facebook_backend1.dtos.PrivateStoryDto;
import centilytics.facebook_backend1.dtos.StoryDto;
import centilytics.facebook_backend1.dtos.StoryResponse;
import centilytics.facebook_backend1.models.*;
import centilytics.facebook_backend1.repository.MyUserRepository;
import centilytics.facebook_backend1.repository.PrivateStoryRepository;
import centilytics.facebook_backend1.repository.PublicStoryRepository;
import centilytics.facebook_backend1.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoryServiceImpl implements StoryService {
    @Autowired
    private PrivateStoryRepository privateStoryRepository;
    @Autowired
    private PublicStoryRepository publicStoryRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private MyUserRepository userRepository;

    @Override
    public Story createStory(PrivateStoryDto privateStoryDto, Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        User user1 = user.orElseThrow(() ->
                new Exception("No user found with id : " + userId));
        System.out.println(user1);
        Story story = new Story();
        if(privateStoryDto.getType() == BodyType.RESTRICTED){
            PrivateStory privateStory = new PrivateStory();
            privateStory.setHeading(privateStoryDto.getHeading());
            privateStory.setBody(privateStoryDto.getBody());
            privateStory.setType(privateStoryDto.getType());
            story.setPrivateStory(privateStory);
            privateStoryRepository.save(privateStory);
        }else {
            PublicStory publicStory = new PublicStory();
            publicStory.setHeading(privateStoryDto.getHeading());
            publicStory.setBody(privateStoryDto.getBody());
            publicStory.setType(privateStoryDto.getType());
            story.setPublicStory(publicStory);
            publicStoryRepository.save(publicStory);
        }
        story.setUser(user1);
        Story story2 = storyRepository.save(story);
        return story2;

    }

    @Override
    public String deleteStory(Long storyId, Long userId) throws Exception {
        Optional<Story> story = this.storyRepository.findById(storyId);
        Story story1 = story.orElseThrow(() ->
                new Exception("No story found with id : " + storyId));
        Optional<User> user = this.userRepository.findById(userId);
        User user1 = user.orElseThrow(() ->
                new Exception("No user found with id : " + userId));
        if (!story1.getUser().equals(user1)) return "You are not allowed";
        this.storyRepository.delete(story1);
        return "Story Deleted";
    }

    @Override
    public StoryResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Story> stories = storyRepository.findAll(pageable);

        // get content for page object
        List<Story> listOfPosts = stories.getContent();

        List<StoryDto> content= listOfPosts.stream().map(story -> mapToDTO(story)).collect(Collectors.toList());

        StoryResponse postResponse = new StoryResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(stories.getNumber());
        postResponse.setPageSize(stories.getSize());
        postResponse.setTotalElements(stories.getTotalElements());
        postResponse.setTotalPages(stories.getTotalPages());
        postResponse.setLast(stories.isLast());

        return postResponse;
    }

    private StoryDto mapToDTO(Story story){
        StoryDto postDto = new StoryDto();
        postDto.setPrivateStory(story.getPrivateStory());
        postDto.setPublicStory(story.getPublicStory());
        postDto.setUser(story.getUser());
        return postDto;
    }

}
