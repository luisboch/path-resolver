/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield.engine;

import org.ws.shield.exceptions.DuplicatedPathException;
import org.ws.shield.exceptions.DuplicatedIndexException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luis
 * @param <A>
 */
public class TreeManager<A extends Serializable> {

    private final Map<A, Node<A>> indexedMap = new HashMap<>();

    private Node<A> rootNode = new Node<>();

    private TreeManager() {
    }

    public TreeManager<A> addUrl(String url, A id) throws DuplicatedPathException, DuplicatedIndexException {

        if (indexedMap.containsKey(id)) {
            throw new DuplicatedIndexException(id, indexedMap.get(id));
        }

        final Node<A> node = rootNode.add(url, id);
        indexedMap.put(id, node);
        return this;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Node<A> getByIndex(A index) {
        return indexedMap.get(index);
    }

    public Collection<PathSearchResult<A>> search(String url) {
        return rootNode.search(url);
    }

    public long getIndexSize() {
        return indexedMap.size();
    }

    public void clear() {
        rootNode = new Node<>();
        indexedMap.clear();
    }

    public static synchronized <A extends Serializable> TreeManager<A> getManager(Class<A> indexType) {
        return new TreeManager<>();
    }

    public void printInfo() {
        StringBuilder b = new StringBuilder();
        b.append("---------------------------------------------------------");
        b.append("Total de nós indexados: ").append(indexedMap.size()).append("\n");
        b.append("Tree: \n");
        b.append(rootNode.print());
        b.append("---------------------------------------------------------");
        System.out.println(b.toString());
    }

}
