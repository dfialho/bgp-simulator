package policies.roc;

import network.Link;
import policies.Attribute;
import policies.Label;

import java.util.EnumMap;
import java.util.Map;

import static policies.InvalidAttribute.invalid;
import static policies.roc.PeerPlusAttribute.peerplus;

/**
 * Implements the peer+ label.
 */
public class PeerPlusLabel implements Label {

    /**
     * Table gives the result of extending each type of attribute.
     */
    private static final Map<RoCAttribute.Type, Attribute> extendTable =
            new EnumMap<>(RoCAttribute.Type.class);

    static {
        extendTable.put(RoCAttribute.Type.SELF, peerplus());
        extendTable.put(RoCAttribute.Type.PEER_PLUS, peerplus());
        extendTable.put(RoCAttribute.Type.CUSTOMER, peerplus());
        extendTable.put(RoCAttribute.Type.PEER, invalid());
        extendTable.put(RoCAttribute.Type.PROVIDER, invalid());
    }

    @Override
    public Attribute extend(Link link, Attribute attribute) {
        if (attribute.isInvalid()) return invalid();

        RoCAttribute rocAttribute = (RoCAttribute) attribute;
        return extendTable.get(rocAttribute.getType());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PeerPlusLabel;
    }

    @Override
    public int hashCode() {
        return 34;  // must be different from all RoC labels
    }

    @Override
    public String toString() {
        return "R+";
    }
}