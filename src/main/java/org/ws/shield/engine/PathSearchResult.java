/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield.engine;

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
    private final Collection<Parameter> params = new ArrayList<>();
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

    public Collection<Parameter> getParams() {
        return params;
    }

    public void setIteractions(int iteractions) {
        this.iteractions = iteractions;
    }

    public int getIteractions() {
        return iteractions;
    }

}
