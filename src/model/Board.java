package model;
//import java.nio.channels.Pipe.SourceChannel;
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

            n[32].warp = n[31];
            n[31].next = n[30];
            n[30].next = n[0];
            n[29].next = n[0];

            


            start = n[0];
        }
            
    }



    public ArrayList<Piece> getStackedPieces(Node position){
        ArrayList<Piece> result = new ArrayList<>();
        for (Player player : players){
            for (Piece piece : player.getPieces()){
                if ( (piece.getStatus() == Piece.State.ON_BOARD || piece.getStatus() == Piece.State.OVERLAPPED) && position == piece.getPosition()) {
                    result.add(piece);
                }
            }
        }
        return result;
    }


    // ----------------------- false == 한번 더 기회 안줘도 됨 / true == 한번 더 기회 줘야함(caught)
    @SuppressWarnings("unchecked")
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
                        result = true;
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
                                destPiece.path = (ArrayList<Node>) sourcePieces.get(0).path.clone();
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
                            result = true;
                        }
                    }
                }
            }

            // 정상적인 윳 나옴
            else{

                // 마지막 목적지 노드와 해당 경로를 미리 계산
                Node destNode = sourcePieces.get(0).path.getLast();
                ArrayList<Node> tempPath = new ArrayList<>();
                tempPath.add(destNode);
                for (int i = -1 ; i<steps ; i++){
                    if ( (i == -1 && destNode.warp != null) ||  ( (type == 4 && destNode.id == 26) && tempPath.get(tempPath.size() - 2).id == 26 )) {
                        destNode = destNode.warp; // enter diagonal
                        tempPath.add(destNode);
                    }
                    else {
                        destNode = destNode.next;
                        tempPath.add(destNode);
                    }

                    // 처음 말을 움직이는게 아닌 상황에서, 말이 1로 이동할 경우 완주
                    if (destNode == start.next){
                        for (Piece sourcePiece : sourcePieces){
                            sourcePiece.setPosition(null);
                            sourcePiece.path.clear();
                            sourcePiece.setStatus(Piece.State.FINISHED);
                            sourcePiece.setCount(1);
                        }
                        return result;
                    }
                }

                ArrayList<Piece> destPieces = getStackedPieces(destNode);
                // 마지막 목적지 노드가 비어있는 경우
                if (destPieces.isEmpty()){

                    for (Piece sourcePiece : sourcePieces){
                        sourcePiece.setPosition(destNode);
                        sourcePiece.path.addAll(tempPath);
                    }
                }

                // 마지막 목적지 노드가 안 비워져 잇는 경우
                else{

                    // grouping
                    if(destPieces.get(0).getOwner() == sourcePieces.get(0).getOwner()){
                        int count = sourcePieces.size() + destPieces.size();

                        // source 피스들
                        for (Piece sourcePiece : sourcePieces){
                            sourcePiece.setPosition(destNode);
                            sourcePiece.path.addAll(tempPath);
                            sourcePiece.setStatus(Piece.State.OVERLAPPED);
                            sourcePiece.setCount(count);
                        }

                        // dest 피스들
                        for (Piece destPiece : destPieces){
                            destPiece.path = (ArrayList<Node>) sourcePieces.get(0).path.clone();
                            destPiece.setStatus(Piece.State.OVERLAPPED);
                            destPiece.setCount(count);
                        }
                    }

                    // catching
                    else{
                        // source 피스들
                        for (Piece sourcePiece : sourcePieces){
                            sourcePiece.setPosition(destNode);
                            sourcePiece.path.addAll(tempPath);
                        }

                        // dest 피스들
                        for (Piece destPiece : destPieces){
                            destPiece.setPosition(null);
                            destPiece.path.clear();
                            destPiece.setStatus(Piece.State.WAITING);
                            destPiece.setCount(1);
                        }
                        result = true;
                    }
                }
            }
        }
        return result;
    }
        
}