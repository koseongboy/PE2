package user;

import java.util.ArrayList;
import map.Board;


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
	
	
	public void groupHorse(Horse from, Horse to) {	//from ���� to ���� ����.
		try {
			if(from.getLocation() == to.getLocation()) {	//�� ���� ��ġ�� ���ƾ� ���� �� ����. �׷� ��� �� ���� ������ ���ϰ� from ���� ������ 0, ���¸� �������� ��ȯ
				to.setCount(to.getCount() + from.getCount());
				from.setCount(0);
				from.setLocation(-1);
				from.setStatus(Horse.OVERLAPPED);
			} else {	//�� ���� ��ġ�� �ٸ� ��� ����
				throw new RuntimeException();
			}
		} catch(Exception e) {
			System.out.println("��ġ�� �ٸ� ���� ������ �ϰ� ����.");
		}
		
	}
	
	public void catchHorse(Horse from, Horse to) {	//from ���� to ���� ����
		try {
			if(from.getLocation() == to.getLocation()) {	//��ġ�� ���ƾ߸� ����
				to.setStatus(Horse.WAITING);				//���� ���� ����, ��ġ�� �ʱ�ȭ. ���� ���� �������� ��츦 ������ ���� ���� ������ 1�� �� �� ���� overlapped�� ������ �ϳ� �� waiting���� �ٲ�
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
			System.out.println("��ġ�� �ٸ� ���� ������ ��");
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
	public int moveHorse(Horse horse, int count, int mapStyle) { //������ ��, ĭ��, �� ���¸� �޾Ƽ� �̵���ų ���� ���� ���� index ����
		int startPoint = 0;  //�̵� �� ���� ��ġ
		try {
			if (horse.getStatus() == Horse.WAITING) { //���� ���� ��� ������ ��� (������ ������ ���� ���) ���� �ε����� 0���� ����� ���� ���¸� ON_MAP���� ����
				startPoint = 0;
				horse.setStatus(Horse.ON_MAP);
			} else if (horse.getStatus() == Horse.ON_MAP) { //���� �̹� �ʿ� �ִ� ��� ���� ��ġ�� ���� �ε���
				startPoint = horse.getLocation();
			} else { //���� �̹� �����ְų� ������ ���� ��� ����
				throw new RuntimeException();
			}
		} catch(Exception e) {
			System.out.println("�����߰ų� �����ִ� ���� �̵���Ű�� �ϰ� ����");
		}
		int dest;
		if(count == -1) { //TO-DO 0��� ��� �Լ� �־����, �鵵 �����ؾ��� = Horse�� prevIdx �̿�
			dest = Board.followPath(startPoint, count, horse.getPrevIdx(), mapStyle); //�鵵�Լ�
		} else {
			dest = Board.followPath(startPoint, count, mapStyle); //�Ϲ� �Լ�
		}
		horse.setPrevIdx(horse.getLocation());
		
		
		
		return dest;
		
	}
	

}
