//grouping 된 애들이 들어왔을때 제대로 동작하는지 체크 - setPosition 과 setStatus 가 동시에 일어나는지 체크해야함
package manager;
import view.*;
import model.*;

//import java.security.cert.PKIXReason;
import java.util.ArrayList;


//change users into players


public class Manager{
    private View ui = new UI();
    private Board board;    
    private int currentPlayer = 0;  //todo : change this?
    private ArrayList<Player> players = new ArrayList<>();
    
    
    public static void main(String[] args) {

        int replay = 1;
        while (replay == 1){
            Manager manager = new Manager();  
            replay = manager.runGame();    
        }            
    }

    public int runGame(){
        

        //setup map,users,pieces
        int[] gameState = ui.gameSetup();
        int mapType = gameState[0];     //setup board
        
        int playerCount = gameState[1]; //setup 
        int pieceCount = gameState[2];

        board = new Board(mapType);         //todo : change the initializer to not have players

        for (int i =0 ;i<playerCount ; i++){
            players.add(new Player(i,pieceCount,ui,board));
        }
        ui.addPlayers(players.toArray(new Player[0]));
        

        int replay;
        //game start : turn starts from 0Z        
        while(true){
            boolean finished = players.get(currentPlayer).playTurn();        //todo 
            if (finished){
                replay = ui.end(currentPlayer);
                break;
            };
            endTurn();          //finishing logic 추가
        }
        return replay;      //todo : make this logic to follow some more clean?
        
    }

    // //한명의 턴을 진행
    // int startTurn() {
    //     while (permittedMoves.size() != 0 || permittedThrows !=0 ){
    //         while(permittedThrows != 0){
    //             //deterministic YUT throw
    //             if (ui.throwing() == true) {
    //                 int moveCount = ui.choiceYut();
    //                 permittedMoves.add(moveCount);
    //                 arr[moveCount]++;
    //                 if (moveCount == 3 || moveCount == 4) permittedThrows += 1;
    //             }
    //             //random YUT throw
    //             else{
    //                 int moveCount =(int)(Math.random() * 6);
    //                 permittedMoves.add(moveCount);
    //                 arr[moveCount]++;
    //                 if (moveCount ==3 || moveCount == 4) permittedThrows += 1;
    //             }
    //             permittedThrows--;
    //             ui.yutStateUpdate(arr);
    //         }  
            
    //         //move piece as given amount
    //         //말 움직일떄 stack되어있는 말도 전부 움직이게 바꿔야함
    //         int piece_id = ui.choicePiece(currentPlayer);
    //         ArrayList<Piece> pieces = board.players.get(currentPlayer).getPiece(piece_id).getStackedPieces();

    //         int chosen_yut;
    //         while(!permittedMoves.contains(chosen_yut = ui.choiceYut())){}
    //         permittedMoves.remove(Integer.valueOf(chosen_yut));
    //         arr[chosen_yut]--;
    //         ui.yutStateUpdate(arr);
    //         if (board.followPath(pieces, chosen_yut)){
    //             permittedThrows++;
    //         }
    //         ui.mapUpdate(board.players.toArray(new Player[0]));

    //         //test for finish
    //         {
    //             int j = 0;
    //             for (Piece wpiece : board.players.get(currentPlayer).getPieces()){
    //                 if (wpiece.getStatus() == Piece.State.FINISHED){
    //                     j++;
    //                 }
    //             }
    //             if (j == board.players.get(currentPlayer).getPieces().size()){
    //                 return board.players.indexOf(board.players.get(currentPlayer));
    //             }
    //         }              
            
    //     }
    //     return -1;
    // }

    private void endTurn() { 
        currentPlayer = (currentPlayer + 1) % this.players.size(); 
        ui.turnChange(currentPlayer);
    }
}



