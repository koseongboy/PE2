package view;

public interface view {

	  //게임 시작 및 맵, 플레이어, 말 수 반환 type int[map, player, piece] map은 4 5 6 (사각형, 오각형, 육각형)
	  public int[] gamesetup();

	  //piece(0~4)가 map_index 에 도착(만약 먹혔으면 -1, 도착했으면 100)
	  public void mapupdate(int piece, int map_index);

	  //return 0 means random button click, return 1 means select button click
	  public int throwing();

	  //return 0~5 빽도 도 개 걸 윷 모
	  public int choice_yut();
	  // return chosen horse number    start from 0
	  public int choice_horse();

	  // if restart button pressed return 1; end button return 0
	  public int end();
	  //player 0~3
	  public void turnchange(int turn);
	  
}
