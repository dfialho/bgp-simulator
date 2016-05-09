package simulation.implementations.handlers;

import network.Network;
import org.junit.Before;
import org.junit.Test;
import policies.implementations.shortestpath.ShortestPathPolicy;
import protocols.implementations.BGPProtocol;
import protocols.implementations.D1R1Protocol;
import simulation.Engine;
import simulation.State;
import addons.eventhandlers.MessageAndDetectionCountHandler;
import simulation.implementations.schedulers.FIFOScheduler;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static wrappers.ShortestPathWrapper.label;
import static wrappers.network.FromNodeElement.from;
import static wrappers.network.LinkElement.link;
import static wrappers.network.ToNodeElement.to;
import static wrappers.network.NetworkWrapper.network;

public class MessageAndDetectionCountHandlerTest {

    private MessageAndDetectionCountHandler eventHandler;
    private Engine engine;

    @Before
    public void setUp() throws Exception {
        eventHandler = new MessageAndDetectionCountHandler();
        engine = new Engine(new FIFOScheduler());
        eventHandler.register(engine.getEventGenerator());
    }

    @Test
    public void messageCount_ForTopology0WithBGP_Is1() throws Exception {
        State state = State.create(
                network(new ShortestPathPolicy(),
                        link(from(0), to(1), label(1))),
                new BGPProtocol());

        engine.simulate(state, 1);

        assertThat(eventHandler.getMessageCount(), is(1));
    }

    @Test
    public void messageCount_ForTopology1_Is4() throws Exception {
        State state = State.create(
                network(new ShortestPathPolicy(),
                        link(from(0), to(1), label(1)),
                        link(from(1), to(2), label(1)),
                        link(from(0), to(2), label(0))),
                new BGPProtocol());

        engine.simulate(state);

        assertThat(eventHandler.getMessageCount(), is(4));
    }

    private static Network network3 = network(new ShortestPathPolicy(),
            link(from(1), to(0), label(0)),
            link(from(2), to(0), label(0)),
            link(from(3), to(0), label(0)),
            link(from(1), to(2), label(-1)),
            link(from(2), to(3), label(1)),
            link(from(3), to(1), label(-2)));

    @Test
    public void messageCount_ForTopology3WithD1R1_Is13() throws Exception {
        State state = State.create(network3, new D1R1Protocol());

        engine.simulate(state, 0);

        assertThat(eventHandler.getMessageCount(), is(13));
    }

    @Test
    public void detectionCount_ForTopology3WithD1R1_Is2() throws Exception {
        State state = State.create(network3, new D1R1Protocol());

        engine.simulate(state, 0);

        assertThat(eventHandler.getDetectionCount(), is(2));
    }
}