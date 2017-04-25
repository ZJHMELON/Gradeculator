package edu.umd.cs.gradeculator;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Ping on 4/24/17.
 */

public class CourseDirFragment extends Fragment{

    private static final String ARG_COURSE_ID = "classId";
    private static final String COURSE_UPDATED = "CourseUpdated";
    private final int REQUEST_CODE = 5;
    private Course course;
    private LinearLayout layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String courseId = getArguments().getString(ARG_COURSE_ID);
        course = DependencyFactory.getCourseService(getActivity().getApplicationContext()).getCourseById(courseId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_dir, container, false);
        layout = (LinearLayout) view.findViewById(R.id.layout_course_dir);
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
        inflater.inflate(R.menu.course_die_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.course_dir_back_button:
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
             return true;
            case R.id.course_dir_edit_button:
                startActivityForResult(AddCourseActivity.newIntent(getActivity(),course.getId()),2);
                return true;
            case R.id.course_dir_add_button:
                startActivityForResult(ClassInfoActivity.newIntent(getActivity(),course.getId()),REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
               final Course c = ClassInfoActivity.getCourse(data);
                TextView temp;
                Log.d("asadasd",course.getExam_weight()+"hello");
                if(Double.compare(course.getExam_weight(),0.0) == 1){
                    temp = new TextView(getActivity());
                    temp.setText("EXAM");
                    temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(CategoryActivity.newIntent(getActivity(),c.getId(), Work.Category.EXAM));
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
                            startActivity(CategoryActivity.newIntent(getActivity(),c.getId(), Work.Category.QUIZ));
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
                            startActivity(CategoryActivity.newIntent(getActivity(),c.getId(), Work.Category.ASSIGNMENT));
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
                            startActivity(CategoryActivity.newIntent(getActivity(),c.getId(), Work.Category.PROJECT));
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
                            startActivity(CategoryActivity.newIntent(getActivity(),c.getId(), Work.Category.EXTRA));
                        }
                    });
                    layout.addView(temp);
                }
            }
        }
    }

    public static Course getCourse(Intent data) {
        return (Course)data.getSerializableExtra(COURSE_UPDATED);
    }

}
