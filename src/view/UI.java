package view;

import javax.swing.*;          // JFrame, JButton, JOptionPane...

import java.awt.*;             // BorderLayout, FlowLayout …
import java.awt.event.*;       // ActionListener …
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import user.User;
import user.Horse;


public class UI implements View{
	
	JFrame frame;		//메인 프레임
	JPanel throwing;	//윷 던지기 패널
	JPanel yut_state;	//윷 던진 결과 패널
	
	JPanel[] player;	//플레이어 패널 저장 배열
	JLabel[] turn_label;//플레이어 패널 영역에 있는 "your turn" 라벨 배열
	JPanel[][] piece;	//플레이어 수 X 말의 수 shape의 말 패널 배열
	JPanel[] map_node;		//맵의 노드 패널 배열
	
	//////////////////////////////////////////////////////////////////////////////////추가
	JPanel[] yut_state_image;//오른쪽 위의 윷 이미지
	JLabel[] yut_state_text;//오른쪽 위 윷 텍스트
	//////////////////////////////////////////////////////////////////////////////////추가
	
	private Image boardImg;		//윷 판 이미지->gamesetup 메서드에서만 사용
	
	//게임 시작 및 맵, 플레이어, 말 수 반환 type int[map, player, piece] map은 4 5 6 (사각형, 오각형, 육각형)
	public int[] gamesetup(){
		
		/*프로그램 시작 시 윷놀이를 설정하기 위한 시작 폼*/
		JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));

		//플레이어와 말 수 선택은 콤보 박스로 구현
		JComboBox<Integer> cbPlayer = new JComboBox<>(new Integer[]{2, 3, 4});
		JComboBox<Integer> cbToken = new JComboBox<>(new Integer[]{2, 3, 4, 5});
		
		//맵 형태 선택은 체크 박스로 구현
		JRadioButton map_4 = new JRadioButton("사각형 맵");
		map_4.setActionCommand("4");
        JRadioButton map_5 = new JRadioButton("오각형 맵");
        map_5.setActionCommand("5");
        JRadioButton map_6 = new JRadioButton("육각형 맵");
        map_6.setActionCommand("6");
        
        ButtonGroup map_group = new ButtonGroup();
        map_group.add(map_4);
        map_group.add(map_5);
        map_group.add(map_6);
        
        panel.add(new JLabel("맵 (사각형/오각형/육각형)"));
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        radioPanel.add(map_4);
        radioPanel.add(map_5);
        radioPanel.add(map_6);
        panel.add(radioPanel);
        
        //맵 형태 체크박스의 기본값은 사각형으로 설정
        map_4.setSelected(true);
        
        panel.add(new JLabel("플레이어 수"));
        panel.add(cbPlayer);
        panel.add(new JLabel("말 개수"));
        panel.add(cbToken);

        //폼의 확인, 취소/닫기 옵션 구현
        int option = JOptionPane.showConfirmDialog(
                null,                     // 부모 컴포넌트 (null = 화면 중앙)
                panel,                    // 내용 패널
                "게임 설정",              // 타이틀
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        
        //유저 선택 저장 변수(맵 형태, 플레이어 수, 말 수)
        int map_type, player_count, piece_count;
        
        if (option == JOptionPane.OK_OPTION) {
        	//확인 버큰 클릭 시 유저 선택 결과 저장
        	ButtonModel sel = map_group.getSelection();
        	map_type = Integer.parseInt(sel.getActionCommand());
            player_count=(Integer) cbPlayer.getSelectedItem();
            piece_count=(Integer) cbToken.getSelectedItem();
        } else {
            return null;   // 사용자가 취소/닫기를 눌렀을 때
        }
        
        /*윷놀이 UI 구현*/
        //메인 프레임 초기화
        frame = new JFrame();
        frame.setBounds(100, 100, 1640, 982);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//맵 부분 패널 초기화
		JPanel map = new JPanel();
		map.setBackground(SystemColor.desktop);
		map.setBounds(269, 10, 930, 930);
		frame.getContentPane().add(map);
		map.setLayout(null);
		
		//map_type에 따른 switch 문에서 변경될 맵의 노드 수와 위치 및 크기 정보
		//사각형 맵으로 변수 초기화 
		int[][] node_location={{758, 756,145},{795, 625, 71},{795, 495,71},{795, 364,71},{795, 230,71},
				   { 759, 28,145},{628,66,71},{499,66,71},{365,66,71},{232,66,71},
				   {30,27,145},{67,229,71},{67,363,71},{67,495,71},{67,628,71},
				   {31, 755, 145},{233,793,71},{366,793,71},{498,793,71},{630,793,71},
				   {663,661,71},{565,561,71},{394,392,145},{662,198,71},{562,297,71},
				   {201,197,71},{299,297,71},{199,658,71},{300,561,71}};;
		int map_len=29;
		
		//맵의 형태에 따른 맵 이미지 및 변수 데이터 설정
        switch(map_type) {
        case 4:
        	boardImg = new ImageIcon(UI.class.getResource("/images/사각형_윷놀이판.jpg")).getImage();
        	break;
        case 5:
        	//오각형 맵의 경우 노드 위치 재설정
        	boardImg = new ImageIcon(UI.class.getResource("/images/오각형_윷놀이판.png")).getImage();
        	int[][] new_location= {{642,743,92},{693, 684, 52},{718, 608, 52},{743, 531, 52},{768, 456, 52},
        						   {767,336,92},{719,290,52},{656,241,52},{591,192,52},{528,142,52},
        						   {419,71,92},{351,142,52},{286,191,52},{222,240,52},{159,289,52},
        						   {70,338,92},{109,456,52},{134,530,52},{159,606,52},{183,683,52},
        						   {197,743,92},{335,765,52},{404,765,52},{473,765,52},{543,766,52},
        						   {575,641,52},{522,569,52},{398,405,135},{681,387,52},{593,409,52},
        						   {438,206,52},{438,301,52},{197,386,52},{283,410,52},{302,641,52},{354,569,52}};
        	node_location=new_location;
        	map_len=36;
        	break;
        case 6:
        	//육각형 맵의 경우 노드 위치 재설정
        	boardImg = new ImageIcon(UI.class.getResource("/images/육각형_윷놀이판.png")).getImage();
        	int[][] map6_location= {{750,635,86},{770,570,48},{770,500,48},{770,430,48},{770,358,48},
        							{749,253,86},{695,221,48},{640,186,48},{583,155,48},{527,123,48},
        							{421,65,86},{363,123,48},{302,158,48},{242,193,48},{181,228,48},
        							{94,254,86},{111,358,48},{111,429,48},{111,500,48},{111,570,48},
        							{93,634,86},{177,690,48},{239,723,48},{300,758,48},{361,791,48},
        							{421,808,86},{510,796,48},{573,762,48},{635,729,48},{696,696,48},
        							{674,583,48},{577,524,48},{393,392,142},{683,311,48},{584,368,48},
        							{441,168,48},{441,283,48},{203,315,48},{303,372,48},{202,581,48},
        							{302,523,48},{437,712,48},{440,597,48}};
        	node_location=map6_location;
        	map_len=43;
        }
        
        map_node=new CirclePanel[map_len];
        //맵의 각 노드에 패널 설정
        for(int i=0;i<map_len;i++) {
        	map_node[i]=new CirclePanel(node_location[i][2]);
        	map_node[i].setBounds(node_location[i][0],node_location[i][1], node_location[i][2],node_location[i][2]);
        	map_node[i].setLayout(new GridBagLayout());
        	map_node[i].setOpaque(false);
		  	map.add(map_node[i]);
        	
        }
        
        //맵 이미지를 화면에 full되도록 설정
        JLabel lblNewLabel = new JLabel() {                       
    		@Override protected void paintComponent(Graphics g) {  
    			super.paintComponent(g);
    			g.drawImage(boardImg, 0, 0, getWidth(), getHeight(), this);
    		}
    	};
        lblNewLabel.setBounds(0, 0, map.getWidth(), map.getHeight());
		map.add(lblNewLabel);
		
		//player 패널의 각 위치(0~3번 행이 각 player 패널 위치를 나타냄)
        int[][] player_position = {{0, 0},
        						   {0, 249},
        						   {0, 502},
        						   {0, 761}};
        //player와 your turn 라벨, 그리고 말의 패널들을 저장하는 배열 할당
        player= new JPanel[player_count];
        turn_label=new JLabel[player_count];
        piece=new JPanel[player_count][piece_count];
        
        //플레이어 패널 안에서 각 말들의 위치(0~5번 행이 각 말들의 위치를 나타냄)
        int[][] piece_position= {{33,30},{112,30},{191,30},
        						 {33,112},{112,112}};
        //player에 따른 말들의 색 rgb 값
        int[][] piece_color= {{255,0,0},{255,165,0},{0,255,0},{0,0,255}};        
        
        //player와 your_turn, 그리고 말들의 패널을 저장하는 배열에 패널들을 저장
        for(int i=0;i<player_count;i++) {
        	player[i]=new JPanel();
        	player[i].setBackground(UIManager.getColor("Table.selectionBackground"));
  		  	player[i].setBounds(player_position[i][0],player_position[i][1],269, 179);
  		  	frame.getContentPane().add(player[i]);
  		  	player[i].setLayout(null);
  		  	
  		  	turn_label[i]=new JLabel("your turn");
  		  	turn_label[i].setBounds(109, 5, 100, 15);
  		  	player[i].add(turn_label[i]);
  		  	turn_label[i].setVisible(false);
  		  	
  		  	for(int j=0;j<piece_count;j++) {
  		  		piece[i][j]=new CirclePanel(46);
  		  		piece[i][j].setBounds(piece_position[j][0],piece_position[j][1], 46,46);
  		  		piece[i][j].setBackground(new Color(piece_color[i][0],piece_color[i][1],piece_color[i][2]));
  		//////////////////////////////////////////////////////////////////////////////////추가
  		  		piece[i][j].setLayout(new BorderLayout());
  		//////////////////////////////////////////////////////////////////////////////////추가
  		  		player[i].add(piece[i][j]);
  		  	}
        }
        //시작 시 첫 번째 플레이러부터 시작하므로 첫 번쨰 player의 your turn 패널만 보이게 설정
        turn_label[0].setVisible(true);
        
        //윷 던지기 패널 초기화
        throwing = new JPanel();
		throwing.setBackground(new Color(139, 0, 139));
		throwing.setBounds(1200, 66, 424, 372);
		frame.getContentPane().add(throwing);
		throwing.setLayout(null);
		
		
		
		//윷 상태 패널 초기화	
		yut_state = new JPanel();
		yut_state.setBackground(new Color(46, 139, 87));
		yut_state.setBounds(1200, 516, 424, 372);
		frame.getContentPane().add(yut_state);
		yut_state.setLayout(null);
		
		//////////////////////////////////////////////////////////////////////////////////추가
		yut_state_image = new JPanel[6];
		for(int i = 0 ; i<6 ; i++) {
			yut_state_image[i] = new JPanel();
		}
		int[][] yut_state_loc = new int[][]{{32,44,72,99},{176, 44, 72, 99},{320, 44, 72, 99},{32, 207, 72, 99},{176, 207, 72, 99},{320, 207, 72, 99}};
		for(int i = 0 ; i<6 ; i++) {
			yut_state_image[i].setBounds(yut_state_loc[i][0],yut_state_loc[i][1],yut_state_loc[i][2],yut_state_loc[i][3]);
			yut_state.add(yut_state_image[i]);
		}
		
		yut_state_text = new JLabel[6];
		for(int i = 0 ; i<6 ; i++) {
			yut_state_text[i] = new JLabel("0");
		}
		int[][] text_loc = new int[][]{{64, 153, 57, 15},{208, 153, 57, 15},{352, 153, 57, 15},{64, 316, 57, 15},{208, 316, 57, 15},{352, 316, 57, 15}};
		for(int i = 0 ; i<6 ; i++) {
			yut_state_text[i].setBounds(text_loc[i][0],text_loc[i][1],text_loc[i][2],text_loc[i][3]);
			yut_state.add(yut_state_text[i]);
		}
		//////////////////////////////////////////////////////////////////////////////////추가
		
		frame.setVisible(true);
		
		//맵 형태, 플레이어 수, 말의 수 반환
        return new int[]{map_type, player_count, piece_count};
		  	
	}

	  //piece(0~4)가 map_index 에 도착(만약 먹혔으면 -1, 도착했으면 100)
	public void mapupdate(User[] user){
		  
		  //플레이어 패널 안에서 각 말들의 위치(0~5번 행이 각 말들의 위치를 나타냄)
		int[][] piece_position= {{33,30},{112,30},{191,30},
	        						 {33,112},{112,112}};
		
		for(int i=0;i<user.length;i++) {
			ArrayList<Horse> pieces=user[i].getHorses();
			
			for(int j=0;j<pieces.size();j++) {
				int state=pieces.get(j).getStatus();
				int location;
				
				piece[i][j].removeAll();
				
				switch(state) {
				case Horse.WAITING:
					piece[i][j].setLocation(piece_position[j][0],piece_position[j][1]);
					player[i].add(piece[i][j]);
					break;
				case Horse.ON_MAP:
					location=pieces.get(j).getLocation();
					map_node[location].add(piece[i][j]);
					break;
				case Horse.OVERLAPPED:
					location=pieces.get(j).getLocation();
					int count=pieces.get(j).getCount();
					JLabel group_count=new JLabel(String.valueOf(count));
					piece[i][j].add(group_count);
					map_node[location].removeAll();
					map_node[location].add(piece[i][j]);
					break;
				case Horse.ARRIVED:
					piece[i][j].setLocation(piece_position[j][0],piece_position[j][1]);
					player[i].add(piece[i][j]);
					piece[i][j].setEnabled(false);
					JLabel complete=new JLabel("도착");
					piece[i][j].add(complete);
				}
			}
		}
	}
	  
	  
	  
	  
	  //return 0 means random button click, return 1 means select button click
	  public int throwing(){
		 	int output;
		  	BlockingQueue<Integer> clickQueue = new ArrayBlockingQueue<>(1);
			JButton random_button = new JButton("random");
			random_button.setBounds(58, 146, 122, 54);
			random_button.addActionListener(e -> {
                // 클릭된 버튼 인덱스를 큐에 넣음 (한 번만 가능)
            	try {
					clickQueue.put(0);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            });
			throwing.add(random_button);
			
			JButton choose_button = new JButton("choose");
			choose_button.setBounds(242, 146, 122, 54);
			choose_button.addActionListener(e -> {
                // 클릭된 버튼 인덱스를 큐에 넣음 (한 번만 가능)
            	try {
					clickQueue.put(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            });
			throwing.add(choose_button);
			throwing.revalidate();
			throwing.repaint();
			
			 try {
		        	output = clickQueue.take();
		        	throwing.removeAll();
		        	throwing.revalidate();
		        	throwing.repaint();
		            // 버튼 클릭까지 대기
		            return output;
		        } catch (InterruptedException e) {
		            Thread.currentThread().interrupt();
		            return -1; // 예외 발생 시 -1 반환
		        }
	  }

	  //return 0~5 빽도 도 개 걸 윷 모
	  public int choice_yut(){
		  	int output;
		  	BlockingQueue<Integer> clickQueue = new ArrayBlockingQueue<>(1);
			JLabel choiceone = new JLabel("CHOOSE ONE");

			
			choiceone.setFont(new Font("굴림", Font.BOLD, 29));
			choiceone.setBounds(108, 0, 220, 34);
			throwing.add(choiceone);
			
			JButton button_arr[] = new JButton[6];
			for(int i = 0 ; i<6 ; i++) {
				button_arr[i] = new JButton("1");
			}
			
			int [][] button_loc = {{32,44,72,99},{176, 44, 72, 99},{320, 44, 72, 99},{32, 207, 72, 99},{176, 207, 72, 99},{320, 207, 72, 99}};
			for(int i = 0 ; i<6 ; i++) {
				final int index = i;
				button_arr[i].setBounds(button_loc[i][0],button_loc[i][1],button_loc[i][2],button_loc[i][3]);
				button_arr[i].addActionListener(e -> {
	                // 클릭된 버튼 인덱스를 큐에 넣음 (한 번만 가능)
	            	try {
						clickQueue.put(index);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            });
				throwing.add(button_arr[i]);
			}

			
			throwing.revalidate();
			throwing.repaint();

			 try {
		        	output = clickQueue.take();
		        	throwing.removeAll();
		        	throwing.revalidate();
		        	throwing.repaint();
		            // 버튼 클릭까지 대기
		            return output;
		        } catch (InterruptedException e) {
		            Thread.currentThread().interrupt();
		            return -1; // 예외 발생 시 -1 반환
		        }
	  }

	  // return chosen horse number    start from 0
	  public int choice_horse(int turn){
		  JButton horse_button[] = new JButton[piece[0].length];
		  int output;
		  BlockingQueue<Integer> clickQueue = new ArrayBlockingQueue<>(1);
		  for(int i = 0 ;i<piece[0].length; i++) {
			  final int index = i;
			  horse_button[i] = new JButton(Integer.toString(i)+"번");
			  horse_button[i].addActionListener(e -> {
	                // 클릭된 버튼 인덱스를 큐에 넣음 (한 번만 가능)
	            	try {
						clickQueue.put(index);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            });
			  piece[turn][i].add(horse_button[i], BorderLayout.CENTER);
			  piece[turn][i].revalidate();
			  piece[turn][i].repaint();
			  
		  }
		  
			 try {
		        	output = clickQueue.take();
		        	for(int i = 0 ;i<piece[0].length; i++) {
		        		piece[turn][i].removeAll();
		        		piece[turn][i].revalidate();
		        		piece[turn][i].repaint();
		        	}

		            // 버튼 클릭까지 대기
		            return output;
		        } catch (InterruptedException e) {
		            Thread.currentThread().interrupt();
		            return -1; // 예외 발생 시 -1 반환
		        }
	  }

	  // if restart button pressed return 1; end button return 0
	  public int end(int winner){
		  frame.getContentPane().removeAll();
		  int output;
		  BlockingQueue<Integer> clickQueue = new ArrayBlockingQueue<>(1);
		  	
		  JPanel panel = new JPanel();
		  panel.setBounds(25, 10, 1587, 923);
		  frame.getContentPane().add(panel);
		  panel.setLayout(null);
			
		  JLabel text = new JLabel("Player " + Integer.toString(winner+1) + " win!!");
		  text.setFont(new Font("굴림", Font.PLAIN, 99));
		  text.setBounds(444, 10, 978, 349);
		  panel.add(text);
		  
		  JButton restart = new JButton("restart");
		  restart.setBounds(196, 342, 350, 276);
		  restart.addActionListener(e -> {
			  // 클릭된 버튼 인덱스를 큐에 넣음 (한 번만 가능)
			  try {
				  clickQueue.put(1);
			  } catch (InterruptedException e1) {
				  // TODO Auto-generated catch block
				  e1.printStackTrace();
			  }
           	});
		  panel.add(restart);
			
		  JButton btnEnd = new JButton("end");
		  btnEnd.setBounds(1017, 342, 350, 276);
		  btnEnd.addActionListener(e -> {
			  // 클릭된 버튼 인덱스를 큐에 넣음 (한 번만 가능)
			  try {
				  clickQueue.put(0);
			  } catch (InterruptedException e1) {
				  // TODO Auto-generated catch block
				  e1.printStackTrace();
			  }
           	});
		  panel.add(btnEnd);
		  
		  frame.revalidate();
		  frame.repaint();
		  try {
			  output = clickQueue.take();
			  frame.dispose();
			  // 버튼 클릭까지 대기
			  return output;
		  } catch (InterruptedException e) {
			  Thread.currentThread().interrupt();
			  return -1; // 예외 발생 시 -1 반환
		  }
	  }
	  //player 0~3
	  public void turnchange(int turn) {
		  for(int i = 0 ; i<turn_label.length ; i++) {
			  turn_label[i].setVisible(false);
		  }
		  turn_label[turn].setVisible(true);
	  }
	  
	  //윷 던진 결과를 UI에 보여주는 메서드: 인수로 윷 던진 결과의 횟수 배열 필요(index 0-> 도, 1-> 개, 2->걸, 3-> 윷, 4->모: 각 인덱스에 이동 횟수 저장)
	  public void yut_state_update(int[] state_arr) {
		  for(int i = 0 ; i<6 ; i++) {
			  yut_state_text[i].setText(Integer.toString(state_arr[i]));
		  }
	  }
	  
	  public static void main(String[] args) {
		  UI a= new UI();
		  int[] num=a.gamesetup();

	  }
	  
	//원 모양 패널을 만들기 위한 class
	  public class CirclePanel extends JPanel {
		  //원 패널의 지름
		    private final int diameter;

		    public CirclePanel(int diameter) {
		        this.diameter = diameter;
		        setOpaque(false); // 배경은 투명
		        setPreferredSize(new Dimension(diameter, diameter));
		        setBackground(new Color(0, 0, 0, 128)); // 기본 원 색 (투명)
		    }

		    @Override
		    protected void paintComponent(Graphics g) {
		        //배경 처리
		        super.paintComponent(g);

		        Graphics2D g2 = (Graphics2D) g.create();
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                            RenderingHints.VALUE_ANTIALIAS_ON);

		        // 중심 좌표 기준으로 정사각형 내에 원 그리기
		        g2.setColor(getBackground()); // 배경색 → 원 색으로 사용
		        g2.fillOval(0, 0, diameter, diameter);

		        g2.dispose();
		    }

		    /**
		     * 마우스 이벤트 처리 범위를 원 내부로 제한
		     */
		    @Override
		    public boolean contains(int x, int y) {
		        int radius = diameter / 2;
		        int dx = x - radius;
		        int dy = y - radius;
		        return dx * dx + dy * dy <= radius * radius;
		    }
		}
		  
}
