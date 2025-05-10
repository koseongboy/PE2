import java.util.List;

import javax.swing.JButton;
class Piece {
    Node current;
    List<Node> getMovableNodes(int steps) {
        /* ... */
    }
    void moveTo(Node n) { current = n; }
}

class Node extends JButton {            // one board spot
    final int id;
    Node(int id) { this.id = id; }
}

enum GameState { SELECT_PIECE, SELECT_NODE }

class Manager {
    private final List<Node> allNodes;
    private final Player[] users;
    private int currentPlayer;                              //현재 턴의 플레이어
    private int moveCount;                                  //윳던지기 의 결과
    private Piece selectedPiece;                            //선택된 말
    private GameState state = GameState.SELECT_PIECE;       

    Game(Player[] users) { this.users = users; }

    //한명의 턴을 진행
    void startTurn() {
        moveCount = throwYut();         //윳던지기
        state = GameState.SELECT_PIECE; //말 선택을 기다리는 상태로 바꿈
    }

    //말이 선택되었을시 호출 -> 하이라이트&노드 선택을 기다림
    void onPieceClicked(Piece p) {
        if (state != GameState.SELECT_PIECE) return;

        
        selectedPiece = p;                       //선택된 말로부터 갈 수 있는 모든 노드를 하이라이트
        highlight(p.getMovableNodes(moveCount));
        state = GameState.SELECT_NODE;           //이동할 노드 선택을 기다리는 상태로 바꿈
    }

    //노드가 선택되었을시 호출 -> 취소 or 해당 노드로 진행
    void onNodeClicked(Node n, boolean rightButton) {
        if (state != GameState.SELECT_NODE) return;

        
        if (rightButton) {              //마우스우클릭시 선택된 말을 취소하고 다시 말 선택을 기다리는 상태로
            clearHighlight();
            selectedPiece = null;
            state = GameState.SELECT_PIECE;
            return;
        }

        
        selectedPiece.moveTo(n);    //노드를 선택시 선택되었던 말을 다음 노드로 이동
        clearHighlight();
        selectedPiece = null;
        endTurn();      //다음 플레이어의 턴을 시작
    }

    

    // ---------- helpers ----------
    private int throwYut() { 
        //1~5를 반환
    }
    private void highlight(List<Node> nodes) { 
        for (Node n : allNodes) n.setEnabled(false);
        for (Node n : nodes)   n.setEnabled(true);
        nodes.forEach(/*선택된 노드들을 전부 칠함*/); 
    }
    private void clearHighlight() { 
        for (Node n : allNodes) {
            n.setEnabled(false);          
            n.setBackground(defaultColor);
        }
        /*모든 노드의 색깔을 원래대로 되돌림*/ 
    }
    private void endTurn() { 
        currentPlayer = (currentPlayer + 1) % users.length; 
        startTurn(); 
    }
}
