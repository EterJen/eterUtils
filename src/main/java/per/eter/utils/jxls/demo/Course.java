package per.eter.utils.jxls.demo;

public class Course {

    private String courseName;

    private String courseScore;

    @Override
    public String toString() {
        return "Course{" + "courseName='" + courseName + '\'' + ", courseScore='" + courseScore + '\'' + '}';
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(String courseScore) {
        this.courseScore = courseScore;
    }
}
