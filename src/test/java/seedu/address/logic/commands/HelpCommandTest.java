package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(
                HelpTopics.getSummary(), false, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_topic_success() {
        CommandResult expectedCommandResult = new CommandResult(
                HelpTopics.getTopic("add"));
        assertCommandSuccess(new HelpCommand("add"), model, expectedCommandResult, expectedModel);
    }
}
