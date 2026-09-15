package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

/**
 * Parses a displayed person index and an optional remark into a remark command.
 * Adapted from the SE-EDU AB3 Adding a Command tutorial.
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {

    /**
     * Parses {@code args}; an empty or omitted remark clears the person's note.
     *
     * @param args The text following the command word.
     * @return The command ready for execution.
     * @throws ParseException If the index is invalid or the remark prefix is repeated.
     */
    @Override
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);
        Index index;
        try {
            index = ParserUtil.parseIndex(arguments.getPreamble());
        } catch (ParseException exception) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE),
                    exception);
        }
        arguments.verifyNoDuplicatePrefixesFor(PREFIX_REMARK);
        Remark remark = new Remark(arguments.getValue(PREFIX_REMARK).orElse(""));
        return new RemarkCommand(index, remark);
    }
}
