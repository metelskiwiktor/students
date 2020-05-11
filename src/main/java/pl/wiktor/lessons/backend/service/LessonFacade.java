package pl.wiktor.lessons.backend.service;

import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.model.info.MonthSummaryInfo;
import pl.wiktor.lessons.backend.model.Student;
import pl.wiktor.lessons.backend.model.info.TableStudentInfo;
import pl.wiktor.lessons.backend.service.filter.LessonFilters;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LessonFacade {
    private LessonService lessonService = new LessonService();
    private StudentService studentService = new StudentService();

    public LessonFacade() {

    }

    public void createStudent(Student student) {
        this.studentService.addStudent(student);
    }

    public void deleteStudent() {

    }

    public void editStudent(Student student) {
        studentService.edit(student);
    }

    public List<Lesson> getLessons(LocalDate date) {
        return lessonService.getLessonFilters().getLessons(date);
    }

    public void addLesson(Lesson lesson) {
        lessonService.addLesson(lesson);
    }

    public void deleteLesson(Lesson lesson) {
        lessonService.deleteLesson(lesson);
    }

    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    public List<String> getStudentsName(){
        return studentService.getStudents().stream().map(Student::getName).collect(Collectors.toList());
    }

    public String earnedStudent(Student student){
        return getFormattedPrice(lessonService.getLessonFilters().getActualEarnedPrice(student));
    }

    public List<Lesson> getUnpaidLessons(Student student){
        return lessonService.getLessonFilters().getUnpaidLessons(student);
    }

    public Map<Student, TableStudentInfo> studentsInfo(LocalDate date) {
        LocalDate start = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate end = LocalDate.of(date.getYear(), date.getMonth().plus(1), 1);

        Map<Student, TableStudentInfo> studentsMap = new HashMap<>();
        studentService.getStudents().forEach(student -> {
            LessonFilters lessonFilters = lessonService.getLessonFilters();

            String countTakeLesson = String.valueOf(lessonFilters.countTakenPlaceLessons(start, end, student));
            String earned = getFormattedPrice(lessonFilters.getActualEarnedPrice(start, end, student));
            String unpaid = getFormattedPrice(lessonFilters.getUnpaidPrice(start, end, student));
            String expected = getFormattedPrice(lessonFilters.getExpectedPrice(start, end, student));
            String lessonPlanned = String.valueOf(lessonFilters.countPlannedLessons(start, end, student));
            Duration lessonDuration = lessonFilters.getDurationOfTakenLessons(start, end, student);

            TableStudentInfo tableStudentInfo = new TableStudentInfo(countTakeLesson, earned, unpaid, expected, lessonPlanned, getFormattedDuration(lessonDuration));

            studentsMap.put(student, tableStudentInfo);
        });

        return studentsMap;
    }

    private String getFormattedDuration(Duration duration){
        return duration.toHours() + "h" + (duration.toMinutes() - duration.toHours()*60) + "m";
    }

    public MonthSummaryInfo monthSummaryInfo(LocalDate date){
        LocalDate start = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate end = LocalDate.of(date.getYear(), date.getMonth().plus(1), 1);

        LessonFilters lessonFilters = lessonService.getLessonFilters();

        String countTakenPlaceLessons = String.valueOf(lessonFilters.countTakenPlaceLessons(start, end));
        String countExpectedLessons = String.valueOf(lessonFilters.countExpectedLessons(start, end));
        String expectedEarn = getFormattedPrice(lessonFilters.getExpectedPrice(start, end));
        String earned = getFormattedPrice(lessonFilters.getActualEarnedPrice(start, end));
        String unpaid = getFormattedPrice(lessonFilters.getUnpaidPrice(start, end));

        return new MonthSummaryInfo(countTakenPlaceLessons, countExpectedLessons, expectedEarn, earned, unpaid);
    }

    private String getFormattedPrice(BigDecimal price) {
        return price.intValue() + " zł";
    }

    public List<Lesson> getLessons(Student student) {
        return lessonService.getLessonFilters().getLessons(student);
    }

    public List<Lesson> getUnpaidLessons() {
        return lessonService.getLessonFilters().getUnpaidLessons();
    }

    public void editLesson(Lesson lesson) {
        lessonService.editLesson(lesson);
    }
}
