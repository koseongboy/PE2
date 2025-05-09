package manager;
import view.*;
import model.*;

import java.util.ArrayList;


//change users into players


public class Manager{
    private View ui = new UI();
    private Board board;    
    private int currentPlayer = 0;
    private Player[] players;
    private int permittedThrows = 1;
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
                    if (moveCount == 3 || moveCount == 4) permittedThrows += 1;
                }
                //random YUT throw
                else{
                    int moveCount =(int)(Math.random() * 6);
                    permittedMoves.add(moveCount);
                    if (moveCount ==3 || moveCount == 4) permittedThrows += 1;
                }
                permittedThrows--;
            }  
            
            //move piece as given amount
            //말 움직일떄 stack되어있는 말도 전부 움직이게 바꿔야함
            int piece_id = ui.choicePiece(currentPlayer);
            Piece piece = players[currentPlayer].getPiece(piece_id);
            ArrayList<Piece> pieces = piece.getStackedPieces();

            int chosen_yut;
            while(!permittedMoves.contains(chosen_yut = ui.choiceYut())){}

            Node arrived_node = board.followPath(pieces, chosen_yut);
            
            boolean catchedOrGrouped = false;
            for (Player player : players){
                for (Piece originalPiece : player.getPieces()){
                    if (originalPiece.getPosition() == arrived_node && (originalPiece != piece) ){     
                        // grouping - 비교하는 연산자가 맞음?   
                        if ( player == players[currentPlayer]){          
                            for (Piece lpiece:pieces){lpiece.setPosition(arrived_node);}
                            player.groupPieces(originalPiece,piece);
                            catchedOrGrouped = true;
                            ui.mapUpdate(players);
                            break;
                        }
                        // catching
                        else{                       
                            piece.setPosition(arrived_node);
                            player.catchPiece(originalPiece,piece);
                            permittedThrows += 1;
                            catchedOrGrouped = true;
                            ui.mapUpdate(players);
                            break;
                        }
                    }
                }
            }
            
            if (!catchedOrGrouped) {
                piece.setPosition(arrived_node);
                if (piece.getStatus() == Piece.State.FINISHED) {
                    for (Piece lpiece : pieces) {
                        lpiece.setPosition(null);
                    }
                }
                ui.mapUpdate(players);
            }
            permittedMoves.remove(Integer.valueOf(chosen_yut));
        }
    }

    private void endTurn() { 
        currentPlayer = (currentPlayer + 1) % players.length; 
        permittedThrows = 1;
        permittedMoves = new ArrayList<>();
        ui.turnChange(currentPlayer);
    }
}



