/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield.engine;

import java.io.Serializable;

/**
 *
 * @author luis
 */
public class Parameter<A extends Serializable> {
    
    private Node<A> node;
    private String parameter;
    private String value;

    public Parameter(Node<A> node, String parameter, String value) {
        this.node = node;
        this.parameter = parameter;
        this.value = value;
    }

    public Node<A> getNode() {
        return node;
    }

    public void setNode(Node<A> node) {
        this.node = node;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
