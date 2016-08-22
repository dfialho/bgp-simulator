package io.networkreaders;

import core.Policy;
import core.topology.Network;

/**
 * A topology associates a topology with a routing policy. It is read only.
 */
public final class Topology {

    private final Network network;
    private final Policy policy;

    /**
     * Constructs a new topology associating the two given topology and policy.
     *
     * @param network topology topology
     * @param policy  topology routing policy
     */
    public Topology(Network network, Policy policy) {
        this.network = network;
        this.policy = policy;
    }

    /**
     * Returns the topology's topology.
     *
     * @return the topology's topology
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Returns the topology's routing policy.
     *
     * @return the topology's routing policy
     */
    public Policy getPolicy() {
        return policy;
    }
}
