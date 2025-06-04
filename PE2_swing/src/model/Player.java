package model;
import view.*;

import java.util.ArrayList;


public class Player {
	private int id; 					//todo : get id 
	public ArrayList<Piece> pieces;
	public int permittedThrows = 1;
	public int[] permittedMoves = new int[6];
	public UI ui;  
	
	@Override
	public String toString(){
		return String.valueOf(id);
	}



	public Player(int id, int pieceCount,UI ui) {		//todo : what GRASP?
		pieces = new ArrayList<Piece>();
		for (int i = 0; i < pieceCount; i++) {
			Piece piece = new Piece(this);
			this.pieces.add(piece);
		}
		this.id = id;
		this.ui = ui;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public void clearPermittedMoves(){
		permittedMoves = new int[6];
		try {
			ui.yutStateUpdate(permittedMoves);
		} catch(NullPointerException e) {
			
		}
	}

	public void thrownYut(int yut){
		permittedMoves[yut]++;
		if (yut == 3 || yut == 4) permittedThrows += 1;
		permittedThrows--;
		try {
			ui.yutStateUpdate(permittedMoves);
		} catch(NullPointerException e) {

		}
		
	}


	public boolean movePiece(int piece_id, int chosen_yut,Board board){
		ArrayList<Piece> stackedPieces = pieces.get(piece_id).getStackedPieces();
		permittedMoves[chosen_yut]--;
		try {
			ui.yutStateUpdate(permittedMoves);
		} catch (NullPointerException e) {

		}
		

		if (board.followPath(stackedPieces, chosen_yut)){
			permittedThrows++;
		}
		try {
			ui.mapUpdate();
		} catch (NullPointerException e) {

		}
		

		for (Piece piece : pieces) {
			if (piece.getStatus() != Piece.State.FINISHED){
				return false;
			}
		}
		return true;
	}

	public void clearPermittedThrows(){
		permittedThrows = 1;
	}




	// public boolean playTurn(){
	// 	int permittedThrows = 1;       
	// 	//ArrayList<Integer> permittedMoves = new ArrayList<>(); 
	// 	int[] permittedMoves = new int[6];
	// 	ui.yutStateUpdate(permittedMoves);
		
	// 	//한명의 턴을 진행
    //     while (!allZero(permittedMoves)|| permittedThrows !=0 ){
    //         while(permittedThrows != 0){
    //             //deterministic YUT throw
    //             if (ui.throwing() == true) {
    //                 int yut = ui.choiceYut();
	// 				permittedMoves[yut]++;
    //                 if (yut == 3 || yut == 4) permittedThrows += 1;
    //             }
    //             //random YUT throw
    //             else{
	// 				int yut = Yut.throwYut();
	// 				permittedMoves[yut]++;
    //                 if (yut ==3 || yut == 4) permittedThrows += 1;
    //             }
    //             permittedThrows--;
    //             ui.yutStateUpdate(permittedMoves);
    //         }  
            
    //         //move piece as given amount
    //         //말 움직일떄 stack되어있는 말도 전부 움직이게 바꿔야함
    //         int piece_id = ui.choicePiece(id);					//todo : why is this the one that asks the view? cause he is the one calling the view to update
            
	// 		ArrayList<Piece> stackedPieces = this.pieces.get(piece_id).getStackedPieces();
    //         int chosen_yut;

			
    //         while(permittedMoves[chosen_yut = ui.choiceYut()] == 0){}
    //         permittedMoves[chosen_yut]--;
    //         ui.yutStateUpdate(permittedMoves);
            
	// 		if (board.followPath(stackedPieces, chosen_yut)){
    //             permittedThrows++;
    //         }
	// 		ui.mapUpdate();

            

    //         //test for finish
    //         {
    //             int j = 0;
    //             for (Piece wpiece : pieces){
    //                 if (wpiece.getStatus() == Piece.State.FINISHED){
    //                     j++;
    //                 }
    //             }
    //             if (j == pieces.size()){
    //                 return true;
    //             }
    //         }              
            
    //     }
    //     return false;
	// }
}