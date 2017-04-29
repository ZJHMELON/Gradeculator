package edu.umd.cs.gradeculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.CourseService;

import static android.app.Activity.RESULT_OK;
import static edu.umd.cs.gradeculator.model.Work.Category;

/**
 * Created by weng2 on 4/12/2017.
 */

public class CategoryFragment extends Fragment {

    private RecyclerView categoryRecyclerView;
    private TextView categoryTitleTextView;

    private CourseService courseService;

    private WorkAdapter adapter;
    private static final String ARG_COURSE_ID = "courseId";
    private static final String ARG_CATEGORY_NAME = "categoryName";
    private static final int REQUEST_CODE_CREATE_WORK = 0;

    private String courseId;
    private Category category;
    private String categoryName;


    public static CategoryFragment newInstance(String courseId, Category category) {
        Bundle args = new Bundle();

        args.putString(ARG_COURSE_ID, courseId);
        args.putSerializable(ARG_CATEGORY_NAME, category);

        CategoryFragment fragment = new CategoryFragment();

        fragment.setArguments(args);
        return fragment; //ss
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseService = DependencyFactory.getCourseService(getActivity().getApplicationContext());

        courseId = getArguments().getString(ARG_COURSE_ID);
        category = (Category) getArguments().getSerializable(ARG_CATEGORY_NAME);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CREATE_WORK) {
            if (data == null) {
                return;
            }
            //haha

            //         Work workCreated = IndividualActivity.getStoryCreated(data);
            //        courseService.getCourseById(courseId).add(workCreated);
            // courseService.addCourseToBacklog();
            updateUI();
        }
    }


    private void updateUI() {

        List<Work> works = new ArrayList<>();
        Course theCourse = courseService.getCourseById(courseId);


        if (category != null) {
            if (category == Category.EXAM) {
                works = theCourse.getExams();
                categoryName = "Exam";
            } else if (category == Category.QUIZ) {
                works = theCourse.getQuzs();
                categoryName = "Quiz";
            } else if (category == Category.PROJECT) {
                works = theCourse.getProjs();
                categoryName = "Project";
            } else if (category == Category.ASSIGNMENT) {
                works = theCourse.getAssigs();
                categoryName = "Assignment";
            } else if (category == Category.EXTRA) {
                works = theCourse.getExtra();
                categoryName = "Extra";
            } else {
                works = null;
                categoryName = "";
            }
        }

        if (works != null) {
            if (adapter == null) {
                adapter = new WorkAdapter(works);
                categoryRecyclerView.setAdapter(adapter);
            } else {
                adapter.setWorks(works);
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // make it take up the whole space
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        categoryTitleTextView = (TextView) view.findViewById(R.id.category_title);

        categoryTitleTextView.setText(categoryName);

        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        toolbar.findViewById(R.id.toolbar_back_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            }
        });

        toolbar.findViewById(R.id.toolbar_add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(IndividualActivity.newIntent(getActivity(), null, categoryName, courseId), REQUEST_CODE_CREATE_WORK);
            }
        });

        TextView center_title = (TextView) toolbar.findViewById(R.id.category_title);
        if (category != null) {
            if (category == Category.EXAM) {
                center_title.setText("Exams");
            } else if (category == Category.QUIZ) {
                center_title.setText("Quizzes");
            } else if (category == Category.PROJECT) {
                center_title.setText("Projects");
            } else if (category == Category.ASSIGNMENT) {
                center_title.setText("Assignments");
            } else if (category == Category.EXTRA) {
                center_title.setText("Extra Credits");
            }
        }
        updateUI();

        return view;
    }

    private class WorkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private TextView dueTextView;
        private TextView pointsTextView;
        private TextView possibleTextView;


        private Work work;

        public WorkHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            nameTextView = (TextView) itemView.findViewById(R.id.list_item_category_name);
            dueTextView = (TextView) itemView.findViewById(R.id.list_item_category_due);
            pointsTextView = (TextView) itemView.findViewById(R.id.list_item_category_points);
            possibleTextView = (TextView) itemView.findViewById(R.id.list_item_category_possible);


        }


        public void bindWork(Work work) {
            this.work = work;

            SimpleDateFormat df = new SimpleDateFormat("MM/dd");

            nameTextView.setText(work.getTitle());
            dueTextView.setText("" + df.format(work.getDueDate())); // fixed
            possibleTextView.setText("" + work.getPossible_points());
            pointsTextView.setText("" + work.getEarned_points());
        }


        @Override
        public void onClick(View view) {

            startActivityForResult(IndividualActivity.newIntent(getActivity(), work.getTitle(), categoryName, courseId), REQUEST_CODE_CREATE_WORK);
//????
//            Intent intent = IndividualActivity.newIntent(getActivity(), work.getTitle());
//
//            startActivityForResult(intent, REQUEST_CODE_CREATE_WORK);
        }
    }


    private class WorkAdapter extends RecyclerView.Adapter<WorkHolder> {
        private List<Work> works;

        public WorkAdapter(List<Work> works) {
            this.works = works;
        }

        public void setWorks(List<Work> works) {
            this.works = works;
        }

        @Override
        public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_category, parent, false);
            return new WorkHolder(view);
        }

        @Override
        public void onBindViewHolder(WorkHolder holder, int position) {
            Work work = works.get(position);
            holder.bindWork(work);
        }

        @Override
        public int getItemCount() {
            return works.size();
        }
    }

    public void onBackPressed() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }
}


