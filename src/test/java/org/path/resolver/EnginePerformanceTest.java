/*
 * The MIT License
 *
 * Copyright 2018 luisboch.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.path.resolver;

import java.util.ArrayList;
import org.path.resolver.engine.PathSearchResult;
import org.path.resolver.engine.TreeManager;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public static boolean THREAD_ACTIVE = true;
    public static boolean THREAD_ERROR = false;
    public static long CONCURRENCY_TEST_TIME = 30 * 1000; // 30 secs

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
        PathSearchResult<Long> rs = match(search, 0, null);
        log.log(Level.INFO, "Search 1 took: {0}", System.currentTimeMillis() - ms + "ms (id" + rs.getNode().getId() + ")");

        // Search 2
        final String search2 = "POST/1/1/98484561/" + (size - 10);
        ms = System.currentTimeMillis();
        search = manager.search(search2);
        rs = match(search, 1, null);
        log.log(Level.INFO, "Search 2 took: {0}", System.currentTimeMillis() - ms + "ms (id" + rs.getNode().getId() + ")");

        log.info("Finished");

    }

    @Test
    public void concurrencyTest() throws Exception {

        // Create many many threads 
        List<Thread> threadsList = new ArrayList<>();
        THREAD_ACTIVE = true;
        for (int i = 0; i < 500; i++) {
            Thread t = new Thread(() -> {
                while (THREAD_ACTIVE) {
                    final String search1 = "POST/1/1/" + (size - 10);

                    try {
                        long ms = System.currentTimeMillis();
                        Collection<PathSearchResult<Long>> search = manager.search(search1);
                        ms = System.currentTimeMillis() - ms;
                        match(search, 0, 43981l);
                        System.out.println("TOOK: " + ms);
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        THREAD_ERROR = true;
                    }

                }
                System.out.println("exit....");
            });

            threadsList.add(t);
        }

        long ms = System.currentTimeMillis();
        //Iniciamos todas as threads
        for (Thread t : threadsList) {
            t.start();
        }

        while (System.currentTimeMillis() - ms > CONCURRENCY_TEST_TIME) {
            Thread.sleep(200);
        }
        THREAD_ACTIVE = false;
        if (THREAD_ERROR) {
            fail("Found error in thread");
        }

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
