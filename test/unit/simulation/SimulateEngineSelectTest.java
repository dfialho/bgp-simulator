package simulation;

import network.Factory;
import network.Link;
import network.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import policies.DummyAttribute;
import policies.DummyAttributeFactory;
import policies.DummyLabel;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimulateEngineSelectTest {

    SimulateEngine engine;  // class under test

    Node destination = Factory.createRandomNode();
    Node learningNode = Factory.createRandomNode();
    Node exportingNode = Factory.createRandomNode();
    Link link = new Link(learningNode, exportingNode, new DummyLabel());

    @Mock
    RouteTable stubRouteTable;
    @InjectMocks
    NodeStateInfo nodeStateInfo = new NodeStateInfo(learningNode, new DummyAttributeFactory());

    @Mock
    Protocol stubProtocol;

    @Before
    public void setUp() throws Exception {
        engine = new SimulateEngine(stubProtocol, new DummyAttributeFactory(), null, null);
        when(stubProtocol.isOscillation(any(), any(), any(), any(), any())).thenReturn(false);
    }

    @Test
    public void
    select_BetweenLearnedInvalidRouteAndExclRouteInvalid_InvalidRoute() throws Exception {
        Route invalidRoute = Route.createInvalid(destination, new DummyAttributeFactory());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(invalidRoute);

        assertThat(engine.select(nodeStateInfo, link, null, invalidRoute), is(invalidRoute));
    }

    @Test
    public void
    select_BetweenLearnedInvalidRouteAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr0AndEmptyPath() throws Exception {
        Route learnedRoute = Route.createInvalid(destination, new DummyAttributeFactory());
        Route exlcRoute = new Route(destination, new DummyAttribute(0), new PathAttribute());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndEmptyPathAndExclRouteInvalid_RouteWithAttr0AndEmptyPath() throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(0), new PathAttribute());
        Route exlcRoute = Route.createInvalid(destination, new DummyAttributeFactory());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(learnedRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndEmptyPathAndExclRouteWithAttr1AndEmptyPath_RouteWithAttr1AndEmptyPath()
            throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(0), new PathAttribute());
        Route exlcRoute = new Route(destination, new DummyAttribute(1), new PathAttribute());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr1AndEmptyPathAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr1AndEmptyPath()
            throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(1), new PathAttribute());
        Route exlcRoute = new Route(destination, new DummyAttribute(0), new PathAttribute());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(learnedRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithDestinationAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr0AndEmptyPath()
            throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(0), new PathAttribute(destination));
        Route exlcRoute = new Route(destination, new DummyAttribute(0), new PathAttribute());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndEmptyPathAndExclRouteWithAttr0AndPathWithDestination_RouteWithAttr0AndEmptyPath()
            throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(0), new PathAttribute());
        Route exlcRoute = new Route(destination, new DummyAttribute(0), new PathAttribute(destination));
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(learnedRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteInvalid_InvalidRoute()
            throws Exception {
        Route invalidRoute = Route.createInvalid(destination, new DummyAttributeFactory());
        Route learnedRoute = new Route(destination, new DummyAttribute(0), new PathAttribute(learningNode));
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(invalidRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(invalidRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr0AndEmptyPath()
            throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(0), new PathAttribute(learningNode));
        Route exlcRoute = new Route(destination, new DummyAttribute(0), new PathAttribute());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteWithAttr0AndPathWith2_RouteWithAttr0AndPathWith2()
            throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(0), new PathAttribute(learningNode));
        Node[] nodes = {destination, Factory.createRandomNode()};
        Route exlcRoute = new Route(destination, new DummyAttribute(0), new PathAttribute(nodes));
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr1AndPathWithLearningNodeAndExclRouteWithAttr0AndEmptyPath_RouteWithAttr0AndEmptyPath()
            throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(1), new PathAttribute(learningNode));
        Route exlcRoute = new Route(destination, new DummyAttribute(0), new PathAttribute());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(exlcRoute));
    }

    @Test
    public void
    select_BetweenLearnedWithAttr0AndPathWithLearningNodeAndExclRouteWithAttr1AndEmptyPath_RouteWithAttr1AndEmptyPath()
            throws Exception {
        Route learnedRoute = new Route(destination, new DummyAttribute(0), new PathAttribute(learningNode));
        Route exlcRoute = new Route(destination, new DummyAttribute(1), new PathAttribute());
        when(stubRouteTable.getSelectedRoute(any(), any())).thenReturn(exlcRoute);

        assertThat(engine.select(nodeStateInfo, link, null, learnedRoute), is(exlcRoute));
    }
}