package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import javafx.application.Platform;
import javafx.scene.control.Label;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

@EnabledOnOs(OS.LINUX)
public class PersonCardTest {

    @BeforeAll
    public static void startJavaFxPlatform() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException e) {
            // The platform was started by an earlier JavaFX test.
        }
    }

    @Test
    public void constructor_personWithRemark_displaysRemark() throws Exception {
        Person person = new PersonBuilder().withRemark("Needs follow-up").build();
        FutureTask<PersonCard> createCard = new FutureTask<>(() -> new PersonCard(person, 1));
        Platform.runLater(createCard);

        PersonCard card = createCard.get(5, TimeUnit.SECONDS);
        Label remark = (Label) card.getRoot().lookup("#remark");
        assertNotNull(remark);
        assertEquals("Needs follow-up", remark.getText());
    }
}
