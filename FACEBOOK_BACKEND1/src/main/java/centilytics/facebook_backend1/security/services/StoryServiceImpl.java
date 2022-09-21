package centilytics.facebook_backend1.security.services;

import centilytics.facebook_backend1.dtos.PrivateStoryDto;
import centilytics.facebook_backend1.dtos.StoryDto;
import centilytics.facebook_backend1.dtos.StoryResponse;
import centilytics.facebook_backend1.models.*;
import centilytics.facebook_backend1.repository.MyUserRepository;
import centilytics.facebook_backend1.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private MyUserRepository userRepository;

    @Override
    public Story createStory(StoryDto storyDto, Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        User user1 = user.orElseThrow(() ->
                new Exception("No user found with id : " + userId));
        System.out.println(user1);
        Story story = new Story();
        if(storyDto.getType() == BodyType.RESTRICTED){
            story.setHeading(storyDto.getHeading());
            story.setBody(storyDto.getBody());
            story.setType(storyDto.getType());
            List<User> userList = new ArrayList<>();
            for(User user2 : userList){
                if (story.getEmailVaults().contains(user2.getEmailVault())){
                    user2.getStoryList().add(story);
                }
            }
        }else {
            story.setHeading(storyDto.getHeading());
            story.setBody(storyDto.getBody());
            story.setType(storyDto.getType());
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
    public StoryResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir, String email ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Story> stories = storyRepository.findAll(pageable);
        List<Story> listOfPosts =  stories.stream().filter(story -> story.getUser()
                .getEmail().equals(email)).toList();
        System.out.println(listOfPosts);
        List<StoryDto> content= listOfPosts.stream().map(story -> mapToDTO(story)).collect(Collectors.toList());

        StoryResponse postResponse = new StoryResponse();
        postResponse.setContent(listOfPosts);
        postResponse.setPageNo(stories.getNumber());
        postResponse.setPageSize(stories.getSize());
        postResponse.setTotalElements(stories.getTotalElements());
        postResponse.setTotalPages(stories.getTotalPages());
        postResponse.setLast(stories.isLast());

        return postResponse;
    }

    private StoryDto mapToDTO(Story story){
        StoryDto postDto = new StoryDto();

        postDto.setUser(story.getUser());
        return postDto;
    }

}
