package pl.wiktor.lessons.ui.calendar.service.buttons;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.wiktor.lessons.backend.model.Lesson;

import java.util.*;

import static pl.wiktor.lessons.ui.calendar.service.buttons.ButtonPressQueue.*;


public enum ButtonCheck {
    PAID {
        private boolean check(Lesson lesson) {
            return lesson.isPaid();
        }

        @Override
        public List<String> keyNames(Lesson lesson) {
            List<String> keysName;

            if(check(lesson)){
                keysName = Arrays.asList(UNMAKE_PAID, MAKE_PAID);
            } else {
                keysName = Arrays.asList(MAKE_PAID, UNMAKE_PAID);
            }

            return keysName;
        }

        @Override
        public Map<String, EventHandler<ActionEvent>> putMap(ButtonPressQueue buttonPressQueue) {
            Map<String, EventHandler<ActionEvent>> actions = new HashMap<>();
            actions.put(MAKE_PAID, event -> {
                if(Objects.isNull(buttonPressQueue.selectedLesson)) return;

                buttonPressQueue.selectedLesson.makePaid();
                buttonPressQueue.lessonFacade.editLesson(buttonPressQueue.selectedLesson);
                buttonPressQueue.refresh();
            });

            actions.put(UNMAKE_PAID, event -> {
                if(Objects.isNull(buttonPressQueue.selectedLesson)) return;

                buttonPressQueue.selectedLesson.unmakePaid();
                buttonPressQueue.lessonFacade.editLesson(buttonPressQueue.selectedLesson);
                buttonPressQueue.refresh();
            });
            return actions;
        }


    },
    TAKE_PLACE {
        private boolean check(Lesson lesson) {
            return lesson.isTakePlace();
        }

        @Override
        public List<String> keyNames(Lesson lesson) {
            List<String> keysName;

            if(check(lesson)){
                keysName = Arrays.asList(UNMAKE_TAKEN_PLACE, MAKE_TAKEN_PLACE);
            } else {
                keysName = Arrays.asList(MAKE_TAKEN_PLACE, UNMAKE_TAKEN_PLACE);
            }

            return keysName;
        }

        @Override
        public Map<String, EventHandler<ActionEvent>> putMap(ButtonPressQueue buttonPressQueue) {
            Map<String, EventHandler<ActionEvent>> actions = new HashMap<>();
            actions.put(MAKE_TAKEN_PLACE, event -> {
                if(Objects.isNull(buttonPressQueue.selectedLesson)) return;

                buttonPressQueue.selectedLesson.makeTakePlace();
                buttonPressQueue.lessonFacade.editLesson(buttonPressQueue.selectedLesson);
                buttonPressQueue.refresh();
            });

            actions.put(UNMAKE_TAKEN_PLACE, event -> {
                if(Objects.isNull(buttonPressQueue.selectedLesson)) return;

                buttonPressQueue.selectedLesson.unmakeTakenPlace();
                buttonPressQueue.lessonFacade.editLesson(buttonPressQueue.selectedLesson);
                buttonPressQueue.refresh();
            });
            return actions;
        }
    };

    public abstract List<String> keyNames(Lesson lesson);
    public abstract Map<String, EventHandler<ActionEvent>> putMap(ButtonPressQueue buttonPressQueue);

}
