package centilytics.facebook_backend1.controllers;

import centilytics.facebook_backend1.constants.AppConstants;
import centilytics.facebook_backend1.dtos.PrivateStoryDto;
import centilytics.facebook_backend1.dtos.StoryResponse;
import centilytics.facebook_backend1.models.BodyType;
import centilytics.facebook_backend1.models.Story;
import centilytics.facebook_backend1.models.User;
import centilytics.facebook_backend1.payload.request.DeleteUser;
import centilytics.facebook_backend1.payload.response.ResponseHandler;
import centilytics.facebook_backend1.security.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/story")
public class StoryController {
    @Autowired
    private StoryService storyService;

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> deleteStory(@RequestBody DeleteUser deleteUser) throws Exception{
        if (deleteUser.getId() == null || deleteUser.getUserId() == null ) return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                "Field Cannot be Empty", null);
    try {
        this.storyService.deleteStory(deleteUser.getId(), deleteUser.getUserId());
        return ResponseHandler.generateResponse(HttpStatus.ACCEPTED, "Story Deleted Successfully", null);
    }catch (Exception exception){
         System.out.println(exception);
         ResponseHandler.generateResponse(HttpStatus.EXPECTATION_FAILED,
                 "Something Went Wrong", null);
     }
    return null;
    }

    @GetMapping("/stories")
    @PreAuthorize("hasRole('USER')")
    public StoryResponse getAllStories(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
//            @RequestParam(value = "start", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int start,
//          @RequestParam(value = "count", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int count,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestBody User user
            ){
        return storyService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> createStory(@PathVariable(value = "userId") Long userId, @RequestBody PrivateStoryDto
            privateStoryDto ) throws Exception{
        System.out.println(privateStoryDto);
        Story story = storyService.createStory(privateStoryDto, userId);
        if(privateStoryDto.getType() == BodyType.RESTRICTED){
            System.out.println(story);
            return ResponseHandler.generateResponse(HttpStatus.OK, "Story created successfully.", story);
        }else {
         return null;
        }

    }
}
