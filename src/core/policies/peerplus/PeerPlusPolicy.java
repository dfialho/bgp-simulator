package core.policies.peerplus;

import core.Attribute;
import core.Label;
import core.Policy;
import io.topologyreaders.exceptions.InvalidPolicyTagException;

public class PeerPlusPolicy implements Policy {

    @Override
    public Attribute createSelf() {
        return SelfAttribute.self();
    }

    @Override
    public Label createLabel(String tag) throws InvalidPolicyTagException {
        switch (tag) {
            case "R+":
                return new PeerPlusLabel();
            case "C":
                return new CustomerLabel();
            case "R":
                return new PeerLabel();
            case "P":
                return new ProviderLabel();
            default:
                throw new InvalidPolicyTagException(tag, "not a valid tag for a Gao Rexford label");
        }
    }

    /**
     * Returns a string with the name of the policy.
     *
     * @return string "RoC"
     */
    @Override
    public String toString() {
        return "RoC";
    }

}