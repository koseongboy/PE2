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
	
	
	public void groupHorse(Horse from, Horse to) {	//from 留먯쓣 to 留먮줈 �뾽�쓬.
		try {
			if(from.getLocation() == to.getLocation()) {	//�몢 留먯쓽 �쐞移섍� 媛숈븘�빞 �뾽�쓣 �닔 �엳�쓬. 洹몃윺 寃쎌슦 �몢 留먯쓽 媛쒖닔瑜� �뜑�븯怨� from 留먯쓽 媛쒖닔瑜� 0, �긽�깭瑜� �뾽�옒�쑝濡� �쟾�솚
				to.setCount(to.getCount() + from.getCount());
				from.setCount(0);
				from.setLocation(-1);
				from.setStatus(Horse.OVERLAPPED);
			} else {	//�몢 留먯쓽 �쐞移섍� �떎瑜� 寃쎌슦 �뿉�윭
				throw new RuntimeException();
			}
		} catch(Exception e) {
			System.out.println("�쐞移섍� �떎瑜� 留먯쓣 �뾽�쑝�젮 �븯怨� �엳�쓬.");
		}
		
	}
	
	public void catchHorse(Horse from, Horse to) {	//from 留먯쓣 to 留먯씠 �옟�쓬
		try {
			if(from.getLocation() == to.getLocation()) {	//�쐞移섍� 媛숈븘�빞留� �옟�쓬
				to.setStatus(Horse.WAITING);				//�옟�엺 留먯쓽 �긽�깭, �쐞移섎�� 珥덇린�솕. �옟�엺 留먯씠 �뾽���엳�쓣 寃쎌슦瑜� �깮媛곹빐 �옟�엺 留먯쓽 媛쒖닔媛� 1�씠 �맆 �븣 源뚯� overlapped�맂 留먮뱾�쓣 �븯�굹 �뵫 waiting�쑝濡� 諛붽퓞
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
			System.out.println("�쐞移섍� �떎瑜� 留먯쓣 �옟�쑝�젮 �븿");
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
	

	

}
