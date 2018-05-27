/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield.engine;

/**
 *
 * @author luis
 */
public class Definition {

    private String name;
    private Type type;

    public Definition(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean check(String url) {
        switch (type) {
            case NUMBER:
                return url.matches(Type.NUMBER.getRegex());
            default:
                return !url.matches(Type.NUMBER.getRegex());
        }
    }

    public boolean match(String path) {
        boolean isPattern = Node.isPattern(path);
        path = path == null ? "" : path.trim();
        if (!isPattern) {
            return type == Type.TEXT;
        } else {
            path = path.replace("{", "").replace("}", "").trim();
            int idx = path.indexOf(":");
            if (idx > 0) {
                Type tp = Type.valueOf(path.substring(idx + 1).trim().toUpperCase());
                return type.equals(tp);
            } else {
                return type.equals(Type.TEXT);
            }
        }
    }

    public String print() {
        return name + ":" + type.name().toLowerCase();
    }

    @Override
    public String toString() {
        return "Definition{" + "name=" + name + ", type=" + type + '}';
    }

}
