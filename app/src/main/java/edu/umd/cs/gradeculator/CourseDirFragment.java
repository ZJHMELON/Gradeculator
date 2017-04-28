package edu.umd.cs.gradeculator;

import android.content.Context;
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
import android.widget.EditText;
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
        Log.d("asadasd", course.getExam_weight() + "hello");
        if (Double.compare(course.getExam_weight(), 0.0) == 1) {
            // check if we have exam already
            LinearLayout exam = (LinearLayout) layout.findViewById(R.id.exam_view);
            if (exam != null) {
                TextView percentage = (TextView) exam.findViewById(R.id.exam_percentage);
                percentage.setText(String.valueOf(course.getExam_weight()) + "%");
                TextView item = (TextView) exam.findViewById(R.id.exam_item);
                if (course.getExams().size() == 0) {
                    item.setText(String.valueOf(course.getExams().size()) + " Exam");
                } else {
                    item.setText(String.valueOf(course.getExams().size()) + " Exams");
                }
                // already exist, just change
            } else {
                //add to view
                LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                View temp = inflater.inflate(R.layout.list_item_course_dir, null);
                TextView category = (TextView) temp.findViewById(R.id.category_name);
                temp.setId(R.id.exam_view);
                category.setText(R.string.exam);
                TextView percentage = (TextView) temp.findViewById(R.id.category_percentage);
                percentage.setId(R.id.exam_percentage);
                percentage.setText(String.valueOf(course.getExam_weight()) + "%");
                TextView item = (TextView) temp.findViewById(R.id.catogery_total);
                item.setId(R.id.exam_item);
                if (course.getExams().size() == 0) {
                    item.setText(String.valueOf(course.getExams().size()) + " Exam");
                } else {
                    item.setText(String.valueOf(course.getExams().size()) + " Exams");
                }
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(CategoryActivity.newIntent(getActivity(), course.getId(), Work.Category.EXAM));
                    }
                });
                layout.addView(temp);
            }
        }
        if (Double.compare(course.getQuiz_weight(), 0.0) == 1) {
            // check if we have exam already
            LinearLayout quiz = (LinearLayout) layout.findViewById(R.id.quiz_view);
            if (quiz != null) {
                TextView percentage = (TextView) quiz.findViewById(R.id.quiz_percentage);
                percentage.setText(String.valueOf(course.getQuiz_weight()) + "%");
                TextView item = (TextView) quiz.findViewById(R.id.quiz_item);
                if (course.getQuzs().size() == 0) {
                    item.setText(String.valueOf(course.getQuzs().size()) + " Quiz");
                } else {
                    item.setText(String.valueOf(course.getQuzs().size()) + " Quizzes");
                }
                // already exist, just change
            } else {
                //add to view
                LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                View temp = inflater.inflate(R.layout.list_item_course_dir, null);
                TextView category = (TextView) temp.findViewById(R.id.category_name);
                temp.setId(R.id.quiz_view);
                category.setText(R.string.quiz);
                TextView percentage = (TextView) temp.findViewById(R.id.category_percentage);
                percentage.setId(R.id.quiz_percentage);
                percentage.setText(String.valueOf(course.getQuiz_weight()) + "%");
                TextView item = (TextView) temp.findViewById(R.id.catogery_total);
                item.setId(R.id.quiz_item);
                if (course.getQuzs().size() == 0) {
                    item.setText(String.valueOf(course.getQuzs().size()) + " Quiz");
                } else {
                    item.setText(String.valueOf(course.getQuzs().size()) + " Quizzes");
                }
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(CategoryActivity.newIntent(getActivity(), course.getId(), Work.Category.QUIZ));
                    }
                });
                layout.addView(temp);
            }
        }
        if (Double.compare(course.getAssignment_weight(), 0.0) == 1) {
            // check if we have exam already
            LinearLayout assignment = (LinearLayout) layout.findViewById(R.id.assignment_view);
            if (assignment != null) {
                TextView percentage = (TextView) assignment.findViewById(R.id.assignment_percentage);
                percentage.setText(String.valueOf(course.getAssignment_weight()) + "%");
                TextView item = (TextView) assignment.findViewById(R.id.assignment_item);
                if (course.getAssigs().size() == 0) {
                    item.setText(String.valueOf(course.getAssigs().size()) + " Assignment");
                } else {
                    item.setText(String.valueOf(course.getAssigs().size()) + " Assignments");
                }
                // already exist, just change
            } else {
                //add to view
                LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                View temp = inflater.inflate(R.layout.list_item_course_dir, null);
                TextView category = (TextView) temp.findViewById(R.id.category_name);
                temp.setId(R.id.assignment_view);
                category.setText(R.string.assignment);
                TextView percentage = (TextView) temp.findViewById(R.id.category_percentage);
                percentage.setId(R.id.assignment_percentage);
                percentage.setText(String.valueOf(course.getAssignment_weight()) + "%");
                TextView item = (TextView) temp.findViewById(R.id.catogery_total);
                item.setId(R.id.assignment_item);
                if (course.getAssigs().size() == 0) {
                    item.setText(String.valueOf(course.getAssigs().size()) + " Assignment");
                } else {
                    item.setText(String.valueOf(course.getAssigs().size()) + " Assignments");
                }
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(CategoryActivity.newIntent(getActivity(), course.getId(), Work.Category.ASSIGNMENT));
                    }
                });
                layout.addView(temp);
            }
        }
        if (Double.compare(course.getProject_weight(), 0.0) == 1) {
            // check if we have exam already
            LinearLayout project = (LinearLayout) layout.findViewById(R.id.project_view);
            if (project != null) {
                TextView percentage = (TextView) project.findViewById(R.id.project_percentage);
                percentage.setText(String.valueOf(course.getProject_weight()) + "%");
                TextView item = (TextView) project.findViewById(R.id.project_item);
                if (course.getProjs().size() == 0) {
                    item.setText(String.valueOf(course.getProjs().size()) + " Project");
                } else {
                    item.setText(String.valueOf(course.getProjs().size()) + " Projects");
                }
                // already exist, just change
            } else {
                //add to view
                LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                View temp = inflater.inflate(R.layout.list_item_course_dir, null);
                TextView category = (TextView) temp.findViewById(R.id.category_name);
                temp.setId(R.id.project_view);
                category.setText(R.string.project);
                TextView percentage = (TextView) temp.findViewById(R.id.category_percentage);
                percentage.setId(R.id.project_percentage);
                percentage.setText(String.valueOf(course.getProject_weight()) + "%");
                TextView item = (TextView) temp.findViewById(R.id.catogery_total);
                item.setId(R.id.project_item);
                if (course.getProjs().size() == 0) {
                    item.setText(String.valueOf(course.getProjs().size()) + " Project");
                } else {
                    item.setText(String.valueOf(course.getProjs().size()) + " Projects");
                }
                temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(CategoryActivity.newIntent(getActivity(), course.getId(), Work.Category.PROJECT));
                    }
                });
                layout.addView(temp);
            }
        }
        if (Double.compare(course.getExtra_weight(), 0.0) == 1) {
            // check if we have exam already
            LinearLayout extra_credit = (LinearLayout) layout.findViewById(R.id.extracredit_view);
            if (extra_credit != null) {
                TextView percentage = (TextView) extra_credit.findViewById(R.id.extracredit_percentage);
                percentage.setText(String.valueOf(course.getExtra_weight()) + "%");
                TextView item = (TextView) extra_credit.findViewById(R.id.extracredit_item);
                if (course.getExtra().size() == 0) {
                    item.setText(String.valueOf(course.getExtra().size()) + " Extra Credit Assignment");
                } else {
                    item.setText(String.valueOf(course.getExtra().size()) + " Extra Credit Assignments");
                    // already exist, just change
                }
            } else {
                //add to view
                LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                View temp = inflater.inflate(R.layout.list_item_course_dir, null);
                TextView category = (TextView) temp.findViewById(R.id.category_name);
                temp.setId(R.id.extracredit_view);
                category.setText(R.string.extra_credit);
                TextView percentage = (TextView) temp.findViewById(R.id.category_percentage);
                percentage.setId(R.id.extracredit_percentage);
                percentage.setText(String.valueOf(course.getExtra_weight()) + "%");
                TextView item = (TextView) temp.findViewById(R.id.catogery_total);
                item.setId(R.id.extracredit_item);
                if (course.getExtra().size() == 0) {
                    item.setText(String.valueOf(course.getExtra().size()) + " Extra Credit Assignment");
                } else {
                    item.setText(String.valueOf(course.getExtra().size()) + " Extra Credit Assignments");
                    temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(CategoryActivity.newIntent(getActivity(), course.getId(), Work.Category.EXTRA));
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
