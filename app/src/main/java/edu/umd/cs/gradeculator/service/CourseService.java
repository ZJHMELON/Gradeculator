package edu.umd.cs.gradeculator.service;

import java.util.ArrayList;

import edu.umd.cs.gradeculator.model.Course;

/**
 * Created by howardksw1 on 4/5/17.
 */

public interface CourseService {
    public void addCourseToBacklog(Course course);
    public Course getCourseById(String id);
    public ArrayList<Course> getAllCourses();
    public boolean remove_course(int position);
}
