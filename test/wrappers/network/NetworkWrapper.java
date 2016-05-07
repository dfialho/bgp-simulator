package wrappers.network;

import network.Network;
import network.Node;
import network.exceptions.NodeNotFoundException;
import policies.Policy;

/**
 * Implements a set of static method wrappers to improve generating a network statically in a more
 * readable way.
 */
public class NetworkWrapper {

    private NetworkWrapper() {}  // can not be instantiated outside of the class

    // ----- PUBLIC INTERFACE -----------------------------------------------------------------------------------------

    /**
     * Creates a network from the link elements.
     *
     * @param policy policy of the network.
     * @param links links of the network.
     * @return network instance initialized.
     */
    public static Network network(Policy policy, LinkElement... links) {
        Network network = new Network(policy);
        try {
            for (LinkElement link : links)
                link.addTo(network);

        } catch (NodeNotFoundException e) {
            e.printStackTrace();
        }

        return network;
    }

    /**
     * Creates a node with any id. To be used in a context when the id of the node is not important.
     */
    public static Node anyNode() {
        return new Node(Integer.MAX_VALUE);
    }

}
