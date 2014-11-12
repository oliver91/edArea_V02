package controllers;

import models.Course;
import models.Picture;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.course;
import views.html.courses;
import views.html.creatingCourse;
import views.html.upload;

import java.io.File;
import java.io.IOException;

/**
 * Created by oliver on 07.11.14.
 */

@Security.Authenticated(Secured.class)
public class Courses extends Controller
{
    public static Result creatingCourse()
    {
        return ok(creatingCourse.render(Form.form(Course.class)));
    }

    public static Result add() {

        Form<Course> creatingCourse_form = Form.form(Course.class).bindFromRequest();
        Course newCourse = new Course(
                                      request().username(),
                                      creatingCourse_form.get().courseName,
                                      null,
                                      creatingCourse_form.get().picture,
                                      creatingCourse_form.get().science,
                                      creatingCourse_form.get().aboutCourse,
                                      true);
        newCourse.save();
        return ok(upload.render(null, ""));
    }

    public static Result showCourses()
    {
        return ok(courses.render(User.find.byId(request().username()), Course.find.where().like("email", "%"+request().username()+"%").findList()));
    }

    public static Result showCoursePage(String courseName)
    {
        return ok(course.render(User.find.byId(request().username()), Course.find.byId(courseName)));
    }

    public static Result uploadPicture() {
        Course currentCourse = Course.find.where().like("email", "%"+request().username()+"%").like("current", "true").findUnique();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("picture");
        if (picture != null) {
            File file = picture.getFile();
            try {
                Picture image = new Picture();
                image.content = com.google.common.io.Files.toByteArray(file);
                image.contentType = picture.getContentType();
                image.save();
                currentCourse.logoId = image.id;
                currentCourse.current = false;
                currentCourse.save();

            } catch (IOException e) {
                return badRequest();
            }

            return ok(course.render(User.find.byId(request().username()), currentCourse));
        } else {
            return badRequest(upload.render(null, "File uploaded"));
        }
    }


    public static Result renderImage(Long id) {
        Picture image = Picture.byId(id);
        return ok(image.content).as(image.contentType);
    }

}
