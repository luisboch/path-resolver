/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield.exceptions;

import org.ws.shield.engine.Node;

/**
 *
 * @author luis
 */
public class DuplicatedPathException extends Exception {

    private final Node found;
    private final String url;

    public DuplicatedPathException(Node found, String url) {
        super("Duplicated URL: Found: " + found + ", trying this: " + url);
        this.found = found;
        this.url = url;
    }

    public DuplicatedPathException(Node found, String url, String message) {
        super(message);
        this.found = found;
        this.url = url;
    }

    public DuplicatedPathException(Node found, String url, String message, Throwable cause) {
        super(message, cause);
        this.found = found;
        this.url = url;
    }

    public DuplicatedPathException(Node found, String url, Throwable cause) {
        super(cause);
        this.found = found;
        this.url = url;
    }

    public DuplicatedPathException(Node found, String url, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.found = found;
        this.url = url;
    }

    public Node getFound() {
        return found;
    }

    public String getUrl() {
        return url;
    }

}
