package view;
import user.User;

public interface View {

	  //게임 시작 및 맵, 플레이어, 말 수 반환 type int[map, player, piece] map은 4 5 6 (사각형, 오각형, 육각형)
	  public int[] gameSetup();

	  //piece(0~4)가 map_index 에 도착(만약 먹혔으면 -1, 도착했으면 100)
	  public void mapUpdate(User[] user);

	  //return 0 means random button click, return 1 means select button click
	  public int throwing();

	  //return 0~5 빽도 도 개 걸 윷 모
	  public int choiceYut();
	  // return chosen horse number    start from 0
	  public int choiceHorse(int turn);
	  //윷 던진 결과를 UI에 보여주는 메서드: 인수로 윷 던진 결과의 횟수 배열 필요(index 0-> 도, 1-> 개, 2->걸, 3-> 윷, 4->모: 각 인덱스에 이동 횟수 저장)
	  public void yutStateUpdate(int[] state_arr);

	  // if restart button pressed return 1; end button return 0
	  public int end(int winner);
	  //player 0~3
	  public void turnChange(int turn);
	  
}
