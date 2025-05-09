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
	
	
	public void groupPieces(Piece from, Piece to) {	//from 말을 to 말로 업음.
		try {
			if(from.getPosition() == to.getPosition()) {	//두 말의 위치가 같아야 업을 수 있음. 그럴 경우 두 말의 개수를 더하고 from 말의 개수를 0, 상태를 업힘으로 전환
				to.setCount(to.getCount() + from.getCount());
				from.setCount(0);
				from.setPosition(null);
				from.setStatus(Piece.State.OVERLAPPED);
				from.aboutToFinish = to.aboutToFinish;
			} else {	//두 말의 위치가 다를 경우 에러
				throw new RuntimeException();
			}
		} catch(Exception e) {
			System.out.println("위치가 다른 말을 업으려 하고 있음.");
		}
	}
	
	public void catchPiece(Piece from, Piece to) {	//from 말을 to 말이 잡음
		try {
			if(from.getPosition() == to.getPosition()) {	//위치가 같아야만 잡음
				from.setStatus(Piece.State.WAITING);				//잡힌 말의 상태, 위치를 초기화. 잡힌 말이 업혀있을 경우를 생각해 잡힌 말의 개수가 1이 될 때 까지 overlapped된 말들을 하나 씩 waiting으로 바꿈
				from.aboutToFinish = false;
				from.setPosition(null);
				if (from.getCount() > 1) {
					int currentCount;
					for(Piece h : pieces) {
						if(h.getStatus() == Piece.State.OVERLAPPED) {
							h.setStatus(Piece.State.WAITING);
							h.setCount(1);
							h.setPosition(null);
							currentCount = from.getCount();
							from.setCount(currentCount - 1);
							if(from.getCount() == 1) break;
						}
					}
				}
			} else {
				throw new RuntimeException();
			}
		} catch(Exception e) {
			System.out.println("위치가 다른 말을 잡으려 함");
		}
	}
	
	

	

}