package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.ReadOnlyAddressBook;

/**
 * Verifies that removing postal addresses preserves the remaining data in legacy files.
 */
public class RemovedAddressStorageTest {
    @TempDir
    public Path temporaryDirectory;

    @Test
    public void readAndSave_legacyAddress_preservesContactWithoutAddress() throws Exception {
        Path file = temporaryDirectory.resolve("addressbook.json");
        Files.writeString(file, """
                {"persons":[{"name":"Alice Yeoh","phone":"87438807",
                "email":"alice@example.com","address":"Old postal address",
                "remark":"Call tomorrow","tags":["friends"]}]}
                """);
        JsonAddressBookStorage storage = new JsonAddressBookStorage(file);
        ReadOnlyAddressBook loaded = storage.readAddressBook().orElseThrow();
        assertEquals("Alice Yeoh", loaded.getPersonList().get(0).getName().fullName);
        assertEquals("Call tomorrow", loaded.getPersonList().get(0).getRemark().value);
        storage.saveAddressBook(loaded);
        assertFalse(Files.readString(file).contains("\"address\""));
        assertEquals(loaded, storage.readAddressBook().orElseThrow());
    }
}
