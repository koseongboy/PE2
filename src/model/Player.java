package model;

import java.util.ArrayList;


public class Player {
	private ArrayList<Piece> pieces;
	
	public Player(int num_of_horses) {
		pieces = new ArrayList<Piece>();
		for (int i = 0; i < num_of_horses; i++) {
			Piece piece = new Piece(this);
			this.pieces.add(piece);
		}
	}
	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public Piece getPiece(int index) { 
		return pieces.get(index);
	}
	

}