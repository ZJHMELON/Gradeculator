package edu.umd.cs.gradeculator.service;

import java.util.List;

import edu.umd.cs.gradeculator.model.Course;

/**
 * Created by howardksw1 on 4/5/17.
 */

public interface CourseService {
    public void addCourseToBacklog(Course course);
    public Course getCourseById(String id);
    public List<Course> getAllCourses();
    public boolean remove_course(int position);
}
