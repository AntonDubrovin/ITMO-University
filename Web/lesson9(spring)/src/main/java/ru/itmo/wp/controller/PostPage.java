package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.form.CommentForm;
import ru.itmo.wp.form.validator.CommentFormValidator;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;
    private final CommentFormValidator commentFormValidator;

    public PostPage(PostService postService, CommentFormValidator commentFormValidator) {
        this.commentFormValidator = commentFormValidator;
        this.postService = postService;

    }

    @InitBinder("commentForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(commentFormValidator);
    }

    @Guest
    @GetMapping("/post/{id}")
    public String post(@PathVariable String id, Model model) {
        long postId = parsePostId(id);

        Post viewedPost = postId == -1 ? null : postService.findById(postId);
        model.addAttribute("viewedPost", viewedPost);
        model.addAttribute("commentForm", new CommentForm());
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String post(@PathVariable String id,
                       @Valid @ModelAttribute("commentForm") CommentForm commentForm, BindingResult bindingResult, Model model,
                       HttpSession httpSession) {
        long postId = parsePostId(id);

        Post viewedPost = postId == -1 ? null : postService.findById(postId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("viewedPost", viewedPost);
            return "PostPage";
        }

        Comment comment = new Comment();
        comment.setUser(getUser(httpSession));
        if (viewedPost != null) {
            postService.addComment(viewedPost, comment);
        }

        return "redirect:/post/" + id;
    }

    private long parsePostId(String id) {
        long postId = -1;
        try {
            postId = Long.parseLong(id);
        } catch (NumberFormatException ignored) {
        }

        return postId;
    }
}
