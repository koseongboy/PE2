package map;

import java.util.*;
public final class Node {
    public final int id;             // useful for debugging / UI prints
    public Node next;                // always non?‘null except for HOME
    public Node warp;                // null unless this is an entry square
    Node(int id) {
        this.id = id;
    }
}

