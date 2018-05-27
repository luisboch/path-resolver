/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield.engine;

import static org.ws.shield.engine.Node.isPattern;

/**
 *
 * @author luis
 */
public enum Type {
    NUMBER("^([+-]?(\\d+\\.)?\\d+)$"),
    TEXT("");

    private final String regex;

    private Type(String regex) {
        this.regex = regex;
    }

    /**
     * Don't use with simple path, only with already checked pattern
     *
     * @param pattern
     * @return
     */
    public static Definition resolvePattern(String pattern) {
        
        int idx = pattern.indexOf(":");

        if (idx > -1) {
            return new Definition(pattern.substring(0, idx), Type.valueOf(pattern.substring(idx + 1).toUpperCase()));
        }

        return new Definition(pattern, Type.TEXT);
    }

    public String getRegex() {
        return regex;
    }
}
