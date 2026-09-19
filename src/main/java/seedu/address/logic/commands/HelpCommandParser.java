package seedu.address.logic.commands;

import seedu.address.logic.parser.exceptions.ParseException;

/** Parses the optional topic of a {@link HelpCommand}. */
public class HelpCommandParser {

    /**
     * Parses help arguments. Help accepts either no argument or exactly one command keyword.
     *
     * @param args arguments following {@code help}
     * @return the parsed help command
     * @throws ParseException if the arguments are invalid
     */
    public HelpCommand parse(String args) throws ParseException {
        String normalizedArgs = args.trim().replaceAll("\\s+", " ");

        if (normalizedArgs.isEmpty()) {
            return new HelpCommand();
        }

        String[] topics = normalizedArgs.split(" ");
        if (topics[0].startsWith("-")) {
            throw new ParseException(HelpCommand.MESSAGE_INVALID_COMMAND_FORMAT);
        }

        if (topics.length > 1) {
            throw new ParseException(HelpCommand.MESSAGE_MULTIPLE_COMMANDS);
        }

        String topic = topics[0];
        if (!HelpTopics.hasTopic(topic)) {
            throw new ParseException(String.format(HelpCommand.MESSAGE_UNKNOWN_TOPIC_FORMAT, topic));
        }

        return new HelpCommand(topic);
    }
}
