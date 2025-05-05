package map;
import java.util.*;
public final class Node {
    public final int id;             // useful for debugging / UI prints
    public Node next;                // always non‑null except for HOME

    public Node fromstraightline;
    public Node fromshortcut;

    public Node fromrightdiagonalshortcut;
    public Node fromleftdiagonalshortcut;
    public Node frommiddlediagonalshortcut;
    public Node before;
    public Node startfromcenternext;
    public Node warp;
    Node(int id) {
        this.id = id;
    }
}

public class Board {
    private Node start = new Node(0);                    // entry square
//    private final Node end;                     // final square


    public Board(int style) {
        // Build nodes
        if (style == 4) {
            Node[] n = new Node[29];
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);


            // Regular outer track (counter‑clockwise)
            for (int i = 0; i < 27; i++) n[i].next = n[(i + 1) % 28];
            for (int i = 1; i < 29; i++) n[i].before = n[(i - 1) % 28];
            //n[27] = n[0];                     // “home” re‑enters start (ends turn)

            // Diagonals (warp edges) entering centre (#28)
            n[5].warp = n[23];
            n[10].warp = n[25];
            n[24].next = n[22];
            n[26].next = n[22];
            n[20].next = n[0];
            n[19].next = n[0];
            n[23].before = n[5];
            n[25].before = n[10];
            n[21].before = n[22];
            n[28].before = n[22];
            n[15].fromstraightline = n[14];
            n[15].fromshortcut = n[27];
            n[22].fromleftdiagonalshortcut = n[26];
            n[22].fromrightdiagonalshortcut = n[24];
            n[0].fromstraightline = n[19];
            n[0].fromshortcut = n[20];


            // Centre diagonal exits
            n[22].startfromcenternext = n[21];
            n[22].next = n[28];
            n[27].next = n[15];

            start = n[0];
            // end = n[27];
        } else if (style == 5) {
            Node[] n = new Node[36];
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);

            // Regular outer track (counter‑clockwise)
            for (int i = 0; i < 34; i++) n[i].next = n[(i + 1) % 35];
            for (int i = 1; i < 36; i++) n[i].before = n[(i - 1) % 35];
            //n[34] = n[0];                     // “home” re‑enters start (ends turn)


            n[5].warp = n[28];
            n[10].warp = n[30];
            n[15].warp = n[32];
            n[31].next = n[27];
            n[33].next = n[27];
            n[24].next = n[0];
            n[25].next = n[0];
            n[27].startfromcenternext = n[26];
            n[27].next = n[35];
            n[26].next = n[25];
            n[35].next = n[34];
            n[34].next = n[20];
            n[28].before = n[5];
            n[30].before = n[10];
            n[32].before = n[15];
            n[20].fromstraightline = n[19];
            n[20].fromshortcut = n[34];

            n[0].fromstraightline = n[24];
            n[0].fromshortcut = n[25];
            n[27].fromleftdiagonalshortcut = n[33];
            n[27].fromrightdiagonalshortcut = n[29];
            n[27].frommiddlediagonalshortcut = n[31];
            n[34].before = n[35];
            n[35].before = n[27];
            n[26].before = n[27];
            n[25].before = n[26];

            start = n[0];
            // end = n[34];
        } else if (style == 6) {
            Node[] n = new Node[43];
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);

            // Regular outer track (counter‑clockwise)
            for (int i = 0; i < 41; i++) n[i].next = n[(i + 1) % 42];
            for (int i = 1; i < 43; i++) n[i].before = n[(i - 1) % 42];
            //  n[41] = n[0];                     // “home” re‑enters start (ends turn)

            n[5].warp = n[33];
            n[10].warp = n[35];
            n[15].warp = n[37];
            n[20].warp = n[39];

            n[36].next = n[32];
            n[34].next = n[32];
            n[38].next = n[32];
            n[40].next = n[32];

            n[32].startfromcenternext = n[31]; //센터 노드 처리
            n[32].next = n[42]; //센터 노드 처리

            n[31].next = n[30];
            n[30].next = n[0];
            n[29].next = n[0];

            n[33].before = n[5]; //빽도 처리
            n[35].before = n[10];
            n[37].before = n[15];
            n[39].before = n[20];
            n[32].frommiddlediagonalshortcut = n[36];
            n[32].fromrightdiagonalshortcut = n[34];
            n[32].fromleftdiagonalshortcut = n[38];
            n[0].fromshortcut = n[30];
            n[0].fromstraightline = n[29];
            n[25].fromstraightline = n[24];
            n[25].fromshortcut = n[41];
            n[41].before = n[42];
            n[42].before = n[32];
            n[31].before = n[32];
            n[30].before = n[31];

            start = n[0];
            //   end = n[41];
        }
    }
}
