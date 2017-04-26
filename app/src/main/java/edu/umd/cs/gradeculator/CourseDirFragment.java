package edu.umd.cs.gradeculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.CourseService;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ping on 4/24/17.
 */

public class CourseDirFragment extends Fragment{

    private static final String ARG_COURSE_ID = "classId";
    private static final String COURSE_UPDATED = "CourseUpdated";
    private final int REQUEST_CODE_ADD_WEIGHT = 5;
    private final int REQUEST_CODE_EDIT_COURSE = 1;
    private Course course;
    private LinearLayout layout;
    private CourseService courseService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String courseId = getArguments().getString(ARG_COURSE_ID);
        course = DependencyFactory.getCourseService(getActivity()).getCourseById(courseId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_dir, container, false);
        layout = (LinearLayout) view.findViewById(R.id.layout_course_dir);
        updateUI();
        return view;
    }

    public static CourseDirFragment newInstance(String classId) {
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_ID, classId);

        CourseDirFragment fragment = new CourseDirFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.course_dir_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.course_dir_back_button:
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
             return true;
            case R.id.course_dir_edit_button:
                startActivityForResult(AddCourseActivity.newIntent(getActivity(),course.getId()),REQUEST_CODE_EDIT_COURSE);
                return true;
            case R.id.course_dir_add_button:
                startActivityForResult(ClassInfoActivity.newIntent(getActivity(),course.getId()),REQUEST_CODE_ADD_WEIGHT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_WEIGHT) {
               final Course c = ClassInfoActivity.getCourse(data);
//                Log.d("what",c.toString());
                course.setExam_weight(c.getExam_weight());
                course.setAssignments_weight(c.getAssignment_weight());
                course.setQuiz_weight(c.getQuiz_weight());
                course.setProject_weight(c.getProject_weight());
                course.setExtra_weight(c.getExtra_weight());
                updateUI();
            }

            if(requestCode == REQUEST_CODE_EDIT_COURSE) {
                final Course c = AddCourseActivity.getCourseCreated(data);
                course.setTitle(c.getTitle());
                course.setIdentifier(c.getIdentifier());
                course.setCredit(c.getCredit());
                course.setDesire_grade(c.getDesire_grade_inLetter(c.getDesire_grade()));
                Log.d("what",c.getTitle());
                Log.d("what",course.getTitle());
            }
        }
    }

    private void updateUI() {
        TextView temp;
        Log.d("asadasd",course.getExam_weight()+"hello");
        if(Double.compare(course.getExam_weight(),0.0) == 1){
            temp = new TextView(getActivity());
            temp.setText("EXAM");
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(CategoryActivity.newIntent(getActivity(),course.getId(), Work.Category.EXAM));
                }
            });

            layout.addView(temp);
        }
        if(Double.compare(course.getQuiz_weight(),0.0) == 1){
            temp = new TextView(getActivity());
            temp.setText("QUIZ");
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(CategoryActivity.newIntent(getActivity(),course.getId(), Work.Category.QUIZ));
                }
            });
            layout.addView(temp);
        }
        if(Double.compare(course.getAssignment_weight(),0.0) == 1){
            temp = new TextView(getActivity());
            temp.setText("Assignment");
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(CategoryActivity.newIntent(getActivity(),course.getId(), Work.Category.ASSIGNMENT));
                }
            });
            layout.addView(temp);
        }
        if(Double.compare(course.getProject_weight(),0.0) == 1){
            temp = new TextView(getActivity());
            temp.setText("Project");
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(CategoryActivity.newIntent(getActivity(),course.getId(), Work.Category.PROJECT));
                }
            });
            layout.addView(temp);
        }
        if(Double.compare(course.getExtra_weight(),0.0) == 1){
            temp = new TextView(getActivity());
            temp.setText("Extra");
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(CategoryActivity.newIntent(getActivity(),course.getId(), Work.Category.EXTRA));
                }
            });
            layout.addView(temp);
        }
    }

    public static Course getCourse(Intent data) {
        return (Course)data.getSerializableExtra(COURSE_UPDATED);
    }

}
