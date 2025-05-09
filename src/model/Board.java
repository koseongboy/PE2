package model;
import java.nio.channels.Pipe.SourceChannel;
import java.util.*;


public class Board {

    public static Node start;                    // entry square
    public static int type;
    public static Node[] n;
    public ArrayList<Player> players;

    public Board(int type, ArrayList<Player> players) {
        this.players = players;
        Board.type = type;
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
            n[29].next = n[27];
            
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


    public ArrayList<Piece> getStackedPieces(Node position){
        ArrayList<Piece> result = new ArrayList<>();
        for (Player player : players){
            for (Piece piece : player.getPieces()){
                if (position.id == piece.getPosition().id){
                    result.add(piece);
                }
            }
        }
        return result;
    }


    // ----------------------- false == 한번 더 기회 안줘도 됨 / true == 한번 더 기회 줘야함(caught)
    public Boolean followPath(ArrayList<Piece> sourcePieces, int steps) {

        boolean result = false;

        // 처음 말을 움직일 경우
        if (sourcePieces.get(0).getStatus() == Piece.State.WAITING){
            Piece sourcePiece = sourcePieces.get(0);

            // 백도 나옴
            if(steps == 5){
                ArrayList<Piece> stackedPieces = getStackedPieces(start);
                
                // 시작점이 비어있음
                if (stackedPieces.isEmpty()){
                    sourcePiece.setPosition(start);
                    sourcePiece.path.add(start);
                    sourcePiece.setStatus(Piece.State.ON_BOARD);
                }

                // 시작점이 안비어있음
                else{

                    // 그루핑
                    if ( stackedPieces.get(0).getOwner() == sourcePiece.getOwner()){
                        
                        // source 피스들을 담당
                        sourcePiece.setPosition(start);
                        sourcePiece.path.add(start);
                        sourcePiece.setStatus(Piece.State.OVERLAPPED);
                        int count = 1 + stackedPieces.size();
                        sourcePiece.setCount(count);

                        // dest piece 들의 상태 변경
                        for (Piece destPiece : stackedPieces){
                            destPiece.setStatus(Piece.State.OVERLAPPED);
                            destPiece.setCount(count);
                        }
                    }

                    // catching
                    else{

                        // source 피스들
                        sourcePiece.setPosition(start);
                        sourcePiece.path.add(start);
                        sourcePiece.setStatus(Piece.State.ON_BOARD);

                        // dest 피스들
                        for (Piece destPiece : stackedPieces){
                            destPiece.setPosition(null);
                            destPiece.path.clear();
                            destPiece.setStatus(Piece.State.WAITING);
                            destPiece.setCount(1);
                        }
                        result = true;
                    }
                }
            }
            
            // 백도가 아님
            else{
                Node destination = start;
                ArrayList<Node> tempPath = new ArrayList<>();
                tempPath.add(destination);
                for (int i = -1; i < steps; i++) {
                    destination = destination.next;
                    tempPath.add(destination);
                }
                ArrayList<Piece> stackedPieces = getStackedPieces(destination);

                // 목적지가 비어있음
                if (stackedPieces.isEmpty()){
                    sourcePiece.setPosition(destination);
                    sourcePiece.path.addAll(tempPath);
                    sourcePiece.setStatus(Piece.State.ON_BOARD);
                }

                // 안비어있음
                else{

                    // grouping
                    if (stackedPieces.get(0).getOwner() == sourcePiece.getOwner()){
                        
                        // source 피스들을 담당
                        sourcePiece.setPosition(destination);
                        sourcePiece.path.addAll(tempPath);
                        sourcePiece.setStatus(Piece.State.OVERLAPPED);
                        int count = 1 + stackedPieces.size();
                        sourcePiece.setCount(count);

                        // dest piece 들의 상태 변경
                        for (Piece destPiece : stackedPieces){
                            destPiece.setStatus(Piece.State.OVERLAPPED);
                            destPiece.setCount(count);
                        }

                    }

                    // catching
                    else{

                        // source 피스들
                        sourcePiece.setPosition(destination);
                        sourcePiece.path.addAll(tempPath);
                        sourcePiece.setStatus(Piece.State.ON_BOARD);

                        // dest 피스들
                        for (Piece destPiece : stackedPieces){
                            destPiece.setPosition(null);
                            destPiece.path.clear();
                            destPiece.setStatus(Piece.State.WAITING);
                            destPiece.setCount(1);
                        }
                    }
                }
            }
        }

        // 이미 보드에 있는 말을 움직이는 경우
        else{

            // 백도 나옴
            if (steps == 5){

                // 과거 없이, 시작점에서 백도가 나온 경우
                if (sourcePieces.get(0).path.size() == 1 && sourcePieces.get(0).path.get(0).id == start.id ){

                }

                // 그렇지 않을 경우 정상적으로 백도
                else{

                    int secondToLastIndex = sourcePieces.get(0).path.size() - 2 ;
                    Node previousNode = sourcePieces.get(0).path.get(secondToLastIndex);
                    ArrayList<Piece> destPieces = getStackedPieces(previousNode);  // 현재 위치 직전 노드 위치를 가지고 반환

                    // 백도한 위치가 비어있을 경우
                    if (destPieces.isEmpty()){
                        for (Piece sourcePiece : sourcePieces){
                            sourcePiece.setPosition(previousNode);
                            sourcePiece.path.removeLast();
                        }

                    }

                    // 백도한 위치가 안 비워져 있을 경우
                    else{

                        // grouping 일 경우
                        if(destPieces.get(0).getOwner() == sourcePieces.get(0).getOwner()){

                            int count = sourcePieces.size() + destPieces.size();

                            // source 피스들
                            for (Piece sourcePiece : sourcePieces){
                                sourcePiece.setPosition(previousNode);
                                sourcePiece.path.removeLast();
                                sourcePiece.setStatus(Piece.State.OVERLAPPED);
                                sourcePiece.setCount(count);
                            }

                            // dest 피스들
                            for (Piece destPiece : destPieces){
                                destPiece.path = sourcePieces.get(0).path;
                                destPiece.setStatus(Piece.State.OVERLAPPED);
                                destPiece.setCount(count);
                            }
                        }

                        // catching 일 경우
                        else{

                            // source 피스들
                            for (Piece sourcePiece : sourcePieces){
                                sourcePiece.setPosition(previousNode);
                                sourcePiece.path.removeLast();
                            }

                            // dest 피스들
                            for (Piece destPiece : destPieces){
                                destPiece.setPosition(null);
                                destPiece.path.clear();
                                destPiece.setStatus(Piece.State.WAITING);
                                destPiece.setCount(1);
                            }
                        }
                    }
                }
            }

            // 정상적인 윳 나옴
            else{


                
            }




        }
        else {
            cur = sourcePieces.get(0).getPosition();
        }
        if (steps == 5){
            if (sourcePieces.get(0).aboutToFinish == false){
                return cur;
            }
            cur = cur.before;
        }else{        
            for (int i = -1; i < steps; i++) {
                if (i == -1 && cur.warp != null || (type==4 && (cur.before != null) && cur.before.id == 26) ) {
                    Node temp = cur;
                    cur = cur.warp; // enter diagonal
                    for (Piece piece : sourcePieces){
                        piece.path.add(temp);
                    }
                }
                else {
                    Node temp = cur;        
                    cur = cur.next;
                    for (Piece piece : sourcePieces){
                        piece.path.add(temp);
                    }
                }
                if()
                
            }

        }
        return cur;
    }
        
}