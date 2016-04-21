package simulation;

import network.Node;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static simulation.Route.invalidRoute;
import static wrappers.DummyWrapper.dummyAttr;
import static wrappers.PathWrapper.path;
import static wrappers.RouteWrapper.route;
import static wrappers.network.NetworkWrapper.anyNode;

public class RouteTest {
    
    private Node destination = anyNode();

    @Test
    public void compareTo_InvalidRouteToInvalidRoute_Equal() throws Exception {
        assertThat(invalidRoute(destination).compareTo(invalidRoute(destination)), equalTo(0));
    }

    @Test
    public void compareTo_InvalidRouteToRoute0AndEmptyPath_Greater() throws Exception {
        assertThat(invalidRoute(destination).compareTo(route(destination, dummyAttr(0), path())), greaterThan(0));
    }

    @Test
    public void compareTo_Route0AndEmptyPathToInvalidRoute_Lesser() throws Exception {
        assertThat(route(destination, dummyAttr(0), path()).compareTo(invalidRoute(destination)), lessThan(0));
    }

    @Test
    public void compareTo_Route0AndEmptyPathAndRoute0AndEmptyPath_Equal() throws Exception {
        Route route1 = route(destination, dummyAttr(0), path());
        Route route2 = route(destination, dummyAttr(0), path());

        assertThat(route1.compareTo(route2), equalTo(0));
    }

    @Test
    public void compareTo_Route1AndEmptyPathToRoute2AndEmptyPath_Greater() throws Exception {
        Route route1 = route(destination, dummyAttr(1), path());
        Route route2 = route(destination, dummyAttr(2), path());

        assertThat(route1.compareTo(route2), greaterThan(0));
    }

    @Test
    public void compareTo_Route2AndEmptyPathToRoute1AndEmptyPath_Lesser() throws Exception {
        Route route1 = route(destination, dummyAttr(1), path());
        Route route2 = route(destination, dummyAttr(2), path());

        assertThat(route2.compareTo(route1), lessThan(0));
    }

    @Test
    public void compareTo_Route0AndPathWithNode0ToRoute0AndEmptyPath_Greater() throws Exception {
        Route route1 = route(destination, dummyAttr(0), path(0));
        Route route2 = route(destination, dummyAttr(0), path());

        assertThat(route1.compareTo(route2), greaterThan(0));
    }

    @Test
    public void compareTo_Route0AndEmptyPathToRoute0AndPathWithNode0_Lesser() throws Exception {
        Route route1 = route(destination, dummyAttr(0), path());
        Route route2 = route(destination, dummyAttr(0), path(0));

        assertThat(route1.compareTo(route2), lessThan(0));
    }

    @Test
    public void compareTo_Route1AndPathWithNode0ToRoute2AndEmptyPath_Greater() throws Exception {
        Route route1 = route(destination, dummyAttr(1), path(0));
        Route route2 = route(destination, dummyAttr(2), path());

        assertThat(route1.compareTo(route2), greaterThan(0));
    }

    @Test
    public void compareTo_Route2AndEmptyPathToRoute1AndPathWithNode0_Lesser() throws Exception {
        Route route1 = route(destination, dummyAttr(2), path());
        Route route2 = route(destination, dummyAttr(1), path(0));

        assertThat(route1.compareTo(route2), lessThan(0));
    }

}