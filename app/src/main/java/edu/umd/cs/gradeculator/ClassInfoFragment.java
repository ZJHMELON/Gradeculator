package edu.umd.cs.gradeculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import edu.umd.cs.gradeculator.model.Course;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Michael on 4/21/2017.
 */

public class ClassInfoFragment extends Fragment {
    private static final String ARG_COURSE_ID = "classId";
    private static final String COURSE_UPDATED = "CourseUpdated";
    private Course course;
    private EditText exam_edit;
    private EditText quiz_edit;
    private EditText assignment_edit;
    private EditText project_edit;
    private EditText extra_edit;

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
        View view = inflater.inflate(R.layout.fragment_class_info, container, false);
        exam_edit = (EditText) view.findViewById(R.id.exam_edit_text);
        quiz_edit = (EditText) view.findViewById(R.id.quiz_edit_text);
        assignment_edit = (EditText) view.findViewById(R.id.assignment_edit_text);
        project_edit = (EditText) view.findViewById(R.id.project_edit_text);
        extra_edit = (EditText) view.findViewById(R.id.extra_edit_text);
        return view;
    }

    public static ClassInfoFragment newInstance(String classId) {
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_ID, classId);

        ClassInfoFragment fragment = new ClassInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_class_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.class_info_menu_item_cancel_button:
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
                return true;
            case R.id.class_info_menu_item_save_button:

                // Since we already limit the input to be decimal so we just need to check if the
                // user enter or not. IMPOSSIBLE TO IMPUT NON NUMBER OR NEGATIVE NUMBER
                if(course == null) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Opps..Something is wrong")
                            .setMessage("Make sure the course itself is create first")
                            .setPositiveButton(R.string.okay, null)
                            .show();
                }else{
                    // course is already established
                    // start extracting data
                    double exam_double = 0.0,quiz_double = 0.0,assignment_double = 0.0,
                            project_double = 0.0,extra_double = 0.0,total = 0.0;

                    // Extracting any non-empty data non by non
                    if(exam_edit.getText().toString().length() > 0 ){
                        exam_double = Double.parseDouble(exam_edit.getText().toString());
                    }
                    if(quiz_edit.getText().toString().length() > 0 ){
                        quiz_double = Double.parseDouble(quiz_edit.getText().toString());
                    }
                    if(assignment_edit.getText().toString().length() > 0 ){
                        assignment_double = Double.parseDouble(assignment_edit.getText().toString());
                    }
                    if(project_edit.getText().toString().length() > 0 ){
                        project_double = Double.parseDouble(project_edit.getText().toString());
                    }
                    if(extra_edit.getText().toString().length() > 0 ){
                        extra_double = Double.parseDouble(extra_edit.getText().toString());
                    }
                    total = exam_double + quiz_double + assignment_double + project_double;
                    if(total < 100){
                        course.setAssignments_weight(assignment_double);
                        course.setExam_weight(exam_double);
                        course.setQuiz_weight(quiz_double);
                        course.setExtra_weight(extra_double);
                        course.setProject_weight(project_double);
                        Intent data = new Intent();
                        data.putExtra(COURSE_UPDATED, course);
                        getActivity().setResult(RESULT_OK, data);
                        getActivity().finish();
                    } else{
                        // total exceeding 100
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Opps..")
                                .setMessage("Make sure the total weight does not exceed 100%.")
                                .setPositiveButton(R.string.okay,null)
                                .show();
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Course getCourse(Intent data) {
        return (Course)data.getSerializableExtra(COURSE_UPDATED);
    }


}
