package uz.nusratedu.test.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nusratedu.test.application.dto.TestResultResponse;
import uz.nusratedu.test.infastructure.entity.TestResultEntity;

@Mapper(componentModel = "spring")
public interface TestResultMapper {

    @Mapping(source = "isPassed", target = "isPassed")
    TestResultResponse toResponse(TestResultEntity entity);
}