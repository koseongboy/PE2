//grouping 된 애들이 들어왔을때 제대로 동작하는지 체크 - setPosition 과 setStatus 가 동시에 일어나는지 체크해야함
package manager;
import view.*;
import model.*;

import java.security.cert.PKIXReason;
import java.util.ArrayList;


//change users into players


public class Manager{
    private View ui = new UI();
    private Board board;    
    private int currentPlayer = 0;
    private Player[] players;
    private int permittedThrows = 1;
    private int[] arr = new int[6];
    private ArrayList<Integer> permittedMoves = new ArrayList<>();
    
    public static void main(String[] args) {
        Manager manager = new Manager();  
        manager.runGame();                
    }

    public void runGame(){
        //setup map,users,pieces
        System.err.println("test");
        int[] gameState = ui.gameSetup();
        int mapType = gameState[0];     //setup board
        board = new Board(mapType);
        int playerCount = gameState[1]; //setup 
        int pieceCount = gameState[2];
        players = new Player[playerCount];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(pieceCount);  
        }

        //game start : turn starts from 0Z
        int finishedPlayers = 0 ;
        while(finishedPlayers != playerCount-1){
            
            startTurn();
            endTurn();          //finishing logic 추가
        }
    }

    //한명의 턴을 진행
    void startTurn() {
        while (permittedMoves.size() != 0 || permittedThrows !=0 ){
            while(permittedThrows != 0){
                //deterministic YUT throw
                if (ui.throwing() == true) {
                    int moveCount = ui.choiceYut();
                    permittedMoves.add(moveCount);
                    arr[moveCount]++;
                    if (moveCount == 3 || moveCount == 4) permittedThrows += 1;
                }
                //random YUT throw
                else{
                    int moveCount =(int)(Math.random() * 6);
                    permittedMoves.add(moveCount);
                    arr[moveCount]++;
                    if (moveCount ==3 || moveCount == 4) permittedThrows += 1;
                }
                permittedThrows--;
                ui.yutStateUpdate(arr);
            }  
            
            //move piece as given amount
            //말 움직일떄 stack되어있는 말도 전부 움직이게 바꿔야함
            int piece_id = ui.choicePiece(currentPlayer);
            Piece piece = players[currentPlayer].getPiece(piece_id);
            ArrayList<Piece> pieces = piece.getStackedPieces();

            int chosen_yut;
            while(!permittedMoves.contains(chosen_yut = ui.choiceYut())){}
            permittedMoves.remove(Integer.valueOf(chosen_yut));
            arr[chosen_yut]--;
            ui.yutStateUpdate(arr);
            Node arrived_node = board.followPath(pieces, chosen_yut);

            

            
            boolean catchedOrGrouped = false;
            for (Player player : players){
                for (Piece destPiece : player.getPieces()){
                    if (destPiece.getPosition() == arrived_node ){     
                        // grouping - 비교하는 연산자가 맞음?   
                        if ( player == players[currentPlayer]){          
                            {
                                for (Piece sourcePiece: pieces){
                                    sourcePiece.setPosition(arrived_node);
                                    sourcePiece.copyFrom(destPiece);
                                    
                                }
                            }

                        }
                        // catching
                        else{                       
                            piece.setPosition(arrived_node);
                            if (piece.getStatus() == Piece.State.WAITING){
                                piece.setStatus(Piece.State.ON_BOARD);
                            }
                            player.catchPiece(destPiece,piece);
                            permittedThrows += 1;
                        }
                        catchedOrGrouped = true;
                        ui.mapUpdate(players);
                        break;
                    }
                }
            }
            
            if (!catchedOrGrouped && piece.getStatus() != Piece.State.FINISHED) {
                piece.setPosition(arrived_node);
                if (piece.getStatus() == Piece.State.WAITING){
                    piece.setStatus(Piece.State.ON_BOARD);
                }
                if (piece.getStatus() == Piece.State.FINISHED) {
                    for (Piece lpiece : pieces) {
                        lpiece.setPosition(null);
                        lpiece.setStatus(Piece.State.FINISHED);
                    }
                }
                
            }
            ui.mapUpdate(players);
            
        }
    }

    private void endTurn() { 
        currentPlayer = (currentPlayer + 1) % players.length; 
        permittedThrows = 1;
        arr = new int[6];
        ui.yutStateUpdate(arr);
        permittedMoves = new ArrayList<>();
        ui.turnChange(currentPlayer);
    }
}



