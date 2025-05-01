package view;

public interface view {

	 public void gamesetup(int map, int player, int token){
		    
	  }

	  //말이동으로 어떤말이 어떻게 움직였는지만 받아오면 가능
	  public void mapupdate(){
	    
	  }

	  //return 0 means random button click, return 1 means select button click
	  public int throwing(){
	    
	  }

	  //return 0~5 빽도 도 개 걸 윷 모
	  public int choice_yut(){
	    
	  }

	  // return chosen horse number    start from 1
	  public int choice_horse(){
	    
	  }

	  // if restart button pressed return 1; end button return 0
	  public int end(){
	    
	  }
}
