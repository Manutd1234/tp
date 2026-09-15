package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

/**
 * Verifies remark updates, displayed indices, and preservation during ordinary edits.
 */
public class RemarkCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addReplaceAndRemoveRemark_success() throws Exception {
        Person original = model.getFilteredPersonList().getFirst();
        Index index = Index.fromOneBased(1);
        for (String text : new String[]{"Likes swimming", "Prefers email", ""}) {
            CommandResult result = new RemarkCommand(index, new Remark(text)).execute(model);
            Person expected = new PersonBuilder(original).withRemark(text).build();
            assertEquals(expected, model.getFilteredPersonList().getFirst());
            String message = text.isEmpty() ? RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS
                    : RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS;
            assertEquals(String.format(message, Messages.format(expected)), result.getFeedbackToUser());
        }
    }

    @Test
    public void execute_filteredList_updatesDisplayedPerson() throws Exception {
        Person secondPerson = model.getFilteredPersonList().get(1);
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(List.of("Benson")));
        new RemarkCommand(Index.fromOneBased(1), new Remark("Follow up")).execute(model);
        assertTrue(model.getAddressBook().getPersonList().contains(
                new PersonBuilder(secondPerson).withRemark("Follow up").build()));
        assertEquals(7, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_invalidIndex_modelUnchanged() {
        assertCommandFailure(new RemarkCommand(Index.fromOneBased(100), new Remark("Note")), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_editOtherField_preservesRemark() throws Exception {
        new RemarkCommand(Index.fromOneBased(1), new Remark("Keep this note")).execute(model);
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setName(new Name("Alice Renamed"));
        new EditCommand(Index.fromOneBased(1), descriptor).execute(model);
        assertEquals(new Remark("Keep this note"), model.getFilteredPersonList().getFirst().getRemark());
    }

    @Test
    public void equals_differentIndexOrRemark_notEqual() {
        RemarkCommand command = new RemarkCommand(Index.fromOneBased(1), new Remark("Note"));
        assertEquals(command, new RemarkCommand(Index.fromOneBased(1), new Remark("Note")));
        assertNotEquals(command, new RemarkCommand(Index.fromOneBased(2), new Remark("Note")));
        assertNotEquals(command, new RemarkCommand(Index.fromOneBased(1), new Remark("Changed")));
        assertNotEquals(command, null);
    }
}
