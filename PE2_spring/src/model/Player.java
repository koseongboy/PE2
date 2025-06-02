package model;
import view.*;

import java.util.ArrayList;


public class Player {
	private int id; 					//todo : get id 
	private ArrayList<Piece> pieces;
	private View ui;
	private Board board;
	
	@Override
	public String toString(){
		return String.valueOf(id);
	}

	private class Yut{
		public static int throwYut(){
			int result =(int)(Math.random() * 6);		//0~5 : 0 이 도 / 4가 모 5가 백도
			return result;
		}
	}

	public Player(int id, int pieceCount,View ui,Board board) {		//todo : what GRASP?
		pieces = new ArrayList<Piece>();
		for (int i = 0; i < pieceCount; i++) {
			Piece piece = new Piece(this);
			this.pieces.add(piece);
		}
		this.ui = ui;
		this.id = id;
		this.board = board;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	private static boolean allZero(int[] array) {
		for (int value : array) {
			if (value != 0) {
				return false;
			}
		}
    return true;
	}


	public boolean playTurn(){
		int permittedThrows = 1;       
		//ArrayList<Integer> permittedMoves = new ArrayList<>(); 
		int[] permittedMoves = new int[6];
		ui.yutStateUpdate(permittedMoves);
		
		//한명의 턴을 진행
        while (!allZero(permittedMoves)|| permittedThrows !=0 ){
            while(permittedThrows != 0){
                //deterministic YUT throw
                if (ui.throwing() == true) {
                    int yut = ui.choiceYut();
					permittedMoves[yut]++;
                    if (yut == 3 || yut == 4) permittedThrows += 1;
                }
                //random YUT throw
                else{
					int yut = Yut.throwYut();
					permittedMoves[yut]++;
                    if (yut ==3 || yut == 4) permittedThrows += 1;
                }
                permittedThrows--;
                ui.yutStateUpdate(permittedMoves);
            }  
            
            //move piece as given amount
            //말 움직일떄 stack되어있는 말도 전부 움직이게 바꿔야함
            int piece_id = ui.choicePiece(id);					//todo : why is this the one that asks the view? cause he is the one calling the view to update
            
			ArrayList<Piece> stackedPieces = this.pieces.get(piece_id).getStackedPieces();
            int chosen_yut;

			
            while(permittedMoves[chosen_yut = ui.choiceYut()] == 0){}
            permittedMoves[chosen_yut]--;
            ui.yutStateUpdate(permittedMoves);
            
			if (board.followPath(stackedPieces, chosen_yut)){
                permittedThrows++;
            }
			ui.mapUpdate();

            

            //test for finish
            {
                int j = 0;
                for (Piece wpiece : stackedPieces){
                    if (wpiece.getStatus() == Piece.State.FINISHED){
                        j++;
                    }
                }
                if (j == stackedPieces.size()){
                    return true;
                }
            }              
            
        }
        return false;
	}
}