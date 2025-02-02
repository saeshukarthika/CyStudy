package onetoone.Courses;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Users.User;
import onetoone.Users.UserRepository;

/**
 * 
 * @author Rahul Sudev
 * 
 */ 

@RestController
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/courses")
    List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    @GetMapping(path = "/courses/{id}")
    Course getCourseById(@PathVariable long id){
        return courseRepository.findById(id);
    }

    @PostMapping(path = "/courses/post")
    String createCourse(Course course){
        if (course == null)
            return failure;
        courseRepository.save(course);
        return success;
    }


    @PutMapping(path = "/courses/{id}")
    Course updateCourse(@PathVariable long id, @RequestBody Course request){
        Course course = courseRepository.findById(id);
        if(course == null)
            throw new RuntimeException("User id does not exist");
        else if(request.getId()!=id){
            throw new RuntimeException("Path variable Id does not match with request Id");
        }
        courseRepository.save(request);
        return courseRepository.findById(id);
    }

    @DeleteMapping(path = "/courses/{user_id}/{course_id}")
    String deleteCourse(@PathVariable long user_id, @PathVariable long course_id){

        // Check if there is an object depending on user and then remove the dependency
        User user = userRepository.findById(user_id);
        Course course = courseRepository.findById(course_id);
        user.removeCourse(course);
        userRepository.save(user);

        // delete the laptop if the changes have not been reflected by the above statement
        courseRepository.deleteById(course_id);
        return success;
    }
}
