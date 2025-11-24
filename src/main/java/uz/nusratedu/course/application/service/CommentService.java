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

/**
 * ✅ CONVERTED: CommentService from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Replaced with simple blocking repository calls.
 * Virtual threads handle concurrency efficiently.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    // ✅ CHANGED: Returns CommentResponse instead of Mono<CommentResponse>
    @Override
    public CommentResponse create(CommentCreateRequest dto) {
        log.debug("Creating comment");
        // ✅ Simple blocking repository call
        var entity = commentMapper.toEntity(dto);
        var saved = commentRepository.save(entity);
        return commentMapper.toResponse(saved);
    }

    // ✅ CHANGED: Returns List<CommentResponse> instead of Flux<CommentResponse>
    @Override
    public List<CommentResponse> getByLessonId(UUID lessonId) {
        log.debug("Getting comments for lesson: {}", lessonId);
        // ✅ Simple blocking repository call
        // ✅ Assumes repository now returns List or Iterable instead of Flux
        // If repository returns List:
        return commentRepository.findByLessonId(lessonId)
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());

        // ✅ Alternative if repository still has Flux (should be updated):
        // return Flux.fromIterable(commentRepository.findByLessonId(lessonId))
        //     .map(commentMapper::toResponse)
        //     .collectList()
        //     .block();  // Block to get synchronous result
    }
}