package per.eter.utils.jxls.demo;


import per.eter.utils.jxls.JxlsTemplate;

import java.util.*;

//import org.jxls.transform.Transformer;
//import org.jxls.transform.poi.PoiTransformer;

public class ExcelRenderTest {
    public static void main(String[] args) throws Exception {
        List<Student> students = new ArrayList<>();

        Student student = new Student();
        student.setName("张三");
        student.setGender("男");
        student.setGradeClass("初一一班");
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setCourseName("语文");
        course.setCourseScore("98");
        courses.add(course);
        course = new Course();
        course.setCourseName("数学");
        course.setCourseScore("105");
        courses.add(course);
        course = new Course();
        course.setCourseName("物理");
        course.setCourseScore("80");
        courses.add(course);

        student.setCourses(courses);
        students.add(student);


        student = new Student();
        student.setName("王丽丽");
        student.setGender("女");
        student.setGradeClass("初一二班");
        courses = new ArrayList<>();
        course = new Course();
        course.setCourseName("语文");
        course.setCourseScore("102");
        courses.add(course);
        course = new Course();
        course.setCourseName("数学");
        course.setCourseScore("110");
        courses.add(course);
        student.setCourses(courses);
        students.add(student);

        student = new Student();
        student.setName("李梅");
        student.setGender("女");
        student.setGradeClass("初一三班");
        courses = new ArrayList<>();
        course = new Course();
        course.setCourseName("语文");
        course.setCourseScore("110");
        courses.add(course);
        course = new Course();
        course.setCourseName("数学");
        course.setCourseScore("100");
        courses.add(course);
        course = new Course();
        course.setCourseName("物理");
        course.setCourseScore("85");
        courses.add(course);
        student.setCourses(courses);
        students.add(student);

        //模板里展示的数据
        Map<String, Object> data = new HashMap<>();
        data.put("students", students);
        data.put("sts", students);

        // 模板路径和输出流
        String templatePath = "/home/eter/workspace/virtualBOx/share/exclOut/template.xlsx";

        JxlsTemplate jxlsTemplate = new JxlsTemplate(templatePath, "/home/eter/workspace/virtualBOx/share/exclOut/out.xls", data);
        jxlsTemplate.generate();
    }
}
