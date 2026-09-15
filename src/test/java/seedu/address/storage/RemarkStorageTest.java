package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Verifies that remarks survive JSON storage and legacy records can still be read.
 */
public class RemarkStorageTest {
    @TempDir
    public Path temporaryDirectory;

    @Test
    public void saveAndRead_remarkWithUnicodeAndQuotes_preserved() throws Exception {
        Person person = new PersonBuilder().withRemark("Prefers \"email\" — 你好").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);
        JsonAddressBookStorage storage = new JsonAddressBookStorage(temporaryDirectory.resolve("addressbook.json"));
        storage.saveAddressBook(addressBook);
        assertEquals(addressBook, new AddressBook(storage.readAddressBook().orElseThrow()));
    }

    @Test
    public void toModelType_legacyRecordWithoutRemark_defaultsToEmpty() throws Exception {
        Person original = new PersonBuilder().build();
        JsonAdaptedPerson legacy = new JsonAdaptedPerson(original.getName().fullName, original.getPhone().value,
                original.getEmail().value, original.getAddress().value, null, null);
        assertEquals(original, legacy.toModelType());
    }
}
