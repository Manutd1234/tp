package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * An immutable optional note about a person. An empty value represents no remark.
 */
public class Remark {
    public final String value;

    /**
     * Creates a remark containing any non-null text, including an empty string.
     *
     * @param value The text of the remark.
     */
    public Remark(String value) {
        this.value = requireNonNull(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof Remark otherRemark && value.equals(otherRemark.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
