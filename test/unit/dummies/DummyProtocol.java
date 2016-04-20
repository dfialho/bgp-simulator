package dummies;

import network.*;
import policies.Attribute;
import protocols.Protocol;
import simulation.PathAttribute;
import simulation.Route;

public class DummyProtocol implements Protocol {
    @Override
    public Attribute extend(Node destination, Link link, Attribute attribute) {
        return new DummyAttribute();
    }

    @Override
    public boolean isOscillation(Link link, Route learnedRoute, Attribute attribute, PathAttribute path, Route exclRoute) {
        return false;
    }

    @Override
    public void setParameters(Link link, Route learnedRoute, Attribute attribute, PathAttribute path, Route exclRoute) {

    }
}