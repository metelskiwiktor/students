package pl.wiktor.lessons.backend.model;

import pl.wiktor.lessons.backend.model.Student;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Lesson {
    private final DateTimeFormatter formatterNoDays = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    private static int nextId;

    private int id;
    private Student student;
    private LocalDateTime startLesson;
    private LocalDateTime endLesson;
    private boolean paid;
    private boolean takePlace;
    private BigDecimal totalPrice;

    public Lesson() {
    }

    public Lesson(Student student, LocalDateTime startLesson, LocalDateTime endLesson) {
        this.student = student;
        this.startLesson = startLesson;
        this.endLesson = endLesson;
        this.paid = false;
        this.takePlace = false;
        setTotalPrice();
        id = nextId++;
    }

    private void setTotalPrice(){
        BigDecimal lessonPricePerMinute = student.getCostPerHouse().divide(new BigDecimal(Duration.ofHours(1).toMinutes()), MathContext.DECIMAL128);
        double lessonDurationInMinutes = Duration.between(startLesson, endLesson).toMinutes();

        this.totalPrice = lessonPricePerMinute.multiply(BigDecimal.valueOf(lessonDurationInMinutes)).round(MathContext.DECIMAL32);
    }

    public int hoursLessonDuration(){
        return (int) Duration.between(startLesson, endLesson).toHours();
    }

    public int minutesLessonDuration(){
        return (int) Duration.between(startLesson, endLesson).toMinutes() - hoursLessonDuration()*60;
    }

    public Duration lessonDuration(){
        return Duration.between(startLesson, endLesson);
    }

    public void makePaid(){
        this.paid = true;
    }

    public void unmakePaid(){
        this.paid = false;
    }

    public void makeTakePlace(){
        this.takePlace = true;
    }

    public void unmakeTakenPlace(){
        this.takePlace = false;
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public LocalDateTime getStartLesson() {
        return startLesson;
    }

    public LocalDateTime getEndLesson() {
        return endLesson;
    }

    public boolean isPaid() {
        return paid;
    }

    public boolean isTakePlace() {
        return takePlace;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStudentName(){
        return String.join(" ", student.getFirstName(), student.getLastName());
    }

    public String getFormattedStart() {
        return getStartLesson().format(formatterNoDays);
    }

    public String getFormattedEnd() {
        return getEndLesson().format(formatterNoDays);
    }

    public String getFormattedTotalPrice(){
        int price = this.totalPrice.intValue();
        return price + "zł";
    }

    public String getFormattedTakenPlace(){
        if(takePlace){
            return "TAK";
        }
        return "NIE";
    }

    public String getFormattedPaid(){
        if(paid){
            return "TAK";
        }
        return "NIE";
    }

    public String getFormattedDate(){
        return getStartLesson().format(formatterDay);
    }

    public String getFormattedDuration(){
        Duration duration = this.lessonDuration();
        return duration.toHours() + "h" + (duration.toMinutes() - duration.toHours()*60) + "m";
    }

    @Override
    public String toString() {
        return "Lesson{" +
                ", id=" + id +
                ", student=" + student +
                ", start=" + startLesson +
                ", end=" + endLesson +
                ", paid=" + paid +
                ", takePlace=" + takePlace +
                ", totalPrice=" + totalPrice +
                '}';
    }


}
