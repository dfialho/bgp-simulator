package simulation;

import network.Link;

public interface Protocol {

    /**
     * Extends the attribute using the given link, while applying some modifications to the import properties
     * according to the protocol implemented.
     * @param link link to extend the attribute.
     * @param attribute attribute to be extended.
     * @return extended attribute (new instance).
     */
    Attribute extend(Link link, Attribute attribute);

    /**
     * Checks if the condition to detect an oscillation is verified.
     * @param link link from which the route was learned. Some of the following parameters might not be used.
     * @param learnedRoute learned route.
     * @param attribute learned route attribute extended.
     * @param path learned route path including the node that exported the route.
     * @param exclRoute route preferred excluding the node from which the route was learned.
     * @return true if an oscillation is detected and false otherwise.
     */
    boolean isOscillation(Link link, Route learnedRoute, Attribute attribute, PathAttribute path, Route exclRoute);

    /**
     * Sets the parameters used by the extend operation. This adds more flexibility to configure the extend operation.
     * Some of the following parameters might not be used.
     * @param link link from which the route was learned.
     * @param learnedRoute learned route.
     * @param attribute learned route attribute extended.
     * @param path learned route path including the node that exported the route.
     * @param exclRoute route preferred excluding the node from which the route was learned.
     */
    void setParameters(Link link, Route learnedRoute, Attribute attribute, PathAttribute path, Route exclRoute);

}