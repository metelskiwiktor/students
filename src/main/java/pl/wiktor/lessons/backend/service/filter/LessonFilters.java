package pl.wiktor.lessons.backend.service.filter;

import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.model.Student;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LessonFilters {
    private List<Lesson> lessons;

    public LessonFilters(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public BigDecimal getExpectedPrice(LocalDate start, LocalDate end) {
        return CalculateLessonPrice.of(lessons)
                .setDate(start, end)
                .calculate();
    }

    public BigDecimal getExpectedPrice(LocalDate start, LocalDate end, Student student) {
        return CalculateLessonPrice.of(lessons)
                .setDate(start, end)
                .setStudent(student)
                .calculate();
    }

    public BigDecimal getExpectedPrice(Student student) {
        return CalculateLessonPrice.of(lessons)
                .setStudent(student)
                .calculate();
    }

    public BigDecimal getActualEarnedPrice(LocalDate start, LocalDate end) {
        return CalculateLessonPrice.of(lessons)
                .setDate(start, end)
                .setPaid(true)
                .calculate();
    }

    public BigDecimal getActualEarnedPrice(LocalDate start, LocalDate end, Student student) {
        return CalculateLessonPrice.of(lessons)
                .setDate(start, end)
                .setPaid(true)
                .setStudent(student)
                .calculate();
    }

    public BigDecimal getActualEarnedPrice(Student student) {
        return CalculateLessonPrice.of(lessons)
                .setPaid(true)
                .setStudent(student)
                .calculate();
    }

    public BigDecimal getUnpaidPrice(LocalDate start, LocalDate end) {
        return CalculateLessonPrice.of(lessons)
                .setDate(start, end)
                .setPaid(false)
                .setTakenPlace(true)
                .calculate();
    }

    public BigDecimal getUnpaidPrice(LocalDate start, LocalDate end, Student student) {
        return CalculateLessonPrice.of(lessons)
                .setDate(start, end)
                .setPaid(false)
                .setTakenPlace(true)
                .setStudent(student)
                .calculate();
    }

    public BigDecimal getUnpaidPrice(Student student) {
        return CalculateLessonPrice.of(lessons)
                .setPaid(false)
                .setTakenPlace(true)
                .setStudent(student)
                .calculate();
    }

    public List<Lesson> getLessons(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return getLessons(start, end);
    }

    public List<Lesson> getLessons(LocalDateTime start, LocalDateTime end){
        return lessons.stream()
                .filter(lesson -> lesson.getStartLesson().isAfter(start) && lesson.getStartLesson().isBefore(end))
                .collect(Collectors.toList());
    }

    public int countLessons(Student student){
        return (int) lessons.stream()
                .filter(lesson -> lesson.getStudent().equals(student))
                .count();
    }

    public int countTakenPlaceLessons(Student student){
        return (int) lessons.stream()
                .filter(lesson -> lesson.getStudent().equals(student))
                .filter(Lesson::isTakePlace)
                .count();
    }

    public int countTakenPlaceLessons(LocalDate start, LocalDate end, Student student) {
        return (int) lessons.stream()
                .filter(lesson -> lesson.getStartLesson().isAfter(start.atStartOfDay()) && lesson.getEndLesson().isBefore(end.atStartOfDay()))
                .filter(lesson -> lesson.getStudent().equals(student))
                .filter(Lesson::isTakePlace)
                .count();
    }

    public int getCountPlannedLessons(Student student) {
        return (int) lessons.stream()
                .filter(lesson -> lesson.getStudent().equals(student))
                .filter(lesson -> !lesson.isTakePlace())
                .count();
    }

    public int countPlannedLessons(LocalDate start, LocalDate end, Student student) {
        return (int) lessons.stream()
                .filter(lesson -> lesson.getStartLesson().isAfter(start.atStartOfDay()) && lesson.getEndLesson().isBefore(end.atStartOfDay()))
                .filter(lesson -> lesson.getStudent().equals(student))
                .filter(lesson -> !lesson.isTakePlace())
                .count();
    }

    public int countTakenPlaceLessons(LocalDate start, LocalDate end) {
        return (int) lessons.stream()
                .filter(lesson -> lesson.getStartLesson().isAfter(start.atStartOfDay()) && lesson.getEndLesson().isBefore(end.atStartOfDay()))
                .filter(Lesson::isTakePlace)
                .count();
    }

    public int countExpectedLessons(LocalDate start, LocalDate end) {
        return (int) lessons.stream()
                .filter(lesson -> lesson.getStartLesson().isAfter(start.atStartOfDay()) && lesson.getEndLesson().isBefore(end.atStartOfDay()))
                .filter(lesson -> !lesson.isTakePlace() )
                .count();
    }

    public List<Lesson> getUnpaidLessons(Student student) {
        return lessons.stream()
                .filter(lesson -> lesson.getStudent().equals(student))
                .filter(lesson -> !lesson.isPaid() && lesson.isTakePlace())
                .collect(Collectors.toList());
    }

    public List<Lesson> getLessons(Student student) {
        return lessons.stream()
                .filter(lesson -> lesson.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    public List<Lesson> getUnpaidLessons() {
        return lessons.stream()
                .filter(lesson -> !lesson.isPaid() && lesson.isTakePlace())
                .collect(Collectors.toList());
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Duration getDurationOfLessons(LocalDate start, LocalDate end){
        return getLessons(start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream()
                .map(Lesson::lessonDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public Duration getDurationOfTakenLessons(LocalDate start, LocalDate end, Student student) {
        return getLessons(start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream()
                .filter(lesson -> lesson.getStudent().equals(student))
                .filter(Lesson::isTakePlace)
                .map(Lesson::lessonDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }
}
