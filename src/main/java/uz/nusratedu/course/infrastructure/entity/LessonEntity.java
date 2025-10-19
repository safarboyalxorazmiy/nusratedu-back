package uz.nusratedu.course.infrastructure.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("course_section_lesson")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonEntity {
    @PrimaryKey
    private UUID id = Uuids.timeBased();

    @Column("title")
    private String title;

    @Column("vimeo_url")
    private String vimeoUrl;

    @Column("pdf_url")
    private String pdfUrl;

    @Column("section_id")
    private UUID sectionId;
}