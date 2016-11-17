package v2.core;


/**
 * Represents an invalid attribute. This attribute can be used with any policy implementation and must always be the
 * least preferable attribute. It is implemented as a singleton meaning it has only one instance every time.
 */
public class InvalidAttribute implements Attribute {

    // It exists only one unique Invalid Attribute instance that can be accessed through the
    // static method invalidAttr().
    private static final InvalidAttribute invalid = new InvalidAttribute();

    private InvalidAttribute() {
    } // not be instantiated directly

    /**
     * Returns always the same instance of an invalid attribute. Its the only way to get an invalid attribute
     * instance.
     *
     * @return invalid attribute instance.
     */
    public static InvalidAttribute invalidAttr() {
        return invalid;
    }

    /**
     * The invalid attribute is equal to other invalid attributes and greater than any other attribute.
     *
     * @param attribute attribute to be compared.
     * @return 0 if attribute is invalid or greater than 0 if is not an invalid attribute.
     */
    @Override
    public int compareTo(Attribute attribute) {
        return attribute == invalidAttr() ? 0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof InvalidAttribute;
    }

    // HASHCODE - Since there is only one object the default hashCode() implementation is sufficient

    @Override
    public String toString() {
        return "•";
    }
}
