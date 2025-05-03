package user;

import java.util.ArrayList;

public class User implements User_Interface {
	private ArrayList<Horse> horses;
	private int teamIndex;
	
	public User(int num_of_horses, int team_index) {
		this.teamIndex = team_index;
		for (int i = 0; i < num_of_horses; i++) {
			Horse horse = new Horse(team_index);
			this.horses.add(horse);
		}
	}
	@Override
	public ArrayList<Horse> getHorses() {
		return horses;
	}
	@Override
	public int moveHorse(Horse horse, int count) {
		int startPoint;
		if (horse.getStatus() == Horse.WAITING) {
			startPoint = 0;
		} else if (horse.getStatus() == Horse.ON_MAP) {
			startPoint = horse.getLocation();
		} else {
			System.out.println("�����߰ų� �����ִ� ���� �̵���Ű�� �ϰ� ����");
			throw new RuntimeException();
		}
		int dest = 0; //TO-DO 0��� ��� �Լ� �־���� 
		return dest;
		
	}
}
