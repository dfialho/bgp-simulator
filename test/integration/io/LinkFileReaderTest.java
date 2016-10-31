package io;

import core.UnlabelledLink;
import org.junit.Test;

import java.io.File;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LinkFileReaderTest {

    private static File file(String filename) {
        return new File("test/integration/io/link_file_reader", filename);
    }

    private static UnlabelledLink link(int srcId, int destId) {
        return new UnlabelledLink(srcId, destId);
    }

    @Test
    public void readAllLinks_CleanLinkFile_ReadAllLinksInTheFile() throws Exception {

        UnlabelledLink[] expectedLinks = {
                link(1, 2), link(1, 3), link(2, 3), link(10, 22)
        };

        try (LinkFileReader reader = new LinkFileReader(file("clean_link_file.txt"))) {
            assertThat(reader.readAllLinks(), is(asList(expectedLinks)));
        }
    }

}