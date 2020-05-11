package pl.wiktor.lessons.backend.translator;

import java.time.Month;

public enum MonthTranslator {
    JANUARY("styczeń"), FEBRUARY("luty"), MARCH("marzec"),
    APRIL("kwiecień"), MAY("maj"), JUNE("czerwiec"),
    JULY("lipiec"), AUGUST("sierpień"), SEPTEMBER("wrzesień"),
    OCTOBER("październik"), NOVEMBER("listopad"), DECEMBER("grudzień");

    private String name;

    MonthTranslator(String monthName) {
        this.name = monthName;
    }

    private static String getNameByMonth(Month month) {
        return MonthTranslator.valueOf(month.name()).name;
    }

    public static String getFormattedName(Month month){
        String name = getNameByMonth(month);
        char[] nameChars = name.toCharArray();
        nameChars[0] = Character.toUpperCase(nameChars[0]);

        return String.valueOf(nameChars);
    }
}