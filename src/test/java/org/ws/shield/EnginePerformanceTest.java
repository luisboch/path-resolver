/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield;

import org.ws.shield.engine.PathSearchResult;
import org.ws.shield.engine.TreeManager;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author luis
 */
public class EnginePerformanceTest {

    private static Logger log = Logger.getLogger(EnginePerformanceTest.class.getSimpleName());
    private static final TreeManager<Long> manager = TreeManager.getManager(Long.class);
    private static int size = 1000;

    public EnginePerformanceTest() {

    }

    @BeforeClass
    public static void populate() throws Exception {
        log.info("Starting...");
        long ms = System.currentTimeMillis();
        long id = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                for (int k = 0; k < size; k++) {
                    manager.addUrl("POST/" + i + "/" + j + "/" + k + "/", ++id);
                    manager.addUrl("POST/" + i + "/" + j + "/{cod:number}/" + k + "/", ++id);
                }
            }
        }

        log.log(Level.INFO, "Took: {0}", System.currentTimeMillis() - ms + "ms to add " + id);
    }

    @Test
    public void searchTest() throws Exception {
        log.info("Starting...");
        long ms = System.currentTimeMillis();

        // Search 1
        final String search1 = "POST/1/1/" + (size - 10);
        Collection<PathSearchResult<Long>> search = manager.search(search1);
        PathSearchResult rs = match(search, 0, null);
        log.log(Level.INFO, "Search 1 took: {0}", System.currentTimeMillis() - ms + "ms (id" + rs.getNode().getId() + ")");
//        assertEquals(search1 + "/", rs.getNode().getFullPath());

        // Search 2
        final String search2 = "POST/1/1/98484561/" + (size - 10);
        ms = System.currentTimeMillis();
        search = manager.search(search2);
        rs = match(search, 1, null);
        log.log(Level.INFO, "Search 2 took: {0}", System.currentTimeMillis() - ms + "ms (id" + rs.getNode().getId() + ")");

//        assertEquals("POST/2/3/{cod:number}/" + (size - 10) + "/", rs.getNode().getFullPath());
        log.info("Finished");

//        manager.printInfo();
    }

    private static PathSearchResult<Long> match(Collection<PathSearchResult<Long>> search, Integer paramSize, Long id) {
        assertTrue(search.size() == 1);
        PathSearchResult<Long> found = search.iterator().next();
        assertTrue(found.getParams().size() == paramSize);

        if (id != null) {
            assertEquals(found.getNode().getId(), id);
        }

        return found;
    }
}
