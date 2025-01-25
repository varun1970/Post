package com.Post.Controller;

import com.Post.Entity.Post;
import com.Post.Paylode.PostDto;
import com.Post.Service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Data
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @PostMapping
    public ResponseEntity<Post> savePost(@RequestBody Post post) {
        if (post.getId() == null)
        post.setId(String.valueOf(UUID.randomUUID()));
        Post saved = postService.save(post);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Post post = postService.getById(String.valueOf(UUID.fromString(id)));
        System.out.println("geting Id ="+post.getId());
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    @CircuitBreaker(name="commentBreaker" , fallbackMethod = "commentFallback")
    public ResponseEntity<PostDto> getPostWithAllComments(@PathVariable  String postId){
        PostDto postDto = postService.getPostWithAllComments(postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }
    public ResponseEntity<PostDto> commentFallback(@PathVariable  String postId, Exception e){
        System.out.println("Fallback is executed because server is down "+e.getMessage());
        e.printStackTrace();
        PostDto postDto = new PostDto();
        postDto.setContent("server down");
        postDto.setDescription("server down");
        postDto.setTitle("server down");
        postDto.setId("1234");
        return new ResponseEntity<>(postDto, HttpStatus.BAD_REQUEST);
    }
}
