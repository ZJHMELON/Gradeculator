package edu.umd.cs.gradeculator;

import static org.junit.Assert.*;

import java.util.List;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.Test;

import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.impl.InMemoryCourseService;

public class test {

	@SuppressWarnings("deprecation")
	@Test
	public void test1() {
		Date date = new Date();
		date.setDate(11);
		date.setMonth(0);
		date.setHours(11);
		date.setMinutes(59);
		date.setSeconds(59);
		Context a = null;
		InMemoryCourseService b=new InMemoryCourseService(a);
		
		Work a1 = new Work("as1");
		Work a2 = new Work("ex2");
		Work a3 = new Work("proj3");
		Work a4 = new Work("quz4");
		Work a5 = new Work("extra5");
		a1.setCategory(Work.Category.ASSIGNMENT);
		a2.setCategory(Work.Category.EXAM);
		a3.setCategory(Work.Category.PROJECT);
		a4.setCategory(Work.Category.QUIZ);
		a5.setCategory(Work.Category.EXTRA);
		a1.setDueDate(date);
		a2.setDueDate(date);
		a3.setDueDate(date);
		a4.setDueDate(date);
		a5.setDueDate(date);
		a1.setPossible_points(100);
		a2.setPossible_points(100);
		a3.setPossible_points(100);
		a4.setPossible_points(100);
		a5.setPossible_points(100);
		a1.setWeight(0.25);
		a2.setWeight(0.25);
		a3.setWeight(0.25);
		a4.setWeight(0.25);
		a5.setWeight(0.01);
		
		Course nCourse1 = new Course("Code God", "CMSC999", 60);
		nCourse1.add(a1);
		nCourse1.add(a2);
		nCourse1.add(a3);
		nCourse1.add(a4);
		nCourse1.add(a5);
		Course nCourse2 = new Course("Code noob", "CMSC000", 70);
		nCourse2.add(a1);
		nCourse2.add(a2);
		nCourse2.add(a3);
		nCourse2.add(a4);
		nCourse2.add(a5);
		Course nCourse3 = new Course("Code it", "CMSC222", 80);
		nCourse3.add(a1);
		nCourse3.add(a2);
		nCourse3.add(a3);
		nCourse3.add(a4);
		nCourse3.add(a5);
		Course nCourse4 = new Course("Code death", "CMSC444", 90);
		nCourse4.add(a1);
		nCourse4.add(a2);
		nCourse4.add(a3);
		nCourse4.add(a4);
		nCourse4.add(a5);
		Course nCourse5 = new Course("Code more", "CMSC666", 100);
		nCourse5.add(a1);
		nCourse5.add(a2);
		nCourse5.add(a3);
		nCourse5.add(a4);
		nCourse5.add(a5);
		
		b.addCourseToBacklog(nCourse1);
		b.addCourseToBacklog(nCourse2);
		b.addCourseToBacklog(nCourse3);
		b.addCourseToBacklog(nCourse4);
		b.addCourseToBacklog(nCourse5);
		assertEquals("	Assigs\n"
				+ "			Title:as1	points100.0/0.0-0.25->Wed Jan 11 11:59:59 EST 2017\n"
				+ "	Exams\n"
				+ "			Title:ex2	points100.0/0.0-0.25->Wed Jan 11 11:59:59 EST 2017\n"
				+ "	Projs\n"
				+ "			Title:proj3	points100.0/0.0-0.25->Wed Jan 11 11:59:59 EST 2017\n"
				+ "	Quzs\n"
				+ "			Title:quz4	points100.0/0.0-0.25->Wed Jan 11 11:59:59 EST 2017\n"
				+ "	Extras\n"
				+ "			Title:extra5	points100.0/0.0-0.01->Wed Jan 11 11:59:59 EST 2017\n", nCourse1.toString());
		String[] cs=new String[5];
		cs[0]="Code noob";
		cs[1]="Code it";
		cs[2]="Code death";
		cs[3]="Code more";
		cs[4]="Code God";
		List<Course> css = b.getAllCourses();
		for(int i=0;i<b.getAllCourses().size();i++){
			assertEquals(css.get(i).getTitle(),cs[i]);
		}
		nCourse1.remove(Category.EXAM, "ex2");
		System.out.print(nCourse1);
	}

}
