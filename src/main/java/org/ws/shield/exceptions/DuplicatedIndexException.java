/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield.exceptions;

import java.io.Serializable;
import org.ws.shield.engine.Node;

/**
 *
 * @author luis
 */
public class DuplicatedIndexException extends Exception {

    private Serializable index;
    private Node found;

    public DuplicatedIndexException(Serializable index, Node found) {
        this.index = index;
        this.found = found;
    }

    public DuplicatedIndexException(Serializable index, Node found, String message) {
        super(message);
        this.index = index;
        this.found = found;
    }

    public DuplicatedIndexException(Serializable index, Node found, String message, Throwable cause) {
        super(message, cause);
        this.index = index;
        this.found = found;
    }

    public DuplicatedIndexException(Serializable index, Node found, Throwable cause) {
        super(cause);
        this.index = index;
        this.found = found;
    }

    public DuplicatedIndexException(Serializable index, Node found, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.index = index;
        this.found = found;
    }

}
