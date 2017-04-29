package edu.umd.cs.gradeculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.CourseService;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class IndividualFragment extends Fragment {
    private static final String EXTRA_WORK_CREATED = "WorkCreated";
    private static final String ARG_WORK_TITLE = "WorkTitle";
    private static final String ARG_COURSE_ID= "CourseId";
    private static final String ARG_CAT="Cat";
    private Work work;
    private CourseService ss;
    private String cat;
    private String title;
    private EditText gradeNameEditText;
    private EditText totalPointsEditText;
    private EditText PointsEditText;
    private EditText weightEditText;
    private String cId;
    private ArrayList<Work> works;

    private Button saveButton;
    private Button cancelButton;
    private static TextView dueDateY;
    private static TextView dueDateM;
    private static TextView dueDateD;

    public static IndividualFragment newInstance(String workTitle,String category,String courseId) {
        Bundle args = new Bundle();
        args.putString(ARG_WORK_TITLE, workTitle);
        args.putString(ARG_COURSE_ID,courseId);
        args.putString(ARG_CAT,category);
        IndividualFragment fragment = new IndividualFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cId = getArguments().getString(ARG_COURSE_ID);
        cat = getArguments().getString(ARG_CAT);
        title = getArguments().getString(ARG_WORK_TITLE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual, container, false);
        // make it take up the whole space
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);



        dueDateY = (TextView) view.findViewById(R.id.dueDateY);
        dueDateM = (TextView) view.findViewById(R.id.dueDateM);
        dueDateD = (TextView) view.findViewById(R.id.dueDateD);

        dueDateY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        dueDateM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        dueDateD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        if (title!=null){
            CourseService ss=DependencyFactory.getCourseService(getActivity());
            Course cs=ss.getCourseById(cId);
            switch(cat){
                case "Exam":
                    works=cs.getExams();
                    break;
                case "Quiz":
                    works=cs.getQuzs();
                    break;
                case "Assignment":
                    works=cs.getAssigs();
                    break;
                case "Project":
                    works=cs.getProjs();
                    break;
                case "Extra":
                    works=cs.getExtra();
                    break;
                default:
                    works=new ArrayList<Work>();
            }
            for(Work cWrok:works){
                if(cWrok.getTitle().equals(title)){
                    work=cWrok;
                }
            }
        }

        gradeNameEditText = (EditText) view.findViewById(R.id.igradeName);
        if(work != null){
            gradeNameEditText.setText(work.getTitle());
        }
        // these array adapters are causing  IndexOutOfBoundsException: Invalid index 116, size is 2

        totalPointsEditText = (EditText) view.findViewById(R.id.itotalPoints);
        if(work != null){
            totalPointsEditText.setText(""+work.getPossible_points());
        }

        PointsEditText = (EditText) view.findViewById(R.id.ipoint);
        if(work != null){
            PointsEditText.setText(""+work.getEarned_points());
        }
        weightEditText = (EditText) view.findViewById(R.id.iweight);
        if(work != null){
            weightEditText.setText(""+work.getWeight());
        }
        if(work !=null){
            dueDateY.setText(""+work.getDueDate().getYear());
            dueDateM.setText(""+work.getDueDate().getMinutes());
            dueDateD.setText(""+work.getDueDate().getDay());

        }


        toolbar.findViewById(R.id.toolbar_cancel_individual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        toolbar.findViewById(R.id.toolbar_save_individual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputsAreValid()) {
                    if (work == null) {
                        work = new Work(EXTRA_WORK_CREATED);
                    }

                    work.setTitle(gradeNameEditText.getText().toString());
                    Date date = new Date();
                    work.setDueDate(date);
                    work.setPossible_points(Double.parseDouble(totalPointsEditText.getText().toString()));
                    work.setEarned_points(Double.parseDouble(PointsEditText.getText().toString()));

                    work.setWeight(Double.parseDouble(weightEditText.getText().toString()));
                    date=new Date();
                    date.setHours(11);
                    date.setMinutes(59);
                    date.setSeconds(59);
                    date.setYear(Integer.parseInt(dueDateY.getText().toString()));
                    date.setMonth(Integer.parseInt(dueDateM.getText().toString()));
                    date.setDate(Integer.parseInt(dueDateD.getText().toString()));
                    work.setDueDate(date);
                    Intent data = new Intent();
                    switch(cat){
                        case "Exam":
                            work.setCategory(Work.Category.EXAM);
                            break;
                        case "Quiz":
                            work.setCategory(Work.Category.QUIZ);
                            break;
                        case "Assignment":
                            work.setCategory(Work.Category.ASSIGNMENT);
                            break;
                        case "Project":
                            work.setCategory(Work.Category.PROJECT);
                            break;
                        case "Extra":
                            work.setCategory(Work.Category.EXTRA);
                            break;

                    }
                    ss=DependencyFactory.getCourseService(getActivity());
                    Course cs =ss.getCourseById(cId);
                    if(title==null){
                        cs.add(work);
                    }else{
                        for(Work cWork:works){
                            if(cWork.getTitle().equals(title)){
                                cWork=work;
                            }
                        }
                    }

                    data.putExtra(EXTRA_WORK_CREATED, work);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                }
            }
        });


        return view;
    }

    public static Work getworkCreated(Intent data) {
        return (Work)data.getSerializableExtra(EXTRA_WORK_CREATED);
    }

    private boolean inputsAreValid() {
        return
                gradeNameEditText.getText().toString().length() > 0 &&
                        totalPointsEditText.getText().toString().length() > 0 &&
                        totalPointsEditText.getText().toString().length() > 0 &&
                        PointsEditText.getText().toString().length() > 0 &&
                        weightEditText.getText().toString().length() > 0;
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Date");
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            dueDateY.setText(year+"");
            dueDateM.setText(month+"");
            dueDateD.setText(day+"");
        }
    }
}
