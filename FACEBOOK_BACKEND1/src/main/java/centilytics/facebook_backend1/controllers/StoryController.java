package centilytics.facebook_backend1.controllers;

import centilytics.facebook_backend1.annotations.DELETE;
import centilytics.facebook_backend1.annotations.GET;
import centilytics.facebook_backend1.annotations.POST;
import centilytics.facebook_backend1.annotations.PREAUTHORIZE;
import centilytics.facebook_backend1.constants.AppConstants;
import centilytics.facebook_backend1.dtos.StoryDto;
import centilytics.facebook_backend1.dtos.StoryResponse;;
import centilytics.facebook_backend1.models.Story;
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

    @DELETE("/delete") @PREAUTHORIZE("hasRole('USER')")
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

    @GET("/stories/{email}") @PREAUTHORIZE("hasRole('USER')")
    public StoryResponse getAllStories(
            @PathVariable(value = "email" ) String email,
            @RequestParam(value = "start", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int start,
            @RequestParam(value = "count", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int count,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
            ){
        return storyService.getAllPosts(start, count, sortBy, sortDir, email);
    }

    @POST("/{userId}") @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> createStory(@PathVariable(value = "userId") Long userId, @RequestBody StoryDto
            storyDto ) throws Exception{
        System.out.println(storyDto);
        Story story = storyService.createStory(storyDto, userId);
            return ResponseHandler.generateResponse(HttpStatus.OK, "Story created successfully.", story);

    }
}
