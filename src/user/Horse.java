package user;

public class Horse {
	public static final int WAITING = 0;		//���
	public static final int ON_MAP = 1;			//��忡 ��������
	public static final int OVERLAPPED = 2;		//��������
	public static final int ARRIVED = 3;		//������
	
	private int status = WAITING;
	private int location = -1;
	private int count = 1;
	
	
	public int getStatus() {
		return status;
	}
	
	public int getLocation() {
		return location;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int value) {
		this.count = value;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setLocation(int location) {
		this.location = location;
	}
	
}
