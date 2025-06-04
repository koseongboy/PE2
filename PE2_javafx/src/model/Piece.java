
package model;

import java.util.ArrayList;

public class Piece {
	private final Player owner;
	private Node pos = null;			//todo : should piece have its position?
	public ArrayList<Node> path = new ArrayList<>();

	public enum State {
		WAITING,
		ON_BOARD,
		OVERLAPPED,
		FINISHED;

		@Override
		public String toString() {
			return name();
		}
	}
	
	//직전 노드가 2개가 있음....(대각선으로 오는 방향도 생각)
	boolean aboutToFinish = false;
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Piece {\n");
		sb.append("  owner = ").append(owner).append(",\n");
		sb.append("  pos = ").append(pos).append(",\n");
		sb.append("  path = ").append(path).append(",\n");
		sb.append("  aboutToFinish = ").append(aboutToFinish).append(",\n");
		sb.append("  status = ").append(status).append(",\n");
		sb.append("  count = ").append(count).append("\n");
		sb.append("}");
		return sb.toString();
	}
	private State status = State.WAITING;
	//private int position = -1;
	private int count = 1;
	
	Piece(Player owner) { this.owner = owner; }

	public State getStatus() {
		return this.status;
	}

	public Player getOwner() { 
		return owner; 
	}
	
	public Node getPosition() {
		return pos;
	}

	public void setPosition(Node n){
		 pos = n;      
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int value) {
		this.count = value;
	}
	
	public void setStatus(State status) {
		this.status = status;
	}
	
	public ArrayList<Piece> getStackedPieces(){
		ArrayList<Piece> result = new ArrayList<>();
		ArrayList<Piece> pieces = owner.getPieces();
		result.add(this);
		for (Piece piece : pieces){
			if (piece != this && piece.getStatus() == Piece.State.OVERLAPPED && piece.getPosition() == this.getPosition()){
				result.add(piece);
			}
		}
		return result;
	}

	
}
