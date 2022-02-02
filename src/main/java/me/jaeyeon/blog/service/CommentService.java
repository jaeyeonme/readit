package me.jaeyeon.blog.service;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
}
