package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.CommentResponse;
import uz.nusratedu.course.application.mapper.CommentMapper;
import uz.nusratedu.course.domain.service.ICommentService;
import uz.nusratedu.course.infrastructure.repository.CommentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public Mono<CommentResponse> create(CommentCreateRequest dto) {
        return commentRepository.save(commentMapper.toEntity(dto))
                .map(commentMapper::toResponse);
    }

    @Override
    public Flux<CommentResponse> getByLessonId(UUID lessonId) {
        return commentRepository.findByLessonId(lessonId).map(commentMapper::toResponse);
    }
}
