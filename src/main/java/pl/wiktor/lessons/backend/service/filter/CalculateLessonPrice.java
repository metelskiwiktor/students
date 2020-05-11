package pl.wiktor.lessons.backend.service.filter;

import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.model.Student;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

final class CalculateLessonPrice {
    private List<Lesson> lessons;
    private Builder builder = new Builder();

    private CalculateLessonPrice(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    static Builder of(List<Lesson> lessons){
        CalculateLessonPrice calculateLessonPrice = new CalculateLessonPrice(lessons);
        return calculateLessonPrice.builder;
    }

    public class Builder{
        private boolean configPaid;
        private boolean configTakenPlace;
        private boolean configStudent;
        private boolean paid;
        private boolean takenPlace;
        private Student student;
        private LocalDate start;
        private LocalDate end;

        {
            start = LocalDate.MIN;
            end = LocalDate.MAX;
        }

        Builder setDate(LocalDate start, LocalDate end){
            this.start = start;
            this.end = end;
            return this;
        }

        Builder setPaid(boolean paid){
            this.paid = paid;
            this.configPaid = true;
            return this;
        }

        Builder setTakenPlace(boolean takenPlace){
            this.takenPlace = takenPlace;
            this.configTakenPlace = true;
            return this;
        }

        public Builder setStudent(Student student){
            this.student = student;
            this.configStudent = true;
            return this;
        }

        BigDecimal calculate(){
            return new Calculate().calculate();
        }

        private class Calculate{
            private BigDecimal calculate(){
                return lessons.stream()
                        .filter(date())
                        .filter(isPaid())
                        .filter(isTakenPlace())
                        .filter(student())
                        .map(Lesson::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            private Predicate<Lesson> date(){
                return lesson -> lesson.getStartLesson().isAfter(start.atStartOfDay()) && lesson.getEndLesson().isBefore(end.atStartOfDay());
            }

            private Predicate<Lesson> isPaid(){
                if(!configPaid){
                    return lesson -> true;
                }
                return lesson -> lesson.isPaid() == paid;
            }

            private Predicate<Lesson> isTakenPlace() {
                if(!configTakenPlace){
                    return lesson -> true;
                }
                return lesson -> lesson.isTakePlace() == takenPlace;
            }

            private Predicate<Lesson> student(){
                if(!configStudent){
                    return lesson -> true;
                }

                return lesson -> lesson.getStudent().equals(student);
            }
        }
    }
}
