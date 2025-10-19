package uz.nusratedu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CassandraSchemaInitializer implements ApplicationRunner {

    private final ReactiveCqlTemplate cqlTemplate;

    @Override
    public void run(ApplicationArguments args) {
        // users
        cqlTemplate.execute("CREATE INDEX IF NOT EXISTS users_logincode_idx ON users (logincode);")
                .doOnSuccess(result -> System.out.println("✅ users_logincode_idx ready."))
                .doOnError(err -> System.err.println("⚠️ Error creating index: " + err.getMessage()))
                .subscribe();

        // comments by lesson_id
        cqlTemplate.execute("CREATE INDEX IF NOT EXISTS comment_lesson_id_idx ON course_section_lesson_comment (lesson_id);")
                .doOnSuccess(result -> System.out.println("✅ comment_lesson_id_idx ready."))
                .doOnError(err -> System.err.println("⚠️ Error creating index for comments: " + err.getMessage()))
                .subscribe();

        // lessons by section_id
        cqlTemplate.execute("CREATE INDEX IF NOT EXISTS lesson_section_id_idx ON course_section_lesson (section_id);")
                .doOnSuccess(result -> System.out.println("✅ lesson_section_id_idx ready."))
                .doOnError(err -> System.err.println("⚠️ Error creating index for lessons: " + err.getMessage()))
                .subscribe();

        // sections by course_id
        cqlTemplate.execute("CREATE INDEX IF NOT EXISTS section_course_id_idx ON course_section (course_id);")
                .doOnSuccess(result -> System.out.println("✅ section_course_id_idx ready."))
                .doOnError(err -> System.err.println("⚠️ Error creating index for sections: " + err.getMessage()))
                .subscribe();

    }
}
