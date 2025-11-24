package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.CommentResponse;
import uz.nusratedu.course.application.mapper.CommentMapper;
import uz.nusratedu.course.domain.service.ICommentService;
import uz.nusratedu.course.infrastructure.repository.CommentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse create(CommentCreateRequest dto) {
        log.debug("Creating comment entity for lesson: {}", dto.getLessonId());
        var entity = commentMapper.toEntity(dto);
        var saved = commentRepository.save(entity);
        log.info("Comment persisted with ID: {}", saved.getId());
        return commentMapper.toResponse(saved);
    }

    @Override
    public List<CommentResponse> getByLessonId(UUID lessonId) {
        log.debug("Fetching comments from repository for lesson: {}", lessonId);
        var comments = commentRepository.findByLessonId(lessonId)
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Retrieved {} comments for lesson: {}", comments.size(), lessonId);
        return comments;
    }
}