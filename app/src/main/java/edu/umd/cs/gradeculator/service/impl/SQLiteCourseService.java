package edu.umd.cs.gradeculator.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.CourseService;

import static edu.umd.cs.gradeculator.service.impl.GradeculatorDbSchema.CourseTable;

/**
 * Created by howardksw1 on 4/30/17.
 */

public class SQLiteCourseService implements CourseService{

    private class CourseCursorWrapper extends CursorWrapper {
        CourseCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Course getCourse() {
            String id = getString(getColumnIndex(CourseTable.Columns.ID));
            String title = getString(getColumnIndex(CourseTable.Columns.TITLE));
            String identifier = getString(getColumnIndex(CourseTable.Columns.IDENTIFIER));
            double exam_weight = getDouble(getColumnIndex(CourseTable.Columns.EXAM_WEIGHT));
            double assignment_weight = getDouble(getColumnIndex(CourseTable.Columns.ASSIGNMENT_WEIGHT));
            double quiz_weight= getDouble(getColumnIndex(CourseTable.Columns.QUIZ_WEIGHT));
            double project_weight = getDouble((getColumnIndex(CourseTable.Columns.PROJECT_WEIGHT)));
            double extra_weight= getDouble(getColumnIndex(CourseTable.Columns.EXTRA_WEIGHT));
            double current_grade = getDouble(getColumnIndex(CourseTable.Columns.CURRENT_GRADE));
            double desired_grade = getDouble(getColumnIndex(CourseTable.Columns.DESIRED_GRADE));
            double desired_letter_grade;
            int credits = getInt(getColumnIndex(CourseTable.Columns.CREDITS));

            Course course = new Course();
            course.setId(id);
            course.setTitle(title);
            course.setIdentifier(identifier);
            course.setExam_weight(exam_weight);
            course.setAssignments_weight(assignment_weight);
            course.setQuiz_weight(quiz_weight);
            course.setProject_weight(project_weight);
            course.setExtra_weight(extra_weight);
            course.setCurrent_grade(current_grade);
            course.setDesire_grade(course.getDesire_grade_inLetter(desired_grade));
            course.setCredit(credits);

            return course;
        }
    }

    // SQLiteCourseService
    private SQLiteDatabase db;
    private SQLiteWorkService workDb;

    public SQLiteCourseService(Context context) {
        db = new GradeculatorCourseDbHelper(context).getWritableDatabase();
        workDb = new SQLiteWorkService(db);
    }

    protected SQLiteDatabase getDatabase() {
        return db;
    }

    private List<Course> queryCourse(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = db.query(CourseTable.NAME, null,
                whereClause, whereArgs, null, null, null);

        CourseCursorWrapper wrapper = new CourseCursorWrapper(cursor);
        ArrayList<Course> courses = new ArrayList<>();

        try {
            wrapper.moveToFirst();
            while(!cursor.isAfterLast()) {
                courses.add(wrapper.getCourse());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        return courses;
    }

    @Override
    public void addCourseToBacklog(Course course, Work work, String title) {
        //Log.d("hello", getCourseById(course.getId()).getTitle());
        if(work!=null)
            workDb.addWorkToBacklog(course, work, title);
        if(course != null) {
            if (getCourseById(course.getId()) != null) {
                db.update(CourseTable.NAME, getContentValues(course), CourseTable.Columns.ID + "=?",
                        new String[]{course.getId()});
            } else {
                db.insert(CourseTable.NAME, null, getContentValues(course));
            }
        }
    }

    @Override
    public Course getCourseById(String id) {
        if(id != null) {

            ArrayList<Course> courses_id =
                    (ArrayList<Course>) queryCourse(CourseTable.Columns.ID + "=?",
                            new String[]{id}, null);

            if(courses_id.size() > 0) {
                ArrayList<Work> works = workDb.getAllWorks_ForCourse(courses_id.get(0));
                for(Work w:works) {
                    courses_id.get(0).add(w);
                }
                return courses_id.get(0);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = (ArrayList<Course>) queryCourse(null, null, null);

        Collections.sort(courses, new Comparator<Course>() {
            @Override
            public int compare(Course course1, Course course2) {
                return course1.getIdentifier().compareTo(course2.getIdentifier());
            }
        });

        for(Course c:courses) {
            ArrayList<Work> works = workDb.getAllWorks_ForCourse(c);
            for(Work w:works) {
                c.add(w);
            }
        }

        return courses;
    }

    @Override
    public boolean remove_course(int position) {
        ArrayList<Course> courses = getAllCourses();
        Course delete = courses.get(position);

        if( (db.delete(CourseTable.NAME, CourseTable.Columns.ID + "=?"
                , new String[]{delete.getId()}) ) > 0) {
            return true;
        }

        return false;
    }

    private static ContentValues getContentValues(Course course) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CourseTable.Columns.ID, course.getId());
        contentValues.put(CourseTable.Columns.TITLE, course.getTitle());
        contentValues.put(CourseTable.Columns.IDENTIFIER, course.getIdentifier());
        contentValues.put(CourseTable.Columns.EXAM_WEIGHT, course.getExam_weight());
        contentValues.put(CourseTable.Columns.ASSIGNMENT_WEIGHT, course.getAssignment_weight());
        contentValues.put(CourseTable.Columns.QUIZ_WEIGHT, course.getQuiz_weight());
        contentValues.put(CourseTable.Columns.PROJECT_WEIGHT, course.getProject_weight());
        contentValues.put(CourseTable.Columns.EXTRA_WEIGHT, course.getExtra_weight());
        contentValues.put(CourseTable.Columns.CURRENT_GRADE, course.getCurrent_grade());
        contentValues.put(CourseTable.Columns.DESIRED_GRADE, course.getDesire_grade());
        contentValues.put(CourseTable.Columns.DESIRED_LETTER_GRADE,
                course.getDesire_grade_inLetter(course.getDesire_grade()).toString());
        contentValues.put(CourseTable.Columns.CREDITS, course.getCredit());

        return contentValues;
    }
}
