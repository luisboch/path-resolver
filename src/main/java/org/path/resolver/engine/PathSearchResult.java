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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author luis
 * @param <A>
 */
public class PathSearchResult<A extends Serializable> {

    private final Node<A> node;
    private final Collection<Parameter<A>> params = new ArrayList<>();
    private int iteractions = 0;

    public PathSearchResult(Node<A> node) {
        this.node = node;
    }

    public PathSearchResult(Node<A> node, Collection<Parameter<A>> params) {
        this.node = node;
        this.params.addAll(params);
    }

    public Node<A> getNode() {
        return node;
    }

    public Collection<Parameter<A>> getParams() {
        return params;
    }

    public void setIteractions(int iteractions) {
        this.iteractions = iteractions;
    }

    public int getIteractions() {
        return iteractions;
    }

}
