package user;

import java.util.ArrayList;

public interface User_Interface {
	
	
	public abstract ArrayList<Horse> getHorses(); //팀의 말들 리스트로 반환
	
	public abstract boolean moveHorse(Horse horse, int count); //선택한 말을 count만큼 움직이기 반환값: 잡혔는 지 여부 반환 (한 번 더 판별용)
	
	
	
}
