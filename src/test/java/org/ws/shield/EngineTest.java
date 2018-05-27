/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield;

import org.ws.shield.engine.PathSearchResult;
import org.ws.shield.engine.TreeManager;
import org.ws.shield.exceptions.DuplicatedIndexException;
import org.ws.shield.exceptions.DuplicatedPathException;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author luis
 */
public class EngineTest {

    private static Logger log = Logger.getLogger(EngineTest.class.getSimpleName());
    private final TreeManager<Integer> manager = TreeManager.getManager(Integer.class);

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

    public EngineTest() {

    }

    @Test(expected = DuplicatedPathException.class)
    public void duplicatedPathTest() throws Exception {
        setupTest();
        manager.addUrl("GET/module3/path-service/pag/{pagination}/{cod}/", 27);
        fail("Duplicated!");
    }

    @Test(expected = DuplicatedIndexException.class)
    public void duplicatedIndexTest() throws Exception {
        setupTest();
        manager.addUrl("GET/module3/path-service/pag/{pagination}/{cod}/", 2);
        fail("Duplicated!");
    }

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

    @Test
    public void searchTest() throws Exception {
        manager.clear();

        // Teste complexo
        manager.addUrl("POST/module3/complex/{cidade:NUMBER}/{nome}/{cep:NUMBER}", 1);
        manager.addUrl("POST/module3/complex/{cidade:NUMBER}/{percent:NUMBER}/{cep:NUMBER}", 2);
        manager.addUrl("POST/module3/complex/{cidade}/{cod:NUMBER}/{cep:NUMBER}", 3);
        manager.addUrl("POST/module3/complex/{cidade}/{nome}/{cep:NUMBER}", 4);
        manager.addUrl("POST/module3/complex/{cidade}/{nome}/{bairro}", 5);
        manager.addUrl("POST/module3/complex/{cidade:NUMBER}/{nome}/{cep}", 6);
        manager.addUrl("POST/module3/complex/{cidade:NUMBER}/{nome:NUMBER}/{bairro}", 7);
        manager.addUrl("POST/module3/complex/{cidade:NUMBER}/{nome:NUMBER}", 8);

        manager.printInfo();

        Collection<PathSearchResult<Integer>> search = manager.search("POST/module3/complex/teste/1.5/JOSE");

        log.log(Level.INFO, "Simple search test: {0}", search.size());

        if (!search.isEmpty()) {
            fail("Não deve encontrar, encontrou " + search.size());
        }

        log.info("Simple search test: 1");
        // Deve encontrar um especifico
        search = manager.search("POST/module3/complex/1.5/nome_teste/1");
        if (search.isEmpty() || search.size() != 1) {
            fail("Pesquisa incorreta");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 1);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("cidade") && !p.getValue().equals("1.5")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("nome") && !p.getValue().equals("nome_teste")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("1")) {
                    Assert.fail("Parametro incorreto!");
                }
            });
        }
        log.info("Simple search test: 2");
        search = manager.search("POST/module3/complex/1.5/755.6655/1");
        if (search.isEmpty() || search.size() != 1) {
            fail("Pesquisa incorreta");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 2);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("cidade") && !p.getValue().equals("1.5")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("percent") && !p.getValue().equals("755.6655")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("1")) {
                    Assert.fail("Parametro incorreto!");
                }
            });
        }
        log.info("Simple search test: 3");
        search = manager.search("POST/module3/complex/curitiba/755.6655/1");
        if (search.isEmpty() || search.size() != 1) {
            fail("Pesquisa incorreta");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 3);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("cidade") && !p.getValue().equals("curitiba")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("percent") && !p.getValue().equals("755.6655")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("1")) {
                    Assert.fail("Parametro incorreto!");
                }
            });
        }
        log.info("Simple search test: 4");
        search = manager.search("POST/module3/complex/curitiba/jose/1");
        if (search.isEmpty() || search.size() != 1) {
            fail("Pesquisa incorreta");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 4);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("cidade") && !p.getValue().equals("curitiba")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("nome") && !p.getValue().equals("jose")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("1")) {
                    Assert.fail("Parametro incorreto!");
                }
            });
        }

        log.info("Simple search test: 5");
        search = manager.search("POST/module3/complex/curitiba/jose/uberaba");
        if (search.isEmpty() || search.size() != 1) {
            fail("Pesquisa incorreta");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 5);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("cidade") && !p.getValue().equals("curitiba")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("nome") && !p.getValue().equals("jose")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("bairro") && !p.getValue().equals("uberaba")) {
                    Assert.fail("Parametro incorreto!");
                }
            });
        }

        log.info("Simple search test: 6");
        search = manager.search("POST/module3/complex/8555/jose/uberaba");
        if (search.isEmpty() || search.size() != 1) {
            fail("Pesquisa incorreta");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 6);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("cidade") && !p.getValue().equals("8555")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("nome") && !p.getValue().equals("jose")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("cep") && !p.getValue().equals("uberaba")) {
                    Assert.fail("Parametro incorreto!");
                }
            });
        }

        log.info("Simple search test: 7");
        // manager.addUrl("POST/module3/complex/{cidade:NUMBER}/{nome:NUMBER}/{bairro}", 7);
        search = manager.search("POST/module3/complex/8555/755/uberaba");
        if (search.isEmpty() || search.size() != 1) {
            fail("Pesquisa incorreta");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 3);
            assertEquals(found.getNode().getId().longValue(), 7);

            found.getParams().stream().forEach((p) -> {
                if (p.getParameter().equals("cidade") && !p.getValue().equals("8555")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("nome") && !p.getValue().equals("755")) {
                    Assert.fail("Parametro incorreto!");
                } else if (p.getParameter().equals("bairro") && !p.getValue().equals("uberaba")) {
                    Assert.fail("Parametro incorreto!");
                }
            });
        }
        log.info("Simple search test: 8");
        search = manager.search("POST/module3/complex/8555/755");
        if (search.isEmpty() || search.size() != 1) {
            fail("Pesquisa incorreta");
        } else {
            // vamos verificar os parametros
            PathSearchResult<Integer> found = search.iterator().next();
            assertTrue(found.getParams().size() == 2);
            assertEquals(found.getNode().getId().longValue(), 8);

            found.getParams().forEach((p) -> {
                if (p.getParameter().equals("cidade") && !p.getValue().equals("8555")) {
                    Assert.fail("Parametro incorreto!");
//                } else if (p.getParameter().equals("nome") && !p.getValue().equals("756")) {
//                    Assert.fail("Parametro incorreto!");
                }
            });
        }

    }
}
