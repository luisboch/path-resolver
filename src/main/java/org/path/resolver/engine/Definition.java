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
        boolean isPattern = NodeImpl.isPattern(path);
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
