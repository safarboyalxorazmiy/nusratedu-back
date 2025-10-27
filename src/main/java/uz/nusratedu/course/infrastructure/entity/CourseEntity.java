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

@Table("course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {
    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    @Column("course_name")
    private String courseName; // Qur'on arabchasi asoslari

    @Column("course_description")
    private String courseDescription; // Arab yozuvi, tajvid va talaffuz bo'yicha zamonaviy kurs

    @Column("course_status")
    private String courseStatus; // Boshlang'ich

    @Column("course_field")
    private String courseField; // Qur'on

    @Column("course_attach_url")
    private String courseAttachUrl;

    @Column("teacher")
    private String teacher;

    @Column("course_description_lg")
    private String courseDescriptionLg;

}