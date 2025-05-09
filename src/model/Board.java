package model;
import java.util.*;


public class Board {

    private Node start;                    // entry square
    private int type;
    private Node[] n;

    public Board(int type) {
        this.type = type;
        // Build nodes
        if (type == 4) {
            n = new Node[29];
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);


            // Regular outer track (counter‑clockwise)
            for (int i = 0; i < 27; i++) n[i].next = n[(i + 1) % 28];
            //for (int i = 1; i < 29; i++) n[i].before = n[(i - 1) % 28];            
            start = n[0];

            // Diagonals (warp edges) entering centre (#28)
            n[5].warp = n[23];
            n[23].next = n[24];
            n[24].next = n[22];

            n[10].warp = n[25];
            n[25].next = n[26];
            n[26].next = n[22];

            n[22].warp = n[21];
            n[21].next = n[20];
            n[20].next = n[0];

            n[22].next = n[28];
            n[28].next = n[27];
            n[27].next = n[15];

            n[19].next = n[0];
            


            

 
        } else if (type == 5) {
            Node[] n = new Node[36];
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);
            start = n[0];

            // Regular outer track (counter‑clockwise)
            for (int i = 0; i < 34; i++) n[i].next = n[(i + 1) % 35];
            //for (int i = 1; i < 36; i++) n[i].before = n[(i - 1) % 35];

            n[24].next = n[0];

            n[5].warp = n[28];
            //
            n[27].next = n[35];
            n[35].next = n[34];
            n[34].next = n[20];

            n[10].warp = n[30];
            //
            n[31].next = n[27];
            
            n[15].warp = n[32];
            //
            n[33].next = n[27];

            n[27].warp = n[26];
            n[26].next = n[25];
            n[25].next = n[0];


            
        } else if (type == 6) {
            Node[] n = new Node[43];
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);

            // Regular outer track (counter‑clockwise)
            for (int i = 0; i < 41; i++) n[i].next = n[(i + 1) % 42];
            //for (int i = 1; i < 43; i++) n[i].before = n[(i - 1) % 42];
            //  n[41] = n[0];                     // “home” re‑enters start (ends turn)

            n[5].warp = n[33];
            n[10].warp = n[35];
            n[15].warp = n[37];
            n[20].warp = n[39];

            n[36].next = n[32];
            n[34].next = n[32];
            n[38].next = n[32];
            n[40].next = n[32];

            n[32].next = n[42]; //센터 노드 처리
            n[42].next = n[41];
            n[41].next = n[25];

            n[31].next = n[30];
            n[30].next = n[0];
            n[29].next = n[0];


            start = n[0];
        }
            
    }

    // ---------- public API ------------------------------------------------

    // /** Move p by 'steps' squares; returns true if it captured an enemy. */
    // public boolean movePiece(Piece piece, int steps) {
    //     Node start = ((piece.getPosition() == null) ? this.start : piece.getPosition());
    //     Node dest = followPath(start, steps);

    //     // ----- capture / stacking logic ----------
    //     List<Piece> onDest = occ.get(dest);
    //     boolean captured = false;

    //     if (onDest != null && !onDest.isEmpty()) {
    //         if (onDest.get(0).getOwner() != piece.getOwner()) {
    //             /* capture: send enemy pieces back to start */
    //             captured = true;
    //             for (Piece enemy : onDest) {
    //                 enemy.setPosition(null);
    //             }
    //             onDest.clear();
    //         }
    //     }

    //     // ----- actually move ----------------------
    //     occ.computeIfAbsent(dest, d -> new ArrayList<>()).add(piece);
    //     if (piece.getPosition() != null) occ.get(piece.getPosition()).remove(piece);
    //     piece.setPosition(dest);
    //     return captured;
    // }



    // ---------- public API ---------------------------------------------------
    /** Follows regular or diagonal path depending on warp entry squares. */
    public Node followPath(ArrayList<Piece> pieces, int steps) {
        Node cur = pieces.get(0).getPosition();
        if (steps == 0){
            cur = cur.before;
        }else{        
            for (int i = 0; i < steps; i++) {
                if (i == 0 && cur.warp != null || (type==4 && cur.before.id == 26) ) {
                    Node temp = cur;        
                    cur = cur.warp; // enter diagonal
                    cur.before = temp;
                }
                else {
                    Node temp = cur;        
                    cur = cur.next;
                    cur.before = temp;
                }
                if (cur.next == start ) {
                    pieces.get(0).aboutToFinish = true;
                }
                if (pieces.get(0).aboutToFinish && (cur == start.next)){
                    for (Piece piece : pieces){piece.setStatus(Piece.State.FINISHED);}
                }
                
            }

        }
        return cur;
    }
        
}