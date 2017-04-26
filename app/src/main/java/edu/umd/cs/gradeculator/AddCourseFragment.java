package edu.umd.cs.gradeculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import edu.umd.cs.gradeculator.model.Course;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
/**
 * Created by apple on 4/20/17.
 */

public class AddCourseFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_COURSE_CREATED = "CourseCreated";
    private static final String ARG_COURSE_ID = "courseId";
    private static final int REQUEST_CODE_SAVE_STORY = 1;

    private Course course;

    private EditText courseNameEditText;
    private EditText courseTitleEditText;
    private EditText creditEditText;
    private Spinner grade_spinner;

    private Button saveButton;
    private Button cancelButton;

    public static AddCourseFragment newInstance(String courseID){
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_ID, courseID);

        AddCourseFragment fragment = new AddCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        String courseId = getArguments().getString(ARG_COURSE_ID);
        course = DependencyFactory.getCourseService(getActivity().getApplicationContext()).
                getCourseById(courseId);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_addcourse, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_course:
                if(course == null){
                    Intent saveCourseIntent = new Intent(getActivity(), MainActivity.class);
                    startActivityForResult(saveCourseIntent, REQUEST_CODE_SAVE_STORY);
                } else{
                    Intent editCourseIntent = new Intent(getActivity(), MainActivity.class);
                    startActivityForResult(editCourseIntent, REQUEST_CODE_SAVE_STORY);
                }
                return true;
            case R.id.menu_item_cancel_course:
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(mainIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcourse, container, false);

        courseNameEditText = (EditText)view.findViewById(R.id.course_name);
        if(course != null){
            courseNameEditText.setText(course.getTitle());
        }
        courseTitleEditText = (EditText)view.findViewById(R.id.course_title);
        if(course != null){
            courseTitleEditText.setText(course.getIdentifier());
        }

        creditEditText = (EditText)view.findViewById(R.id.credit);
        if(course != null){
            creditEditText.setText("" + course.getCredit());
        }

        grade_spinner = (Spinner)view.findViewById(R.id.grade_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.grade_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade_spinner.setAdapter(statusAdapter);
        if (course != null) {
            grade_spinner.setSelection(course.getGradePosition());
        } else {
            grade_spinner.setSelection(statusAdapter.getPosition("A"));
        }

        saveButton = (Button)view.findViewById(R.id.save_story_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputsAreValid()) {
                    if (course == null) {
                        course = new Course();
                    }

                    course.setTitle(courseNameEditText.getText().toString());
                    course.setIdentifier(courseTitleEditText.getText().toString());
                    course.setCredit(Integer.parseInt(creditEditText.getText().toString()));
                    course.setGrade(grade_spinner.getSelectedItemPosition());
                    course.setDesire_grade(course.getGrade());

                    Intent data = new Intent();
                    data.putExtra(EXTRA_COURSE_CREATED, course);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                } else{
                    // create a toast message
                    Toast.makeText(getActivity(),"Invalid input",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancel_story_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;

    }

    public static Course getCourseCreated(Intent data) {
        return (Course)data.getSerializableExtra(EXTRA_COURSE_CREATED);
    }

    private boolean inputsAreValid() {
        return
                courseNameEditText.getText().toString().length() > 0 &&
                        courseTitleEditText.getText().toString().length() > 0 &&
                        Integer.parseInt(creditEditText.getText().toString()) > 0 &&
                        grade_spinner.getSelectedItemPosition() >= 0;
    }
}
