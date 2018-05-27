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
package org.path.resolver.engine;

import org.path.resolver.exceptions.DuplicatedPathException;
import org.path.resolver.exceptions.DuplicatedIndexException;
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
