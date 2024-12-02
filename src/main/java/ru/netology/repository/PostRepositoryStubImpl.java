package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostRepositoryStubImpl implements PostRepository {

    private final List<Post> posts = new ArrayList<>();

    public List<Post> all() {
        return posts;
    }

    public Optional<Post> getById(long id) {
        return posts.stream().filter(x -> x.getId() == id).findFirst();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(posts.size() + 1);
            posts.add(post);
        } else {
            List<Long> ids = posts.stream().map(Post::getId).collect(Collectors.toList());
            if (ids.contains(post.getId())) {
                int postIndex = posts.indexOf(getById(post.getId()).orElseThrow());
                posts.set(postIndex, post);
            }
        }
        return post;
    }

    public void removeById(long id) {
        posts.removeIf(post -> post.getId() == id);
    }
}