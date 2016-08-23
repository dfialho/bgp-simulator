package wrappers.topology;

import core.topology.Node;

/**
 * Represents a source node of a link.
 */
public class ToNodeElement extends NodeElement {

    private ToNodeElement(Node node) {
        super(node);
    }

    public static ToNodeElement to(int id) {
        return new ToNodeElement(new Node(id));
    }
}