package user;

import java.util.ArrayList;
import map.Map;

public interface User_Interface {
	
	
	public abstract ArrayList<Horse> getHorses(); //팀의 말들 리스트로 반환
	
	public abstract int moveHorse(Horse horse, int count, int mapStyle); //선택한 말을 count만큼 움직이기 반환값: 도착지 index
	
	
	
}
