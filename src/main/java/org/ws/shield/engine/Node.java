/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ws.shield.engine;

import org.ws.shield.exceptions.DuplicatedPathException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 *
 * @author luis
 * @param <A>
 */
public class Node<A extends Serializable> {

    private static final String EMPTY = ""; // Just memory improvement.

    
    // List to lookup when using pattern
    private final Collection<Node<A>> children = new ArrayList<>();
    private final Node<A> parent;
    private final String path;
    private final Definition definition;
    private final boolean isPattern;
    private final short level;

    private String fullPath;
    private A id;

    public Node() {
        parent = null;
        path = null;
        level = 0;
        isPattern = false;
        definition = null;
    }

    public static boolean isPattern(String node) {
        return node != null && node.startsWith("{") && node.endsWith("}");
    }

    public Node(Node parent, String path) {
        this.parent = parent;
        this.level = (short) (parent.level + 1);

        // Path resolving
        path = path == null ? EMPTY : path.trim();
        this.isPattern = isPattern(path);

        if (isPattern) {
            path = path.substring(1, path.length() - 1);
            definition = Type.resolvePattern(path);
        } else {
            definition = null;
        }

        this.path = path;
    }

    public Node getParent() {
        return parent;
    }

    public String getPathern() {
        return path;
    }

    public short getLevel() {
        return level;
    }

    public Node add(String url, A id) throws DuplicatedPathException {
        return add(url, url, id);
    }

    private Node add(String url, String fullPath, A id) throws DuplicatedPathException {

        if (url == null || url.isEmpty() || url.equals("/")) {
            return this;
        }

        int idx = url.indexOf("/");
        final String lPath;
        final String remaining;

        if (idx > -1) {
            lPath = url.substring(0, idx);
            remaining = url.substring(idx + 1);
        } else {
            lPath = url;
            remaining = EMPTY;
        }

        Node node = null;

        for (Node n : children) {
            if (n.matchThis(lPath)) {
                if (remaining.equals(EMPTY) && n.getId() != null) {
                    throw new DuplicatedPathException(n, fullPath);
                }
                node = n;
                break;
            }
        }

        if (node == null) {
            node = new Node(this, lPath);
            children.add(node);
        }

        if (remaining.equals(EMPTY)) {
            node.fullPath = fullPath;
            node.id = id;
            if (Objects.equals(node.path, lPath)) {
                //System.out.println("WARN: CONFLICTING PATH: " + path + "!=" + lPath);
            }
        } else {
            return node.add(remaining, fullPath, id);
        }

        return node;
    }

    public Collection<PathSearchResult<A>> search(String request) {
        final HashSet<PathSearchResult<A>> found = new HashSet<>();
        search(request, found, new ArrayList<>());
        return found;
    }

    public boolean matchAny(String request) {
        return !search(request).isEmpty();
    }

    public void search(String url, Collection<PathSearchResult<A>> found, Collection<Parameter<A>> params) {

        params = new ArrayList<>(params); // Clone list to correct delegation.

        if (url == null || url.isEmpty() || url.equals("/")) {
            return;
        }

        if (this.path == null) {
            // ROOT node, then just passthrough
            for (Node node : children) {
                node.search(url, found, params);
            }
        }

        int idx = url.indexOf("/");
        final String searchPath;
        final String remaining;

        if (idx > -1) {
            searchPath = url.substring(0, idx);
            remaining = url.substring(idx + 1);
        } else {
            searchPath = url;
            remaining = EMPTY;
        }

        boolean urlMatch = false;

        if (isPattern) {
            if (definition.check(searchPath)) {
                Parameter<A> param = new Parameter<>(this, definition.getName(), searchPath);
                params.add(param);
                urlMatch = true;
            }
        } else if (searchPath.equals(this.path)) {
            urlMatch = true;
        }

        if (urlMatch) {
            if ((remaining.equals(EMPTY) || children.isEmpty()) && this.id != null) {
                found.add(new PathSearchResult<>(this, params));
            } else {
                if (!remaining.equals(EMPTY)) {
                    for (Node node : children) {
                        node.search(remaining, found, params);
                    }
                }
            }
        }

    }

    private boolean matchThis(String val) {
        if (isPattern) {
            return definition.match(val);
        }

        return val.equals(this.path);
    }

    public String getPath() {
        return path;
    }

    public boolean isIsPattern() {
        return isPattern;
    }

    public String getFullPath() {
        return fullPath;
    }

    public A getId() {
        return id;
    }

    public String print() {
        final StringBuilder b = new StringBuilder();

        for (int i = level; i > 0; i--) {
            b.append("\t");
        }

        if (isPattern) {
            b.append("/{").append(definition.print());
        } else {
            b.append("/").append(path == null ? "" : path);
        }
        if (id != null) {
            b.append(":").append(id).append(isPattern ? "}" : "");
        }
        b.append("\n");
        children.forEach((n) -> {
            b.append(n.print());
        });

        return b.toString();
    }

    @Override
    public String toString() {
        return "Node{fullPath=" + fullPath + ", children=" + children + ", path=" + path + ", definition=" + definition + ", id=" + id + '}';
    }

}
