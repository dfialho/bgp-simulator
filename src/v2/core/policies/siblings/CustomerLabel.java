package v2.core.policies.siblings;


import v2.core.Attribute;
import v2.core.Label;
import v2.core.Link;

import java.util.EnumMap;
import java.util.Map;

import static v2.core.InvalidAttribute.invalidAttr;
import static v2.core.policies.siblings.CustomerAttribute.customer;
import static v2.core.policies.siblings.SiblingAttribute.Type.*;

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
        extendTable.put(SELF, customer(0));
        extendTable.put(CUSTOMER, customer(0));
        extendTable.put(PEER, invalidAttr());
        extendTable.put(PROVIDER, invalidAttr());
    }

    @Override
    public Attribute extend(Link link, Attribute attribute) {
        if (attribute == invalidAttr()) return invalidAttr();

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
