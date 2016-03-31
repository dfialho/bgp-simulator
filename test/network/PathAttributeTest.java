package network;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PathAttributeTest {

    @Test
    public void createInvalid_AlwaysReturnsTheSameInstance() throws Exception {
        PathAttribute invalid = PathAttribute.createInvalidPath();
        assert invalid == PathAttribute.createInvalidPath();
    }

    @Test
    public void createInvalid_ReturnsInvalidPathAttribute() throws Exception {
        assertThat(PathAttribute.createInvalidPath().isInvalid(), is(true));
    }

    @Test
    public void compareTo_TwoEmptyPathsCompared_Equal() throws Exception {
        PathAttribute emptyPath1 = new PathAttribute();
        PathAttribute emptyPath2 = new PathAttribute();

        assertThat(emptyPath1.compareTo(emptyPath2), equalTo(0));
    }

    @Test
    public void compareTo_PathWithOneNodeComparedEmptyPath_Greater() throws Exception {
        PathAttribute pathWithOneNode = new PathAttribute(Factory.createNode());
        PathAttribute emptyPath = new PathAttribute();

        assertThat(pathWithOneNode.compareTo(emptyPath), greaterThan(0));
    }

    @Test
    public void compareTo_EmptyPathComparedPathWithOneNode_Lesser() throws Exception {
        PathAttribute pathWithOneNode = new PathAttribute(Factory.createNode());
        PathAttribute emptyPath = new PathAttribute();

        assertThat(emptyPath.compareTo(pathWithOneNode), lessThan(0));
    }

    @Test
    public void compareTo_PathWithNode1ComparedPathWithNode1_Equal() throws Exception {
        PathAttribute path1WithNode1 = new PathAttribute(Factory.createNode(1));
        PathAttribute path2WithNode1 = new PathAttribute(Factory.createNode(1));

        assertThat(path1WithNode1.compareTo(path2WithNode1), equalTo(0));
    }

    @Test
    public void compareTo_PathWithNode1ComparedPathWithNode2_Equal() throws Exception {
        PathAttribute pathWithNode1 = new PathAttribute(Factory.createNode(1));
        PathAttribute pathWithNode2 = new PathAttribute(Factory.createNode(2));

        assertThat(pathWithNode1.compareTo(pathWithNode2), equalTo(0));
    }

    @Test
    public void compareTo_PathWithTwoNodes1And2ComparedPathWithNode1_Greater() throws Exception {
        Node[] nodes = Factory.createNodes(2);
        PathAttribute pathWithTwoNodes1And2 = new PathAttribute(nodes);
        PathAttribute pathWithNode1 = new PathAttribute(Factory.createNode(1));

        assertThat(pathWithTwoNodes1And2.compareTo(pathWithNode1), greaterThan(0));
    }

    @Test
    public void contains_InvalidPath_ReturnsFalse() throws Exception {
        PathAttribute invalidPath = PathAttribute.createInvalidPath();
        Node node = Factory.createRandomNode();

        assertThat(invalidPath.contains(node), is(false));
    }

    @Test
    public void copyConstructor_InvalidPath_ConstructsInvalidPath() throws Exception {
        PathAttribute invalidPath = PathAttribute.createInvalidPath();

        PathAttribute pathCopy = new PathAttribute(invalidPath);

        assertThat(pathCopy.isInvalid(), is(true));
    }

}