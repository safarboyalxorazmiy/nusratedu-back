package uz.nusratedu.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CassandraSchemaInitializer implements ApplicationRunner {

    private final CqlTemplate cqlTemplate;

    @Override
    public void run(ApplicationArguments args) {
        // users login code index
        try {
            cqlTemplate.execute("CREATE INDEX IF NOT EXISTS users_logincode_idx ON users (logincode);");
            log.info("✅ users_logincode_idx ready.");
        } catch (Exception ex) {
            log.error("⚠️ Error creating users_logincode_idx: {}", ex.getMessage());
        }

        // comments by lesson_id
        try {
            cqlTemplate.execute("CREATE INDEX IF NOT EXISTS comment_lesson_id_idx ON course_section_lesson_comment (lesson_id);");
            log.info("✅ comment_lesson_id_idx ready.");
        } catch (Exception ex) {
            log.error("⚠️ Error creating comment_lesson_id_idx: {}", ex.getMessage());
        }

        // lessons by section_id
        try {
            cqlTemplate.execute("CREATE INDEX IF NOT EXISTS lesson_section_id_idx ON course_section_lesson (section_id);");
            log.info("✅ lesson_section_id_idx ready.");
        } catch (Exception ex) {
            log.error("⚠️ Error creating lesson_section_id_idx: {}", ex.getMessage());
        }

        // sections by course_id
        try {
            cqlTemplate.execute("CREATE INDEX IF NOT EXISTS section_course_id_idx ON course_section (course_id);");
            log.info("✅ section_course_id_idx ready.");
        } catch (Exception ex) {
            log.error("⚠️ Error creating section_course_id_idx: {}", ex.getMessage());
        }
    }
}