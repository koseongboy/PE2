package user;

import java.util.ArrayList;

public interface User_Interface {
	
	
	public abstract ArrayList<Horse> getHorses(); //���� ���� ����Ʈ�� ��ȯ
	
	public abstract boolean moveHorse(Horse horse, int count); //������ ���� count��ŭ �����̱� ��ȯ��: ������ �� ���� ��ȯ (�� �� �� �Ǻ���)
	
	
	
}
