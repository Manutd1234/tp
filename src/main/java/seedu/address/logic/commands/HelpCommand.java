package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Displays command usage instructions in the result display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD + " [COMMAND]";

    public static final String SHOWING_HELP_MESSAGE = "Showing command syntax quick-reference.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT =
            "Unknown command format. " + MESSAGE_USAGE;
    public static final String MESSAGE_MULTIPLE_COMMANDS =
            "Only one command can be inspected at a time. " + MESSAGE_USAGE;
    public static final String MESSAGE_UNKNOWN_TOPIC_FORMAT =
            "No help entry found for \"%s\". Type help for a list of available commands.";

    private final String topic;

    /** Creates a command that displays the command synopsis. */
    public HelpCommand() {
        topic = null;
    }

    /** Creates a command that displays help for the specified command. */
    public HelpCommand(String topic) {
        requireNonNull(topic);
        this.topic = topic;
    }

    @Override
    public CommandResult execute(Model model) {
        if (topic == null) {
            return new CommandResult(HelpTopics.getSummary());
        }
        return new CommandResult(HelpTopics.getTopic(topic));
    }
}
