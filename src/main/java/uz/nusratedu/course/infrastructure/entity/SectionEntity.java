package uz.nusratedu.course.infrastructure.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("course_section")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionEntity {
    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    private String title;

    @Column("course_id")
    private UUID courseId;

    @Column("created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();
}