package models;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Blob;

/**
* Created by oliver on 05.11.14.
*/
@Entity
public class Course extends Model
{
    public String email;
    @Id
    @Required
    public String courseName;
    @Required
    public String science;
//    @Lob
    @Column(columnDefinition = "TEXT")
    @Required
    public String aboutCourse;

    public Long logoId;

    public Blob picture;

    public boolean current;

    public Course(String email, String courseName, Long logoId, Blob picture, String science, String aboutCourse, Boolean current) {
        this.email = email;
        this.courseName = courseName;
        this.picture = picture;
        this.science = science;
        this.aboutCourse = aboutCourse;
        this.logoId = logoId;
        this.current = current;
    }

    public static Model.Finder<String, Course> find = new Model.Finder<String, Course>(String.class, Course.class);
}
