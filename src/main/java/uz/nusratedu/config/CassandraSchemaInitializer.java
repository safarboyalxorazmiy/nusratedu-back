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
        cqlTemplate.execute("CREATE INDEX IF NOT EXISTS users_logincode_idx ON users (logincode);")
                .doOnSuccess(result -> System.out.println("✅ users_logincode_idx ready."))
                .doOnError(err -> System.err.println("⚠️ Error creating index: " + err.getMessage()))
                .subscribe();
    }
}
