package edu.umd.cs.gradeculator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.service.CourseService;
import edu.umd.cs.gradeculator.service.ItemTouchHelperAdapter;

/**
 * Created by howardksw1 on 4/21/17.
 */

// hihihihihi test 101
public class MainFragment extends Fragment{

    private List<Course> all_course;
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
        setHasOptionsMenu(true);
        courseService = DependencyFactory.getCourseService(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        courseRecyclerView = (RecyclerView) view.findViewById(R.id.course_recycler_view);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_create_course:
                Intent createCourseIntent = new Intent(getActivity(), AddCourseActivity.class);
                startActivityForResult(createCourseIntent, REQUEST_CODE_CREATE_COURSE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
//            Toast.makeText(getActivity(), course.getTitle() + "is selected", Toast.LENGTH_SHORT).show();
            Intent intent = CourseDirActivity.newIntent(getActivity(), course.getId());

            startActivityForResult(intent, REQUEST_CODE_EDIT_COURSE);
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> implements ItemTouchHelperAdapter {
        private List<Course> courses;

        public CourseAdapter(List<Course> courses) {
            this.courses = courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_course, parent, false);
            return new CourseHolder(view);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course course = courses.get(position);
            holder.bindCourse(course);
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(courses, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(courses, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            courses.remove(position);
            DependencyFactory.getCourseService(getActivity()).remove_course(position);
            notifyItemRemoved(position);
        }
    }


    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final ItemTouchHelperAdapter mAdapter;
//        private List<Course> courses = null;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            mAdapter = adapter;
//            courses = DependencyFactory.getCourseService(getActivity()).getAllCourses();
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
                        }
                    })
                    .setNegativeButton(R.string.cancel_remove, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            updateUI();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }
}