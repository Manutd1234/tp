package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void equals() {
        Remark remark = new Remark("Needs follow-up");

        assertEquals(remark, remark);
        assertEquals(remark, new Remark("Needs follow-up"));
        assertNotEquals(remark, new Remark("Different note"));
        assertNotEquals(remark, null);
        assertNotEquals(remark, "Needs follow-up");
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        assertEquals(new Remark("Needs follow-up").hashCode(), new Remark("Needs follow-up").hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals("Needs follow-up", new Remark("Needs follow-up").toString());
    }
}
