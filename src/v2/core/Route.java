package v2.core;

import static v2.core.InvalidRoute.invalidRoute;

/**
 * Abstraction of a route for a certain destination router. Commonly a route associates a destination 
 * router with an attribute and a path. Since the simulator only supports one destination router this 
 * means that all routes are for one destination and, therefore, the route does not need to include the 
 * destination router.
 */
public class Route implements Comparable<Route> {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private Fields
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    
    private Attribute attribute;
    private Path path;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Constructors
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Constructs a new route given the attribute and the path.
     *
     * @param attribute policy attribute of the route.
     * @param path path to reach the destination.
     */
    public Route(Attribute attribute, Path path) {
        this.attribute = attribute;
        this.path = path;
    }

    /**
     * Creates a new self-route for the given policy. A self-route used the self-attribute generated by the
     * given policy and has an empty path.
     *
     * @param policy    policy used to create self-attribute of the route.
     */
    public static Route newSelfRoute(Policy policy) {
        return new Route(policy.createSelf(), new Path());
    }
    
    /**
     * Copy constructor. Only the path attribute is hard copied, the attribute is copied but reference 
     * since it is immutable.
     *
     * @param route route to be copied.
     */
    public Route(Route route) {
        this.attribute = route.attribute;
        this.path = Path.copy(route.path);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Public Interface
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Returns the route's attribute.
     *
     * @return attribute of the route.
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Assigns the given attribute to the route.
     *
     * @param attribute attribute to be assigned.
     */
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Returns the route's path.
     *
     * @return path of the route.
     */
    public Path getPath() {
        return path;
    }

    /**
     * Assigns the given path to the route.
     *
     * @param path path to be assigned.
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * Compares this route with another route. The comparison considers the attribute order first and second the path
     * order.
     *
     * @param other other route to compare to.
     * @return 0 if they are equal, -1 if this route is preferable and 1 if the other route is preferable.
     */
    @Override
    public int compareTo(Route other) {
        if (other == invalidRoute()) return -1;

        int attrComparison = attribute.compareTo(other.attribute);
        if (attrComparison == 0) return path.compareTo(other.path); else return attrComparison;
    }

    /**
     * Two routes are equal if they have exactly the same attributes and paths.
     *
     * @param other route to check for equality.
     * @return true if they have the same attributes and paths or false if otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Route)) return false;

        Route route = (Route) other;

        if (attribute != null ? !attribute.equals(route.attribute) : route.attribute != null) return false;
        return path != null ? path.equals(route.path) : route.path == null;

    }

    @Override
    public int hashCode() {
        int result = attribute != null ? attribute.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Route(" + attribute + ", " + path + ')';
    }

}
