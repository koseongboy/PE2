package model;

public final class Node {
    public final int id;             // useful for debugging / UI prints
    public Node next;                // always non‑null except for HOME
    public Node before;
    public Node warp;

    Node(int id) {
        this.id = id;
    }
}
