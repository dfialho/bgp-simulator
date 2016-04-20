package simulation;

import network.Node;
import org.junit.Test;

import static network.Factory.createNodes;
import static network.Factory.createRandomNode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static simulation.PathAttribute.invalidPath;
import static wrappers.PathWrapper.path;

public class PathAttributeTest {

    @Test
    public void createInvalid_AlwaysReturnsTheSameInstance() throws Exception {
        assert invalidPath() == invalidPath();  // assert the reference is the same
    }

    @Test
    public void createInvalid_ReturnsInvalidPathAttribute() throws Exception {
        assertThat(invalidPath().isInvalid(), is(true));
    }

    @Test
    public void compareTo_TwoEmptyPathsCompared_Equal() throws Exception {
        PathAttribute emptyPath1 = new PathAttribute();
        PathAttribute emptyPath2 = new PathAttribute();

        assertThat(emptyPath1.compareTo(emptyPath2), equalTo(0));
    }

    @Test
    public void compareTo_PathWithOneNodeComparedEmptyPath_Greater() throws Exception {
        PathAttribute pathWithOneNode = new PathAttribute(createRandomNode());
        PathAttribute emptyPath = new PathAttribute();

        assertThat(pathWithOneNode.compareTo(emptyPath), greaterThan(0));
    }

    @Test
    public void compareTo_EmptyPathComparedPathWithOneNode_Lesser() throws Exception {
        PathAttribute pathWithOneNode = new PathAttribute(createRandomNode());
        PathAttribute emptyPath = new PathAttribute();

        assertThat(emptyPath.compareTo(pathWithOneNode), lessThan(0));
    }

    @Test
    public void compareTo_PathWithNode1ComparedPathWithNode1_Equal() throws Exception {
        PathAttribute path1WithNode1 = new PathAttribute(new Node(1));
        PathAttribute path2WithNode1 = new PathAttribute(new Node(1));

        assertThat(path1WithNode1.compareTo(path2WithNode1), equalTo(0));
    }

    @Test
    public void compareTo_PathWithNode1ComparedPathWithNode2_Equal() throws Exception {
        PathAttribute pathWithNode1 = new PathAttribute(new Node(1));
        PathAttribute pathWithNode2 = new PathAttribute(new Node(2));

        assertThat(pathWithNode1.compareTo(pathWithNode2), equalTo(0));
    }

    @Test
    public void compareTo_PathWithTwoNodes1And2ComparedPathWithNode1_Greater() throws Exception {
        Node[] nodes = createNodes(2);
        PathAttribute pathWithTwoNodes1And2 = new PathAttribute(nodes);
        PathAttribute pathWithNode1 = new PathAttribute(new Node(1));

        assertThat(pathWithTwoNodes1And2.compareTo(pathWithNode1), greaterThan(0));
    }

    @Test
    public void contains_InvalidPath_ReturnsFalse() throws Exception {
        assertThat(invalidPath().contains(createRandomNode()), is(false));
    }

    @Test
    public void copyConstructor_InvalidPath_ConstructsInvalidPath() throws Exception {
        PathAttribute pathCopy = new PathAttribute(invalidPath());

        assertThat(pathCopy.isInvalid(), is(true));
    }

    @Test
    public void getPathAfter_InvalidPath_InvalidPath() throws Exception {
        assertThat(invalidPath().getPathAfter(createRandomNode()), is(invalidPath()));
    }

    @Test
    public void getPathAfter_EmptyPath_EmptyPath() throws Exception {
        assertThat(path().getPathAfter(createRandomNode()), is(path()));
    }

    @Test
    public void getPathAfter_Node1ForPathWithNode1And0_PathWithNode0() throws Exception {
        assertThat(path(1, 0).getPathAfter(new Node(1)), is(path(0)));
    }

    @Test
    public void getPathAfter_Node1ForPathWithNode1_EmptyPath() throws Exception {
        assertThat(path(1).getPathAfter(new Node(1)), is(path()));
    }

    @Test
    public void getPathAfter_Node0ForPathWithNode1_EmptyPath() throws Exception {
        assertThat(path(1).getPathAfter(new Node(0)), is(path()));
    }

    @Test
    public void getPathAfter_Node1ForPathWithNode2And1And4And0_PathWithNode4And0() throws Exception {
        assertThat(path(2, 1, 4, 0).getPathAfter(new Node(1)), is(path(4, 0)));
    }

}