package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.person.Remark;

/**
 * Verifies remark parsing and malformed input handling through the command dispatcher.
 */
public class RemarkCommandParserTest {
    private final RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_validRemark_success() throws Exception {
        RemarkCommand expected = new RemarkCommand(Index.fromOneBased(1), new Remark("Likes swimming"));
        assertParseSuccess(parser, " 1 r/Likes swimming", expected);
        assertEquals(expected, new AddressBookParser().parseCommand("remark 1 r/Likes swimming"));
    }

    @Test
    public void parse_emptyOrOmittedRemark_removesRemark() {
        RemarkCommand expected = new RemarkCommand(Index.fromOneBased(1), new Remark(""));
        assertParseSuccess(parser, " 1 r/", expected);
        assertParseSuccess(parser, " 1", expected);
        assertParseSuccess(parser, " 1 r/   ", expected);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String error = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        for (String input : new String[]{"", " r/Note", " 0 r/Note", " -1 r/Note", " abc r/Note"}) {
            assertParseFailure(parser, input, error);
        }
    }

    @Test
    public void parse_duplicateRemark_failure() {
        assertParseFailure(parser, " 1 r/First r/Second",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_REMARK));
    }
}
