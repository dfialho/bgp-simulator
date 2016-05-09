package simulation;

import network.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import protocols.Protocol;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static policies.InvalidAttribute.invalid;
import static simulation.PathAttribute.invalidPath;
import static wrappers.StubWrapper.stubAttr;
import static wrappers.StubWrapper.stubLink;
import static wrappers.PathWrapper.path;
import static wrappers.RouteWrapper.route;

@RunWith(MockitoJUnitRunner.class)
public class EngineSelectTest {

    private Engine engine;  // class under test

    @Mock
    private NodeState stubNodeState;

    @Mock
    Protocol stubProtocol;

    @Before
    public void setUp() throws Exception {
        engine = new Engine(null);
        when(stubProtocol.isOscillation(any(), any(), any(), any(), any())).thenReturn(false);
        when(stubNodeState.getProtocol()).thenReturn(stubProtocol);
    }

    @Test
    public void
    select_BetweenLearnedInvalidRouteAndExclRouteInvalid_InvalidRoute() throws Exception {
        Route invalidRoute = Route.invalidRoute(new Node(0));
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(invalidRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, invalidRoute), is(invalidRoute));
    }

    @Test
    public void
    select_BetweenLearnedInvalidRouteAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr0AndEmptyPath() throws Exception {
        Route learnedRoute = Route.invalidRoute(new Node(0));
        Route exlcRoute = route(0, stubAttr(0), path());
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndEmptyPathAndExclRouteInvalid_RouteWithAttr0AndEmptyPath() throws Exception {
        Route learnedRoute = route(0, stubAttr(0), path());
        Route exlcRoute = Route.invalidRoute(new Node(0));
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(learnedRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndEmptyPathAndExclRouteWithAttr1AndEmptyPath_RouteWithAttr1AndEmptyPath()
            throws Exception {
        Route learnedRoute = route(0, stubAttr(0), path());
        Route exlcRoute = route(0, stubAttr(1), path());
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr1AndEmptyPathAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr1AndEmptyPath()
            throws Exception {
        Route learnedRoute = route(0, stubAttr(1), path());
        Route exlcRoute = route(0, stubAttr(0), path());
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(learnedRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithDestinationAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr0AndEmptyPath()
            throws Exception {
        Route learnedRoute = route(0, stubAttr(0), path(0));
        Route exlcRoute = route(0, stubAttr(0), path());
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndEmptyPathAndExclRouteWithAttr0AndPathWithDestination_RouteWithAttr0AndEmptyPath()
            throws Exception {
        Route learnedRoute = route(0, stubAttr(0), path());
        Route exlcRoute = route(0, stubAttr(0), path(0));
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(learnedRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteInvalid_InvalidRoute()
            throws Exception {
        Route invalidRoute = Route.invalidRoute(new Node(0));
        Route learnedRoute = route(0, stubAttr(0), path(1));
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(invalidRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(invalidRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr0AndEmptyPath()
            throws Exception {
        Route learnedRoute = route(0, stubAttr(0), path(1));
        Route exlcRoute = route(0, stubAttr(0), path());
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteWithAttr0AndPathWith2_RouteWithAttr0AndPathWith2()
            throws Exception {
        Route learnedRoute = route(0, stubAttr(0), path(1));
        Route exlcRoute = route(0, stubAttr(0), path(0, 3));
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr1AndPathWithLearningNodeAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr0AndEmptyPath()
            throws Exception {
        Route learnedRoute = route(0, stubAttr(1), path(1));
        Route exlcRoute = route(0, stubAttr(0), path());
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteWithAttr1AndEmptyPath_RouteWithAttr1AndEmptyPath()
            throws Exception {
        Route learnedRoute = route(0, stubAttr(0), path(1));
        Route exlcRoute = route(0, stubAttr(1), path());
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(stubNodeState, stubLink(1, 2), null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteIsValid_TableUpdatedWithInvalidRoute()
            throws Exception {
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(route(0, stubAttr(1), path()));

        engine.select(stubNodeState, stubLink(1, 2), null, route(0, stubAttr(0), path(1)));

        verify(stubNodeState, times(1)).updateRoute(new Node(0), stubLink(1, 2), invalid(), invalidPath());
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndExclRouteWithAttr1BothWithEmptyPaths_TableUpdatedWithRouteWithAttr0AndEmptyPath()
            throws Exception {
        when(stubNodeState.getSelectedRoute(any(), any())).thenReturn(route(0, stubAttr(1), path()));

        engine.select(stubNodeState, stubLink(1, 2), null, route(0, stubAttr(0), path()));

        verify(stubNodeState, times(1)).updateRoute(new Node(0), stubLink(1, 2), stubAttr(0), path());
    }
}