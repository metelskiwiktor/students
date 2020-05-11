package pl.wiktor.lessons.backend.model.info;

public class MonthSummaryInfo {
    private String countTakenPlaceLessons;
    private String countExpectedLessons;
    private String expectedEarn;
    private String earned;
    private String unpaid;

    public MonthSummaryInfo(String countTakenPlaceLessons, String countExpectedLessons, String expectedEarn, String earned, String unpaid) {
        this.countTakenPlaceLessons = countTakenPlaceLessons;
        this.countExpectedLessons = countExpectedLessons;
        this.expectedEarn = expectedEarn;
        this.earned = earned;
        this.unpaid = unpaid;
    }

    public String getCountTakenPlaceLessons() {
        return countTakenPlaceLessons;
    }

    public String getCountExpectedLessons() {
        return countExpectedLessons;
    }

    public String getExpectedEarn() {
        return expectedEarn;
    }

    public String getEarned() {
        return earned;
    }

    public String getUnpaid() {
        return unpaid;
    }
}
