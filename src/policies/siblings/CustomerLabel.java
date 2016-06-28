package policies.siblings;

import network.Link;
import policies.Attribute;
import policies.Label;

import java.util.EnumMap;
import java.util.Map;

import static policies.InvalidAttribute.invalid;
import static policies.siblings.CustomerAttribute.customer;

/**
 * Implements the customer label.
 */
public class CustomerLabel implements Label {

    /**
     * Table gives the result of extending each type of attribute.
     */
    private static final Map<SiblingAttribute.Type, Attribute> extendTable =
            new EnumMap<>(SiblingAttribute.Type.class);

    static {
        extendTable.put(SiblingAttribute.Type.SELF, customer(0));
        extendTable.put(SiblingAttribute.Type.CUSTOMER, customer(0));
        extendTable.put(SiblingAttribute.Type.PEER, invalid());
        extendTable.put(SiblingAttribute.Type.PROVIDER, invalid());
    }

    @Override
    public Attribute extend(Link link, Attribute attribute) {
        if (attribute.isInvalid()) return invalid();

        SiblingAttribute siblingAttribute = (SiblingAttribute) attribute;
        return extendTable.get(siblingAttribute.getType());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CustomerLabel;
    }

    @Override
    public int hashCode() {
        return 31;  // must be different from all Sibling labels
    }

    @Override
    public String toString() {
        return "C";
    }
}
