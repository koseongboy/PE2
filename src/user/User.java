package user;

import java.util.ArrayList;

public class User implements User_Interface {
	private ArrayList<Horse> horses;
	
	
	public User(int num_of_horses) {
		for (int i = 0; i < num_of_horses; i++) {
			Horse horse = new Horse();
			this.horses.add(horse)
		}
	}
	@Override
	public ArrayList<Horse> getHorses() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
		public boolean moveHorse(Horse horse, int count) {
			// TODO Auto-generated method stub
			return false;
		}
}
