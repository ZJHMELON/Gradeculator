package edu.umd.cs.gradeculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import edu.umd.cs.gradeculator.model.Work;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class IndividualFragment extends Fragment {
    private static final String EXTRA_WORK_CREATED = "WorkCreated";
    private static final String ARG_WORK_TITLE = "WorkTitle";
    private Work work;

    private EditText gradeNameEditText;
    private Spinner dueDateSpinnerMonth;
    private Spinner dueDateSpinnerDay;
    private Spinner dueDateSpinnerYear;
    private EditText totalPointsEditText;
    private EditText PointsEditText;
    private Spinner statusSpinner;
    private EditText weightEditText;

    private Button saveButton;
    private Button cancelButton;

    public static IndividualFragment newInstance(String workTitle) {
        Bundle args = new Bundle();
        args.putString(ARG_WORK_TITLE, workTitle);
        IndividualFragment fragment = new IndividualFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String workTitle = getArguments().getString(ARG_WORK_TITLE);
        work = new Work(workTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual, container, false);
        Date date = new Date();
        gradeNameEditText = (EditText) view.findViewById(R.id.igradeName);
        if(work != null){
            gradeNameEditText.setText(work.getTitle());
        }

        dueDateSpinnerMonth = (Spinner) view.findViewById(R.id.ispinnerdueDateMonth);
        Integer[] month = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
        ArrayAdapter<Integer> adapterM = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_dropdown_item, month);
        dueDateSpinnerMonth.setAdapter(adapterM);
        dueDateSpinnerMonth.setSelection(date.getMonth()-1);

        dueDateSpinnerDay = (Spinner) view.findViewById(R.id.ispinnerdueDateDay);
        Integer[] day = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
        ArrayAdapter<Integer> adapterD = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_dropdown_item, day);
        dueDateSpinnerDay.setAdapter(adapterD);
        dueDateSpinnerDay.setSelection(date.getDay()-1);

        dueDateSpinnerYear = (Spinner) view.findViewById(R.id.ispinnerdueDateYear);
        Integer[] year = new Integer[]{2017,2018};
        ArrayAdapter<Integer> adapterY = new ArrayAdapter<Integer>(getContext(),android.R.layout.simple_spinner_dropdown_item, year);
        dueDateSpinnerYear.setAdapter(adapterY);
        dueDateSpinnerYear.setSelection(date.getYear()-1);

        totalPointsEditText = (EditText) view.findViewById(R.id.itotalPoints);
        if(work != null){
            totalPointsEditText.setText(""+work.getPossible_points());
        }

        PointsEditText = (EditText) view.findViewById(R.id.ipoint);
        if(work != null){
            PointsEditText.setText(""+work.getEarned_points());
        }

        statusSpinner = (Spinner) view.findViewById(R.id.ispinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        if (work != null) {
            statusSpinner.setAdapter(statusAdapter);
        }

        weightEditText = (EditText) view.findViewById(R.id.iweight);
        if(work != null){
            weightEditText.setText(""+work.getWeight());
        }

        saveButton = (Button)view.findViewById(R.id.save_story_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputsAreValid()) {
                    if (work == null) {
                        work = new Work(EXTRA_WORK_CREATED);
                    }

                    work.setTitle(gradeNameEditText.getText().toString());
                    Date date = new Date();
                    date.setMonth(dueDateSpinnerMonth.getSelectedItemPosition());
                    date.setDate(dueDateSpinnerDay.getSelectedItemPosition());
                    date.setYear(dueDateSpinnerYear.getSelectedItemPosition());
                    work.setDueDate(date);
                    work.setPossible_points(Double.parseDouble(totalPointsEditText.getText().toString()));
                    work.setEarned_points(Double.parseDouble(PointsEditText.getText().toString()));
                    switch (statusSpinner.getSelectedItem().toString()){
                        case "EXAM":
                            work.setCategory(Work.Category.EXAM);
                            break;
                        case "QUIZ":
                            work.setCategory(Work.Category.QUIZ);
                            break;
                        case "ASSIGNMENT":
                            work.setCategory(Work.Category.ASSIGNMENT);
                            break;
                        case "PROJECT":
                            work.setCategory(Work.Category.PROJECT);
                            break;
                        case "EXTRA":
                            work.setCategory(Work.Category.EXTRA);
                            break;
                    }
                    work.setWeight(Double.parseDouble(weightEditText.getText().toString()));
                    Intent data = new Intent();
                    data.putExtra(EXTRA_WORK_CREATED, work);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
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

    public static Work getworkCreated(Intent data) {
        return (Work)data.getSerializableExtra(EXTRA_WORK_CREATED);
    }

    private boolean inputsAreValid() {
        return
                gradeNameEditText.getText().toString().length() > 0 &&
                        totalPointsEditText.getText().toString().length() > 0 &&
                        dueDateSpinnerMonth.getSelectedItemPosition() >= 0 &&
                        dueDateSpinnerDay.getSelectedItemPosition() >= 0 &&
                        dueDateSpinnerYear.getSelectedItemPosition() >= 0 &&
                        totalPointsEditText.getText().toString().length() > 0 &&
                        PointsEditText.getText().toString().length() > 0 &&
                        weightEditText.getText().toString().length() > 0 &&
                        statusSpinner.getSelectedItemPosition() >= 0;
    }

}
