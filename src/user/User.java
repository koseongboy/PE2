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
	
	
	public void groupHorse(Horse from, Horse to) {	//from 말을 to 말로 업음.
		try {
			if(from.getLocation() == to.getLocation()) {	//두 말의 위치가 같아야 업을 수 있음. 그럴 경우 두 말의 개수를 더하고 from 말의 개수를 0, 상태를 업힘으로 전환
				to.setCount(to.getCount() + from.getCount());
				from.setCount(0);
				from.setLocation(-1);
				from.setStatus(Horse.OVERLAPPED);
			} else {	//두 말의 위치가 다를 경우 에러
				throw new RuntimeException();
			}
		} catch(Exception e) {
			System.out.println("위치가 다른 말을 업으려 하고 있음.");
		}
		
	}
	
	public void catchHorse(Horse from, Horse to) {	//from 말을 to 말이 잡음
		try {
			if(from.getLocation() == to.getLocation()) {	//위치가 같아야만 잡음
				to.setStatus(Horse.WAITING);				//잡힌 말의 상태, 위치를 초기화. 잡힌 말이 업혀있을 경우를 생각해 잡힌 말의 개수가 1이 될 때 까지 overlapped된 말들을 하나 씩 waiting으로 바꿈
				to.setLocation(-1);
				if (to.getCount() > 1) {
					int currentCount;
					for(Horse h : horses) {
						if(h.getStatus() == Horse.OVERLAPPED) {
							h.setStatus(Horse.WAITING);
							h.setCount(1);
							h.setLocation(-1);
							currentCount = to.getCount();
							to.setCount(currentCount - 1);
							if(to.getCount() == 1) break;
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
	public boolean checkHorseArrived(Horse horse) {		
		if(horse.getLocation() == -2) {
			horse.setStatus(Horse.ARRIVED);
			if (horse.getCount() > 1) {
				int currentCount;
				for(Horse h : horses) {
					if(h.getStatus() == Horse.OVERLAPPED) {
						h.setStatus(Horse.ARRIVED);
						h.setCount(1);
						h.setLocation(-2);
						currentCount = horse.getCount();
						horse.setCount(currentCount - 1);
						if(horse.getCount() == 1) break;
					}
				}
			}
		return true;
		} else return false;
	}
	
	@Override
	public int moveHorse(Horse horse, int count, int mapStyle) { //움직일 말, 칸수, 맵 형태를 받아서 이동시킬 말의 도착 지점 index 리턴
		int startPoint;  //이동 전 말의 위치
		try {
			if (horse.getStatus() == Horse.WAITING) { //말이 아직 대기 상태인 경우 (맵으로 나가지 않은 경우) 시작 인덱스를 0으로 만들고 말의 상태를 ON_MAP으로 변경
				startPoint = 0;
				horse.setStatus(Horse.ON_MAP);
			} else if (horse.getStatus() == Horse.ON_MAP) { //말이 이미 맵에 있는 경우 말의 위치가 시작 인덱스
				startPoint = horse.getLocation();
			} else { //말이 이미 업혀있거나 도착한 말일 경우 에러
				throw new RuntimeException();
			}
		} catch(Exception e) {
			System.out.println("도착했거나 업혀있는 말을 이동시키려 하고 있음");
		}
		int dest = mapStyle; //TO-DO 0대신 계산 함수 넣어야함 		
		
		return dest;
		
	}
	

}
