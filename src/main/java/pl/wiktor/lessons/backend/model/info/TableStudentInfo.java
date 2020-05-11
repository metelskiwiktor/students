package pl.wiktor.lessons.backend.model.info;

public class TableStudentInfo {
    private String countTakeLesson;
    private String paid;
    private String unpaid;
    private String expectedEarn;
    private String lessonPlanned;
    private String takenLessonDuration;

    public TableStudentInfo(String countTakeLesson, String paid, String unpaid, String expectedEarn, String lessonPlanned, String takenLessonDuration) {
        this.countTakeLesson = countTakeLesson;
        this.paid = paid;
        this.unpaid = unpaid;
        this.expectedEarn = expectedEarn;
        this.lessonPlanned = lessonPlanned;
        this.takenLessonDuration = takenLessonDuration;
    }

    public String getCountTakenLessons() {
        return countTakeLesson;
    }

    public String getPaid() {
        return paid;
    }

    public String getUnpaid() {
        return unpaid;
    }

    public String getExpectedEarn() {
        return expectedEarn;
    }

    public String getLessonPlanned() {
        return lessonPlanned;
    }

    public String getTakenLessonDuration() {
        return takenLessonDuration;
    }
}
