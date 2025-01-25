package com.Post.Service;

import com.Post.Config.RestTemplateConfig;
import com.Post.Entity.Post;
import com.Post.Paylode.PostDto;
import com.Post.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private RestTemplateConfig restTemplate;

    @Autowired
    private PostRepository postRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post getById(String id) {
        Post post = postRepository.findById(id).orElseThrow(()->new IllegalStateException("id "+id+" not found"));
        return post;
    }

    public PostDto getPostWithAllComments(String postId) {
        Post post = postRepository.findById(postId).get();
        ArrayList comments = restTemplate.getRestTemplate().getForObject("http://COMMENT/api/v1/comments/" + postId, ArrayList.class);
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        postDto.setComments(comments);
        return postDto;
    }

}
