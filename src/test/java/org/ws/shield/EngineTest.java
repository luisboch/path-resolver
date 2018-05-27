/*
 * The MIT License
 *
 * Copyright 2018 luis.
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
package org.ws.shield;

import org.path.resolver.engine.PathSearchResult;
import org.path.resolver.engine.TreeManager;
import org.path.resolver.exceptions.DuplicatedIndexException;
import org.path.resolver.exceptions.DuplicatedPathException;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author luis
 */
public class EngineTest {

    private static Logger log = Logger.getLogger(EngineTest.class.getSimpleName());
    private final TreeManager<Integer> manager = TreeManager.getManager(Integer.class);

    /**
     * Create a list of Generic ulrs, to make a simple test.
     *
     * @throws DuplicatedPathException
     * @throws DuplicatedIndexException
     */
    public void setupTest() throws DuplicatedPathException, DuplicatedIndexException {
        manager.clear();
        manager.addUrl("POST/module1/path-service/pag/none", 1);
        manager.addUrl("GET/module1/path-service/pag/{pagination}/{cod}", 2);
        manager.addUrl("PUT/module1/path-service/pag", 3);
        manager.addUrl("POST/module1/path-service/user/none", 4);
        manager.addUrl("GET/module1/path-service/master/{pagination}/{cod}", 5);
        manager.addUrl("PUT/module1/path-service/urls", 6);

        manager.addUrl("POST/module2/path-service/pag/none", 7);
        manager.addUrl("GET/module2/path-service/pag/{pagination}/{cod}", 8);
        manager.addUrl("PUT/module2/path-service/pag", 9);
        manager.addUrl("POST/module2/path-service/user/none", 10);
        manager.addUrl("GET/module2/path-service/master/{pagination}/{cod}", 11);
        manager.addUrl("PUT/module2/path-service/urls", 12);

        manager.addUrl("POST/module3/path-service/pag/none", 13);
        manager.addUrl("POST/module3/path-service/pag/none/{cod}", 14);
        manager.addUrl("GET/module3/path-service/pag/{pagination}/{cod}", 15);

        manager.addUrl("GET/module3/path-service/pag/{pagination}/{cod:NUMBER}", 16);
        manager.addUrl("PUT/module3/path-service/pag", 17);
        manager.addUrl("POST/module3/path-service/user/none", 18);
        manager.addUrl("GET/module3/path-service/master/{pagination}/{cod}", 19);
        manager.addUrl("PUT/module3/path-service/urls", 20);

        // Teste complexo
    }

    /**
     * Validate {@link DuplicatedPathException}
     *
     * @throws Exception
     */
    @Test(expected = DuplicatedPathException.class)
    public void duplicatedPathTest() throws Exception {
        setupTest();
        manager.addUrl("GET/module3/path-service/pag/{pagination}/{cod}/", 27);
        fail("Duplicated!");
    }

    /**
     * Validate {@link DuplicatedIndexException}
     *
     * @throws Exception
     */
    @Test(expected = DuplicatedIndexException.class)
    public void duplicatedIndexTest() throws Exception {
        setupTest();
        manager.addUrl("GET/module3/path-service/pag/{pagination}/{cod}/", 2);
        fail("Duplicated!");
    }

    /**
     * Check generated Three using indexed search.
     *
     * @throws Exception
     */
    @Test
    public void byIndexTest() throws Exception {
        setupTest();
        // Deve Encontrar
        for (int i = 1; i < 21; i++) {
            if (manager.getByIndex(i) == null) {
                fail("Não encontrou " + i);
            }
        }
        // Não deve encontrar
        if (manager.getByIndex(27) != null) {
            fail("Não encontrou 27");
        }

        manager.printInfo();
    }

    /**
     * Most complex test. Register some URLS, and check it, one by one.
     *
     * @throws Exception
     */
    @Test
    public void searchTest() throws Exception {
        manager.clear();

        manager.addUrl("POST/module3/complex/{city:NUMBER}/{name}/{cep:NUMBER}", 1);
        manager.addUrl("POST/module3/complex/{city:NUMBER}/{percent:NUMBER}/{cep:NUMBER}", 2);
        manager.addUrl("POST/module3/complex/{city}/{cod:NUMBER}/{cep:NUMBER}", 3);
        manager.addUrl("POST/module3/complex/{city}/{name}/{cep:NUMBER}", 4);
        manager.addUrl("POST/module3/complex/{city}/{name}/{location}", 5);
        manager.addUrl("POST/module3/complex/{city:NUMBER}/{name}/{cep}", 6);
        manager.addUrl("POST/module3/complex/{city:NUMBER}/{name:NUMBER}/{location}", 7);
        manager.addUrl("POST/module3/complex/{city:NUMBER}/{name:NUMBER}", 8);

        manager.printInfo();

        Collection<PathSearchResult<Integer>> search = manager.search("POST/module3/complex/teste/1.5/JOSE");

        log.log(Level.INFO, "Simple search test: {0}", search.size());

        if (!search.isEmpty()) {
            fail("Must be empty result, but found: " + search.size());
        }

        log.info("Simple search test: 1");
        search = manager.search("POST/module3/complex/1.5/name_teste/1");
        if (search.isEmpty() || search.size() != 1) {
            fail("Result Search is Wrong");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 1);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("city") && !p.getValue().equals("1.5")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("name") && !p.getValue().equals("name_teste")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("1")) {
                    fail("Parameter is Wrong");
                }
            });
        }
        log.info("Simple search test: 2");
        search = manager.search("POST/module3/complex/1.5/755.6655/1");
        if (search.isEmpty() || search.size() != 1) {
            fail("Result Search is Wrong");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 2);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("city") && !p.getValue().equals("1.5")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("percent") && !p.getValue().equals("755.6655")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("1")) {
                    fail("Parameter is Wrong");
                }
            });
        }
        log.info("Simple search test: 3");
        search = manager.search("POST/module3/complex/curitiba/755.6655/1");
        if (search.isEmpty() || search.size() != 1) {
            fail("Result Search is Wrong");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 3);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("city") && !p.getValue().equals("curitiba")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("percent") && !p.getValue().equals("755.6655")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("1")) {
                    fail("Parameter is Wrong");
                }
            });
        }
        log.info("Simple search test: 4");
        search = manager.search("POST/module3/complex/curitiba/jose/1");
        if (search.isEmpty() || search.size() != 1) {
            fail("Result Search is Wrong");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 4);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("city") && !p.getValue().equals("curitiba")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("name") && !p.getValue().equals("jose")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("1")) {
                    fail("Parameter is Wrong");
                }
            });
        }

        log.info("Simple search test: 5");
        search = manager.search("POST/module3/complex/curitiba/jose/uberaba");
        if (search.isEmpty() || search.size() != 1) {
            fail("Result Search is Wrong");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 5);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("city") && !p.getValue().equals("curitiba")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("name") && !p.getValue().equals("jose")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("location") && !p.getValue().equals("uberaba")) {
                    fail("Parameter is Wrong");
                }
            });
        }

        log.info("Simple search test: 6");
        search = manager.search("POST/module3/complex/8555/jose/uberaba");
        if (search.isEmpty() || search.size() != 1) {
            fail("Result Search is Wrong");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 6);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("city") && !p.getValue().equals("8555")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("name") && !p.getValue().equals("jose")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("uberaba")) {
                    fail("Parameter is Wrong");
                }
            });
        }

        log.info("Simple search test: 7");
        // manager.addUrl("POST/module3/complex/{city:NUMBER}/{name:NUMBER}/{location}", 7);
        search = manager.search("POST/module3/complex/8555/755/uberaba");
        if (search.isEmpty() || search.size() != 1) {
            fail("Result Search is Wrong");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 7);

            found.getParams().stream().forEach((p) -> {
                if (p.getParameter().equals("city") && !p.getValue().equals("8555")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("name") && !p.getValue().equals("755")) {
                    fail("Parameter is Wrong");
                } else if (p.getParameter().equals("location") && !p.getValue().equals("uberaba")) {
                    fail("Parameter is Wrong");
                }
            });
        }
        log.info("Simple search test: 8");
        search = manager.search("POST/module3/complex/8555/755");
        if (search.isEmpty() || search.size() != 1) {
            fail("Result Search is Wrong");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 2);
            assertEquals(found.getNode().getId().longValue(), 8);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("city") && !p.getValue().equals("8555")) {
                    fail("Parameter is Wrong");
                    // in this case, parameter name my change (what we can do?)
//                } else if (p.getParameter().equals("name") && !p.getValue().equals("756")) {
//                    fail("Parameter is Wrong");
                }
            });
        }

    }
}
