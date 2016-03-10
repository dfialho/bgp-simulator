package network;

import java.util.LinkedHashSet;

public class PathAttribute implements Attribute {

    private static PathAttribute invalid() {
        PathAttribute path = new PathAttribute();
        path.path = null;
        return path;
    }

    private static final PathAttribute INVALID = invalid();

    private LinkedHashSet<Node> path = new LinkedHashSet<>();   // must be a LinkedHashSet in order to preserve insertion order

    // TODO replace the default constructor with the static method createInvalid()
    // TODO make the default constructor private

    /**
     * Constructs an empty path.
     */
    public PathAttribute() {
    }

    public PathAttribute(Node node) {
        path.add(node);
    }

    /**
     * Returns an invalid path instance.
     * @return invalid path instance.
     */
    public static PathAttribute createInvalid() {
        return INVALID;
    }

    /**
     * Adds a new node to the path. If the node already exists in the path then it will no be added.
     * @param node node to be added to the path.
     */
    public void add(Node node) {
        path.add(node);
    }

    public boolean contains(Node node) {
        // TODO - implement PathAttribute.contains
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInvalid() {
        return path == null;
    }

    @Override
    public int compareTo(Attribute attribute) {
        // TODO - implement PathAttribute.compareTo
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PathAttribute that = (PathAttribute) o;

        return path != null ? path.equals(that.path) : that.path == null;

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
