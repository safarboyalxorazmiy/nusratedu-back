package uz.nusratedu.test.application.mapper;

import org.mapstruct.*;
import uz.nusratedu.test.application.dto.TestCreateRequest;
import uz.nusratedu.test.application.dto.TestResponse;
import uz.nusratedu.test.infastructure.entity.TestEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "totalQuestions", ignore = true)
    @Mapping(target = "passingScore", ignore = true)
    @Mapping(target = "maxScore", ignore = true)
    TestEntity toEntity(TestCreateRequest dto);

    TestResponse toResponse(TestEntity entity);
}