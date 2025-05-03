package user;

import java.util.ArrayList;

public class User implements User_Interface {
	private ArrayList<Horse> horses;
	
	public User(int num_of_horses) {
		for (int i = 0; i < num_of_horses; i++) {
			Horse horse = new Horse();
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
			System.out.println("도착했거나 업혀있는 말을 이동시키려 하고 있음");
			throw new RuntimeException();
		}
		int dest = 0; //TO-DO 0대신 계산 함수 넣어야함 
		return dest;
		
	}
}
