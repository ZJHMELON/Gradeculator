package edu.umd.cs.gradeculator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.service.CourseService;
import edu.umd.cs.gradeculator.service.ItemTouchHelperAdapter;


/**
 * Created by howardksw1 on 4/21/17.
 */

// hihihihihi test 101
public class MainFragment extends Fragment{

    private ArrayList<Course> all_course;
    private ItemTouchHelper mItemTouchHelper;
    private final String TAG = getClass().getSimpleName();
    private CourseService courseService;
    private RecyclerView courseRecyclerView;
    private CourseAdapter adapter;
    private static final int REQUEST_CODE_CREATE_COURSE = 0;
    private static final int REQUEST_CODE_EDIT_COURSE = 1;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseService = DependencyFactory.getCourseService(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // make it take up the whole space
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        courseRecyclerView = (RecyclerView) view.findViewById(R.id.course_recycler_view);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        toolbar.findViewById(R.id.toolbar_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createCourseIntent = new Intent(getActivity(), AddCourseActivity.class);
                startActivityForResult(createCourseIntent, REQUEST_CODE_CREATE_COURSE);
            }
        });

        updateUI();

        return view;
    }


    private void updateUI() {
        Log.d(TAG, "updating UI all stories");

        all_course = courseService.getAllCourses();

        if (adapter == null) {
            adapter = new CourseAdapter(all_course);
            courseRecyclerView.setAdapter(adapter);
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(courseRecyclerView);
        } else {
            adapter.setCourses(all_course);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CREATE_COURSE) {
            if (data == null) {
                return;
            }

            Course courseCreated = AddCourseActivity.getCourseCreated(data);
            courseService.addCourseToBacklog(courseCreated);
            updateUI();
        }
        if(requestCode == REQUEST_CODE_EDIT_COURSE) {
            updateUI();
        }
    }


    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView courseTitle;
        private TextView courseIdentifier;
        private TextView currentGrade;
        private Course course;

        public CourseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            courseTitle = (TextView) itemView.findViewById(R.id.list_item_course_title);
            courseIdentifier = (TextView) itemView.findViewById(R.id.list_item_course_identifier);
            currentGrade = (TextView) itemView.findViewById(R.id.list_item_current_grade);
        }

        public void bindCourse(Course course) {
            this.course = course;

            courseTitle.setText(course.getTitle());
            courseIdentifier.setText(course.getIdentifier());
            if(Double.compare(course.getCurrent_grade(), 0.0) == 1) {
                currentGrade.setText(Double.toString(course.getCurrent_grade()));
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = CourseDirActivity.newIntent(getActivity(), course.getId());

            startActivityForResult(intent, REQUEST_CODE_EDIT_COURSE);
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> implements ItemTouchHelperAdapter {

        public CourseAdapter(ArrayList<Course> courses) {
            all_course = courses;
        }

        public void setCourses(ArrayList<Course> courses) {
            all_course = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_course, parent, false);
            return new CourseHolder(view);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course course = all_course.get(position);
            holder.bindCourse(course);
        }

        @Override
        public int getItemCount() {
            return all_course.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(all_course, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(all_course, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            DependencyFactory.getCourseService(getActivity()).remove_course(position);
            notifyItemRemoved(position);
        }
    }


    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final ItemTouchHelperAdapter mAdapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder,
                              ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to remove this course?")
                    .setPositiveButton(R.string.confirm_remove, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            mAdapter.onItemDismiss(position);
                            updateUI();
                        }
                    })
                    .setNegativeButton(R.string.cancel_remove, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }
}