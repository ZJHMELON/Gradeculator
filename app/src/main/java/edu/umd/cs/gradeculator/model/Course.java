package edu.umd.cs.gradeculator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import edu.umd.cs.gradeculator.model.Work.Category;

public class Course implements Serializable {
	private String id;
	private String title;
	private String identifier;
	private ArrayList<Work> exams;
	private ArrayList<Work> quizzes;
	private ArrayList<Work> projects;
	private ArrayList<Work> assignments;
	private ArrayList<Work> extra;
	private double exam_weight;
	private double quiz_weight;
	private double project_weight;
	private double assignment_weight;
	private double extra_weight;
	private Grade grade = Grade.A; // default
	private double current_grade;
	private double desire_grade;
	private int credit;

	private boolean setGrade = false;
	// check if we set grade yet, only after seting grade we can fill in desired grade

	public Course() {
		id = UUID.randomUUID().toString();
		exams = new ArrayList<>();
		quizzes = new ArrayList<>();
		projects = new ArrayList<>();
		assignments = new ArrayList<>();
		extra = new ArrayList<>();
		exam_weight = quiz_weight = assignment_weight = project_weight = extra_weight = 0.0;
	}

	public Course(String title, String identifier, Grade grade, int credit) {
		id = UUID.randomUUID().toString();
		this.title = title;
		this.identifier = identifier;
		this.grade = grade;
		this.credit = credit;
		exams = new ArrayList<>();
		quizzes = new ArrayList<>();
		projects = new ArrayList<>();
		assignments = new ArrayList<>();
		extra = new ArrayList<>();
		setDesire_grade(grade);
		exam_weight = quiz_weight = assignment_weight = project_weight = extra_weight = 0.0;
	}

	public double getExam_weight() {
		return exam_weight;
	}

	public double getQuiz_weight() {
		return quiz_weight;

	}

	public double getAssignment_weight() {
		return assignment_weight;
	}

	public double getProject_weight() {
		return project_weight;
	}

	public double getExtra_weight() {
		return extra_weight;
	}

	public void setExam_weight(double weight) {
		exam_weight = weight;
	}

	public void setQuiz_weight(double weight) {
		quiz_weight = weight;
	}

	public void setAssignments_weight(double weight) {
		assignment_weight = weight;
	}

	public void setProject_weight(double weight) {
		project_weight = weight;
	}

	public void setExtra_weight(double weight) {
		extra_weight = weight;
	}

	public ArrayList<Work> getExams() {
		return exams;
	}

	public ArrayList<Work> getQuzs() {
		return quizzes;
	}

	public ArrayList<Work> getProjs() {
		return projects;
	}

	public ArrayList<Work> getAssigs() {
		return assignments;
	}

	public ArrayList<Work> getExtra() {
		return extra;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public double getCredit() {
		return credit;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public int getGradePosition() {
		switch (grade) {
			case A_PLUS:
				return 0;
			case A:
				return 1;
			case A_MINUS:
				return 2;
			case B_PLUS:
				return 3;
			case B:
				return 4;
			case B_MINUS:
				return 5;
			case C_PLUS:
				return 6;
			case C:
				return 7;
			case C_MINUS:
				return 8;
			default:
				return 1;
		}
	}

	public void setGrade(int position) {
		switch (position) {
			case 0:
				this.grade = grade.A_PLUS;
				break;
			case 1:
				this.grade = grade.A;
				break;
			case 2:
				this.grade = grade.A_MINUS;
				break;
			case 3:
				this.grade = grade.B_PLUS;
				break;
			case 4:
				this.grade = grade.B;
				break;
			case 5:
				this.grade = grade.B_MINUS;
				break;
			case 6:
				this.grade = grade.C_PLUS;
				break;
			case 7:
				this.grade = grade.C;
				break;
			case 8:
				this.grade = grade.C_MINUS;
				break;
			default:
				this.grade = grade.A;
				break;
		}
	}


	public void setCurrent_grade(double grade) {
		this.current_grade = grade;
	}

	public double getDesire_grade() {
		return desire_grade;
	}


	public void setDesire_grade(Grade grade) {
		switch (grade) {
			case A_PLUS:
				this.desire_grade = 97;
			case A:
				this.desire_grade = 93;
			case A_MINUS:
				this.desire_grade = 90;
			case B_PLUS:
				this.desire_grade = 87;
			case B:
				this.desire_grade = 83;
			case B_MINUS:
				this.desire_grade = 80;
			case C_PLUS:
				this.desire_grade = 77;
			case C:
				this.desire_grade = 73;
			case C_MINUS:
				this.desire_grade = 70;
			default:
				this.desire_grade = 93;
		}
	}

	public double getCurrent_grade() {
		return current_grade;
	}

	// check if the work element exists in the works data structure
	private Work containsWork(ArrayList<Work> works, String title) {
		for (Work w : works) {
			if (w.getTitle().equals(title)) {
				return w;
			}
		}
		return null;
	}

	// add a work into the data structure depending on the categories
	public boolean add(Work work) {
		switch (work.getCategory()) {
			case EXAM:
				if (containsWork(exams, work.getTitle()) == null) {
					exams.add(work);
					return true;
				}
				break;
			case ASSIGNMENT:
				if (containsWork(assignments, work.getTitle()) == null) {
					assignments.add(work);
					return true;
				}
				break;
			case QUIZ:
				if (containsWork(quizzes, work.getTitle()) == null) {
					quizzes.add(work);
					return true;
				}
				break;
			case PROJECT:
				if (containsWork(projects, work.getTitle()) == null) {
					projects.add(work);
					return true;
				}
				break;
			case EXTRA:
				if (containsWork(extra, work.getTitle()) == null) {
					extra.add(work);
					return true;
				}
				break;
			default:
				break;
		}
		return false;
	}

	// remove helper function to iterate through the data structure and locate the position
	private ArrayList<Work> removeWork(ArrayList<Work> works, String title) {
		for (int i = 0; i < works.size(); i++) {
			if (works.get(i).getTitle().equals(title)) {
				works.remove(works.get(i));
			}
		}
		return works;
	}

	// remove the item by the type of categories
	public void remove(Work.Category work, String target) {
		switch (work) {
			case EXAM:
				exams = removeWork(exams, target);
				break;
			case ASSIGNMENT:
				assignments = removeWork(assignments, target);
				break;
			case QUIZ:
				quizzes = removeWork(quizzes, target);
				break;
			case PROJECT:
				projects = removeWork(projects, target);
				break;
			case EXTRA:
				extra = removeWork(extra, target);
				break;
			default:
				break;
		}
	}

	public String toString() {
		String out = "";
		out += "	Assigs\n";
		for (Work work : this.getAssigs()) {
			out += "			";
			out += work;
			out += "\n";
		}
		out += "	Exams\n";
		for (Work work : this.getExams()) {
			out += "			";
			out += work;
			out += "\n";
		}
		out += "	Projs\n";
		for (Work work : this.getProjs()) {
			out += "			";
			out += work;
			out += "\n";
		}
		out += "	Quzs\n";
		for (Work work : this.getQuzs()) {
			out += "			";
			out += work;
			out += "\n";
		}
		out += "	Extras\n";
		for (Work work : this.getExtra()) {
			out += "			";
			out += work;
			out += "\n";
		}
		return out;
	}

	public double getCtGrade(Category cate) {
		double sum = 0;
		double tempGrade = 0;
		double allGrade = 0;
		ArrayList<Work> sumList;
		switch (cate) {
			case ASSIGNMENT:
				break;

		}
		for (Work work : exams) {
			sum += work.getWeight();
		}
		if (Double.compare(sum, getExam_weight()) == 0) {
			for (Work work : exams) {
				tempGrade += work.getEarned_points();
				allGrade += work.getPossible_points();
			}
			return tempGrade / allGrade;
		}
		return 0.0;
	}

	public enum Grade {
		A_PLUS, A, A_MINUS, B_PLUS, B, B_MINUS, C_PLUS, C, C_MINUS ;
	}
}

