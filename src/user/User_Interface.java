package user;

import java.util.ArrayList;
import map.Map;

public interface User_Interface {
	
	
	public abstract ArrayList<Horse> getHorses(); //���� ���� ����Ʈ�� ��ȯ
	
	public abstract int moveHorse(Horse horse, int count, int mapStyle); //������ ���� count��ŭ �����̱� ��ȯ��: ������ index
	
	
	
}
