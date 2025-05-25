package view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.embed.swing.JFXPanel;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import model.*;

public class UI implements View {
    
    private Stage primaryStage;
    private Scene gameScene;
    private VBox throwing;           // 윷 던지기 패널
    private Pane yutState;          // 윷 던진 결과 패널
    
    private VBox[] player;          // 플레이어 패널 저장 배열
    private Label[] turn_label;     // 플레이어 패널 영역에 있는 "your turn" 라벨 배열
    private Pane[][] piece;         // 플레이어 수 X 말의 수 shape의 말 패널 배열
    private Pane[] map_node;        // 맵의 노드 패널 배열
    
    private Label[] yut_state_image; // 오른쪽 위의 윷 이미지
    private Label[] yut_state_text;  // 오른쪽 위 윷 텍스트
    private String[] yutUrl = {"도","개","걸","윷","모","빽도"};
    
    private Image boardImg;         // 윷 판 이미지
    private Pane frame;             // 메인 프레임 역할
    
    // 게임 시작 및 맵, 플레이어, 말 수 반환 type int[map, player, piece]
    // 게임 시작 및 맵, 플레이어, 말 수 반환 type int[map, player, piece]
    public int[] gameSetup() {
    	
    	try {
    		Platform.startup(() -> {
                // 초기화 완료
            });
    	}catch(Exception e){}
   
        CompletableFuture<int[]> future = new CompletableFuture<>();
        
        Platform.runLater(() -> {
            try {
                // 설정 다이얼로그 생성
                Dialog<int[]> setupDialog = new Dialog<>();
                setupDialog.setTitle("게임 설정");
                setupDialog.setHeaderText("윷놀이 게임 설정을 선택하세요");
                
                // 버튼 타입 설정
                ButtonType okButtonType = new ButtonType("확인", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButtonType = new ButtonType("취소", ButtonBar.ButtonData.CANCEL_CLOSE);
                setupDialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
                
                // 설정 폼 생성
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));
                
                // 맵 형태 선택
                ToggleGroup mapGroup = new ToggleGroup();
                RadioButton map_4 = new RadioButton("사각형 맵");
                RadioButton map_5 = new RadioButton("오각형 맵");
                RadioButton map_6 = new RadioButton("육각형 맵");
                
                map_4.setToggleGroup(mapGroup);
                map_5.setToggleGroup(mapGroup);
                map_6.setToggleGroup(mapGroup);
                map_4.setSelected(true); // 기본값
                
                map_4.setUserData(4);
                map_5.setUserData(5);
                map_6.setUserData(6);
                
                HBox mapBox = new HBox(10);
                mapBox.getChildren().addAll(map_4, map_5, map_6);
                
                // 플레이어 수와 말 개수 선택
                ComboBox<Integer> cbPlayer = new ComboBox<>();
                cbPlayer.getItems().addAll(2, 3, 4);
                cbPlayer.setValue(2);
                
                ComboBox<Integer> cbToken = new ComboBox<>();
                cbToken.getItems().addAll(2, 3, 4, 5);
                cbToken.setValue(2);
                
                grid.add(new Label("맵 형태:"), 0, 0);
                grid.add(mapBox, 1, 0);
                grid.add(new Label("플레이어 수:"), 0, 1);
                grid.add(cbPlayer, 1, 1);
                grid.add(new Label("말 개수:"), 0, 2);
                grid.add(cbToken, 1, 2);
                
                setupDialog.getDialogPane().setContent(grid);
                
                // 결과 변환기 설정
                setupDialog.setResultConverter(dialogButton -> {
                    if (dialogButton == okButtonType) {
                        RadioButton selectedMap = (RadioButton) mapGroup.getSelectedToggle();
                        int map_type = (Integer) selectedMap.getUserData();
                        int player_count = cbPlayer.getValue();
                        int piece_count = cbToken.getValue();
                        return new int[]{map_type, player_count, piece_count};
                    }
                    return null;
                });
                
                // 다이얼로그 표시 및 결과 처리
                java.util.Optional<int[]> result = setupDialog.showAndWait();
                
                if (!result.isPresent()) {
                    future.complete(null);
                    return;
                }
                
                int[] config = result.get();
                int map_type = config[0];
                int player_count = config[1];
                int piece_count = config[2];
                
                // 메인 게임 화면 설정
                primaryStage = new Stage();
                primaryStage.setTitle("윷놀이 게임");
                primaryStage.setWidth(1640);
                primaryStage.setHeight(982);
                
                // 루트 패널 생성
                frame = new Pane();
                frame.setPrefSize(1640, 982);
                
                // 배경 이미지 설정
                try {
                    Image frameImg = new Image(getClass().getResourceAsStream("/images/배경색.png"));
                    ImageView backgroundView = new ImageView(frameImg);
                    backgroundView.setFitWidth(1640);
                    backgroundView.setFitHeight(982);
                    frame.getChildren().add(backgroundView);
                } catch (Exception e) {
                    frame.setStyle("-fx-background-color: lightgray;");
                }
                
                // 맵 부분 패널 초기화
                Pane map = new Pane();
                map.setStyle("-fx-background-color: transparent;");
                map.setLayoutX(269);
                map.setLayoutY(10);
                map.setPrefSize(930, 930);
                frame.getChildren().add(map);
                
                // 맵 타입에 따른 노드 위치와 길이 설정
                int[][] node_location = {{758, 756,145},{795, 625, 71},{795, 495,71},{795, 364,71},{795, 230,71},
                       { 759, 28,145},{628,66,71},{499,66,71},{365,66,71},{232,66,71},
                       {30,27,145},{67,229,71},{67,363,71},{67,495,71},{67,628,71},
                       {31, 755, 145},{233,793,71},{366,793,71},{498,793,71},{630,793,71},
                       {663,661,71},{565,561,71},{394,392,145},{662,198,71},{562,297,71},
                       {201,197,71},{299,297,71},{199,658,71},{300,561,71}};
                int map_len = 29;
                
                // 맵의 형태에 따른 맵 이미지 및 변수 데이터 설정
                switch(map_type) {
                    case 4:
                        try {
                            boardImg = new Image(getClass().getResourceAsStream("/images/사각형_윷놀이판.jpg"));
                        } catch (Exception e) {}
                        break;
                    case 5:
                        try {
                            boardImg = new Image(getClass().getResourceAsStream("/images/오각형_윷놀이판.png"));
                        } catch (Exception e) {}
                        int[][] new_location = {{642,743,92},{693, 684, 52},{718, 608, 52},{743, 531, 52},{768, 456, 52},
                                    {767,336,92},{719,290,52},{656,241,52},{591,192,52},{528,142,52},
                                    {419,71,92},{351,142,52},{286,191,52},{222,240,52},{159,289,52},
                                    {70,338,92},{109,456,52},{134,530,52},{159,606,52},{183,683,52},
                                    {197,743,92},{335,765,52},{404,765,52},{473,765,52},{543,766,52},
                                    {575,641,52},{522,569,52},{398,405,135},{681,387,52},{593,409,52},
                                    {438,206,52},{438,301,52},{197,386,52},{283,410,52},{302,641,52},{354,569,52}};
                        node_location = new_location;
                        map_len = 36;
                        break;
                    case 6:
                        try {
                            boardImg = new Image(getClass().getResourceAsStream("/images/육각형_윷놀이판.png"));
                        } catch (Exception e) {}
                        int[][] map6_location = {{750,635,86},{770,570,48},{770,500,48},{770,430,48},{770,358,48},
                                    {749,253,86},{695,221,48},{640,186,48},{583,155,48},{527,123,48},
                                    {421,65,86},{363,123,48},{302,158,48},{242,193,48},{181,228,48},
                                    {94,254,86},{111,358,48},{111,429,48},{111,500,48},{111,570,48},
                                    {93,634,86},{177,690,48},{239,723,48},{300,758,48},{361,791,48},
                                    {421,808,86},{510,796,48},{573,762,48},{635,729,48},{696,696,48},
                                    {674,583,48},{577,524,48},{393,392,142},{683,311,48},{584,368,48},
                                    {441,168,48},{441,283,48},{203,315,48},{303,372,48},{202,581,48},
                                    {302,523,48},{437,712,48},{440,597,48}};
                        node_location = map6_location;
                        map_len = 43;
                }
                
                map_node = new CirclePane[map_len];
                // 맵의 각 노드에 패널 설정
                for (int i = 0; i < map_len; i++) {
                    int d = node_location[i][2];
                    CirclePane node = new CirclePane(d);
                    node.setLayoutX(node_location[i][0]);
                    node.setLayoutY(node_location[i][1]);
                    node.setPrefSize(d, d);
                    map.getChildren().add(node);
                    map_node[i] = node;
                }
                
                // 맵 이미지를 화면에 표시
                if (boardImg != null) {
                    ImageView boardView = new ImageView(boardImg);
                    boardView.setFitWidth(930);
                    boardView.setFitHeight(930);
                    map.getChildren().add(0, boardView); // 배경으로 설정
                }
                
                // player 패널의 각 위치
                int[][] player_position = {{0, 0}, {0, 249}, {0, 502}, {0, 761}};
                
                // player와 your turn 라벨, 그리고 말의 패널들을 저장하는 배열 할당
                player = new VBox[player_count];
                turn_label = new Label[player_count];
                piece = new Pane[player_count][piece_count];
                
                // 플레이어 패널 안에서 각 말들의 위치
                int[][] piece_position = {{33,30},{112,30},{191,30},{33,112},{112,112}};
                
                // player에 따른 말들의 색 rgb 값
                Color[] piece_color = {Color.RED, Color.ORANGE, Color.GREEN, Color.BLUE};
                
                // player와 your_turn, 그리고 말들의 패널을 저장하는 배열에 패널들을 저장
                for(int i = 0; i < player_count; i++) {
                    Pane playerPane = new Pane();
                    playerPane.setLayoutX(player_position[i][0]);
                    playerPane.setLayoutY(player_position[i][1]);
                    playerPane.setPrefSize(269, 179);
                    
                    // 플레이어 배경 이미지
                    try {
                        Image playerImg = new Image(getClass().getResourceAsStream("/images/player" + (i+1) + ".png"));
                        ImageView playerView = new ImageView(playerImg);
                        playerView.setFitWidth(269);
                        playerView.setFitHeight(179);
                        playerPane.getChildren().add(playerView);
                    } catch (Exception e) {
                        playerPane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
                    }
                    
                    frame.getChildren().add(playerPane);
                    
                    turn_label[i] = new Label("your turn");
                    turn_label[i].setLayoutX(109);
                    turn_label[i].setLayoutY(5);
                    turn_label[i].setVisible(false);
                    playerPane.getChildren().add(turn_label[i]);
                    
                    for(int j = 0; j < piece_count; j++) {
                        piece[i][j] = new Pane();
                        piece[i][j].setLayoutX(piece_position[j][0]);
                        piece[i][j].setLayoutY(piece_position[j][1]);
                        piece[i][j].setPrefSize(46, 46);
                        piece[i][j].setStyle("-fx-background-color: " + toRGBCode(piece_color[i]) + 
                                           "; -fx-background-radius: 5;");
                        playerPane.getChildren().add(piece[i][j]);
                    }
                    
                    player[i] = new VBox();
                    player[i].getChildren().add(playerPane);
                    player[i].setLayoutX(player_position[i][0]);
                    player[i].setLayoutY(player_position[i][1]);
                    
                    frame.getChildren().add(player[i]);
                }
                
                // 시작 시 첫 번째 플레이어부터 시작
                turn_label[0].setVisible(true);
                
                // 윷 던지기 패널 초기화
                throwing = new VBox();
                throwing.setLayoutX(1200);
                throwing.setLayoutY(66);
                throwing.setPrefSize(424, 372);
                throwing.setAlignment(Pos.CENTER);
                throwing.setSpacing(10);
                
                String throwing_imagePath = getClass().getResource("/images/윷던지기_윷상태.png").toExternalForm();
                throwing.setStyle(
                    "-fx-border-color: black; " +
                    "-fx-border-width: 7; " +
                    "-fx-background-image: url('" + throwing_imagePath + "'); " +
                    "-fx-background-size: 424 372; " +  // 정확한 크기로 설정
                    "-fx-background-repeat: no-repeat; " +
                    "-fx-background-position: center;"
                );
                
                frame.getChildren().add(throwing);
                
                // 윷 상태 패널 초기화
                yutState = new Pane();
                yutState.setLayoutX(1200);
                yutState.setLayoutY(516);
                yutState.setPrefSize(424, 372);
                
                yutState.setStyle(
                        "-fx-border-color: black; " +
                        "-fx-border-width: 7; " +
                        "-fx-background-image: url('" + throwing_imagePath + "'); " +
                        "-fx-background-size: 424 372; " +  // 정확한 크기로 설정
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-position: center;"
                    );
                
                frame.getChildren().add(yutState);
                
                // 윷 상태 이미지와 텍스트 초기화
                yut_state_image = new Label[6];
                yut_state_text = new Label[6];
                
                int[][] yut_state_loc = {{32,44,72,99},{176, 44, 72, 99},{320, 44, 72, 99},{32, 207, 72, 99},{176, 207, 72, 99},{320, 207, 72, 99}};
                int[][] text_loc = {{64, 153, 57, 15},{208, 153, 57, 15},{352, 153, 57, 15},{64, 316, 57, 15},{208, 316, 57, 15},{352, 316, 57, 15}};
                
                for(int i = 0; i < 6; i++) {
                    yut_state_image[i] = new Label();
                    try {
                        Image yutStateImage = new Image(getClass().getResourceAsStream("/images/" + yutUrl[i] + ".png"));
                        ImageView yutStateImageView = new ImageView(yutStateImage);
                        yutStateImageView.setFitWidth(yut_state_loc[i][2]);
                        yutStateImageView.setFitHeight(yut_state_loc[i][3]);
                        yut_state_image[i].setGraphic(yutStateImageView);
                    } catch (Exception e) {
                        yut_state_image[i].setText(yutUrl[i]);
                    }
                    yut_state_image[i].setLayoutX(yut_state_loc[i][0]);
                    yut_state_image[i].setLayoutY(yut_state_loc[i][1]);
                    yutState.getChildren().add(yut_state_image[i]);
                    
                    yut_state_text[i] = new Label("0");
                    yut_state_text[i].setLayoutX(text_loc[i][0]);
                    yut_state_text[i].setLayoutY(text_loc[i][1]);
                    yutState.getChildren().add(yut_state_text[i]);
                }
                
                gameScene = new Scene(frame, 1640, 982);
                primaryStage.setScene(gameScene);
                primaryStage.show();
                
                future.complete(new int[]{map_type, player_count, piece_count});
                
            } catch (Exception e) {
                e.printStackTrace();
                future.complete(null);
            }
        });
        
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // user들의 각 말들을 맵에 업데이트 하는 함수
    public void mapUpdate(Player[] players) {
        Platform.runLater(() -> {
            // 플레이어 패널 안에서 각 말들의 위치
            int[][] piece_position = {{33,30},{112,30},{191,30},{33,112},{112,112}};
            
            // 모든 노드와 플레이어 패널에서 말 제거
            for(int i = 0; i < players.length; i++) {
                ArrayList<Piece> pieces = players[i].getPieces();
                for(int j = 0; j < pieces.size(); j++) {
                    piece[i][j].getChildren().clear();
                    if (piece[i][j].getParent() != null) {
                        ((Pane)piece[i][j].getParent()).getChildren().remove(piece[i][j]);
                    }
                }
            }
            
            // 모든 맵 노드에서 말 제거
            for(Pane node : map_node) {
                node.getChildren().removeIf(child -> !(child instanceof Circle));
            }
            
            // 각 player의 말 읽어들이기
            for(int i = 0; i < players.length; i++) {
                ArrayList<Piece> pieces = players[i].getPieces();
                
                // 말 배열 loop 문
                for(int j = 0; j < pieces.size(); j++) {
                    Piece.State state = pieces.get(j).getStatus();
                    Node position;
                    
                    System.out.println(state);
                    switch(state) {
                        // 말이 대기 상태인 경우
                        case WAITING:
                            piece[i][j].setLayoutX(piece_position[j][0]);
                            piece[i][j].setLayoutY(piece_position[j][1]);
                            ((Pane)player[i].getChildren().get(0)).getChildren().add(piece[i][j]);
                            break;
                            
                        // 말이 맵 위에 있는 경우
                        case ON_BOARD:
                            position = pieces.get(j).getPosition();
                            int on_d = (int)map_node[position.id].getPrefHeight();
                            piece[i][j].setLayoutX((on_d-46)/2);
                            piece[i][j].setLayoutY((on_d-46)/2);
                            map_node[position.id].getChildren().add(piece[i][j]);
                            break;
                            
                        // 말이 겹쳐진 경우
                        case OVERLAPPED:
                            position = pieces.get(j).getPosition();
                            int count = pieces.get(j).getCount();
                            System.out.println(count);
                            
                            // 겹쳐진 말의 수를 text로 표시
                            Label group_count = new Label(String.valueOf(count));
                            boolean hasCountLabel = map_node[position.id].getChildren().stream()
                                .anyMatch(child -> child instanceof Label && ((Label)child).getText().matches("\\d+"));
                            
                            if(!hasCountLabel) {
                                group_count.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
                                group_count.setStyle("-fx-background-color: transparent;");
                                
                                int over_d = (int)map_node[position.id].getPrefHeight();
                                group_count.setLayoutX(over_d/2 - 10);
                                group_count.setLayoutY(over_d/2 - 10);
                                
                                piece[i][j].setLayoutX((over_d-46)/2);
                                piece[i][j].setLayoutY((over_d-46)/2);
                                
                                map_node[position.id].getChildren().addAll(piece[i][j], group_count);
                                
                                group_count.setMouseTransparent(true);
                            }
                            break;
                            
                        // 말이 도착한 경우
                        case FINISHED:
                            piece[i][j].setLayoutX(piece_position[j][0]);
                            piece[i][j].setLayoutY(piece_position[j][1]);
                            piece[i][j].setDisable(true);
                            
                            Label complete = new Label("도착");
                            complete.setAlignment(Pos.CENTER);
                            piece[i][j].getChildren().add(complete);
                            ((Pane)player[i].getChildren().get(0)).getChildren().add(piece[i][j]);
                            break;
                    }
                }
            }
        });
    }
    
    // return false means random button click, return true means select button click
    public boolean throwing() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        Platform.runLater(() -> {
            throwing.getChildren().clear();
            
            Button random_button = new Button();
            try {
                Image randomImg = new Image(getClass().getResourceAsStream("/images/랜덤윷던지기.png"));
                ImageView randomView = new ImageView(randomImg);
                randomView.setFitWidth(122);
                randomView.setFitHeight(54);
                random_button.setGraphic(randomView);
            } catch (Exception e) {
                random_button.setText("랜덤");
            }
            random_button.setOnAction(e -> future.complete(false));
            
            Button choose_button = new Button();
            try {
                Image chooseImg = new Image(getClass().getResourceAsStream("/images/선택윷던지기.png"));
                ImageView chooseView = new ImageView(chooseImg);
                chooseView.setFitWidth(122);
                chooseView.setFitHeight(54);
                choose_button.setGraphic(chooseView);
            } catch (Exception e) {
                choose_button.setText("선택");
            }
            choose_button.setOnAction(e -> future.complete(true));
            
            HBox buttonBox = new HBox(20);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(random_button, choose_button);
            throwing.getChildren().add(buttonBox);
        });
        
        try {
            boolean result = future.get();
            Platform.runLater(() -> {
                throwing.getChildren().clear();
            });
            return result;
        } catch (Exception e) {
            return false;
        }
    }
    
    // return 0~5 빽도 도 개 걸 윷 모
    public int choiceYut() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        
        Platform.runLater(() -> {
            throwing.getChildren().clear();
            
            Label choiceone = new Label("CHOOSE ONE");
            choiceone.setFont(Font.font("굴림", FontWeight.BOLD, 29));
            
            GridPane buttonGrid = new GridPane();
            buttonGrid.setHgap(10);
            buttonGrid.setVgap(10);
            buttonGrid.setAlignment(Pos.CENTER);
            
            for(int i = 0; i < 6; i++) {
                final int index = i;
                Button button = new Button();
                
                try {
                    Image yutImage = new Image(getClass().getResourceAsStream("/images/" + yutUrl[i] + ".png"));
                    ImageView yutView = new ImageView(yutImage);
                    yutView.setFitWidth(72);
                    yutView.setFitHeight(99);
                    button.setGraphic(yutView);
                } catch (Exception e) {
                    button.setText(yutUrl[i]);
                }
                
                button.setOnAction(e -> future.complete(index));
                buttonGrid.add(button, i % 3, i / 3);
            }
            
            VBox choiceBox = new VBox(10);
            choiceBox.setAlignment(Pos.CENTER);
            choiceBox.getChildren().addAll(choiceone, buttonGrid);
            throwing.getChildren().add(choiceBox);
        });
        
        try {
            int result = future.get();
            Platform.runLater(() -> {
                throwing.getChildren().clear();
            });
            return result;
        } catch (Exception e) {
            return -1;
        }
    }
    
    // return chosen horse number start from 0
    public int choicePiece(int turn) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        
        Platform.runLater(() -> {
            for(int i = 0; i < piece[0].length; i++) {
                final int index = i;
                Button horse_button = new Button();
                horse_button.setStyle("-fx-background-color: transparent;");
                horse_button.setPrefSize(46, 46);
                horse_button.setOnAction(e -> future.complete(index));
                
                if(piece[turn][i].getChildren().isEmpty()) {
                    piece[turn][i].getChildren().add(horse_button);
                }
            }
        });
        
        try {
            int result = future.get();
            Platform.runLater(() -> {
                for(int i = 0; i < piece[0].length; i++) {
                    if(!piece[turn][i].isDisabled()) {
                        piece[turn][i].getChildren().clear();
                    }
                }
            });
            return result;
        } catch (Exception e) {
            return -1;
        }
    }
    
    // if restart button pressed return 1; end button return 0
    public int end(int winner) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        Platform.runLater(() -> {
            // 화면 초기화
            frame.getChildren().clear();

            // 1) 배경과 컨텐츠 겹침을 위한 StackPane
            StackPane root = new StackPane();
            root.setPrefSize(1587, 923);
            root.setLayoutX(25);
            root.setLayoutY(10);

            // 2) 배경 이미지 설정
            try {
                Image backgroundImg = new Image(getClass().getResourceAsStream("/images/끝배경.png"));
                ImageView backgroundView = new ImageView(backgroundImg);
                backgroundView.setFitWidth(1587);
                backgroundView.setFitHeight(923);
                root.getChildren().add(backgroundView);
            } catch (Exception e) {
                root.setStyle("-fx-background-color: lightgray;");
            }

            // 3) 컨텐츠 레이아웃 (중앙 정렬된 VBox)
            VBox content = new VBox(20);
            content.setAlignment(Pos.CENTER);

            // 승리자 텍스트
            Label winnerText = new Label("Player " + (winner + 1) + " win!!");
            winnerText.setFont(Font.font("굴림", 99));

            // 버튼 박스
            HBox buttonBox = new HBox(50);
            buttonBox.setAlignment(Pos.CENTER);

            // 재시작 버튼
            Button restart = new Button();
            try {
                Image restartImg = new Image(getClass().getResourceAsStream("/images/재시작버튼.png"));
                ImageView restartView = new ImageView(restartImg);
                restartView.setFitWidth(350);
                restartView.setFitHeight(276);
                restart.setGraphic(restartView);
            } catch (Exception e) {
                restart.setText("재시작");
            }
            restart.setOnAction(e -> future.complete(1));

            // 종료 버튼
            Button btnEnd = new Button();
            try {
                Image endImg = new Image(getClass().getResourceAsStream("/images/끝내기버튼.png"));
                ImageView endView = new ImageView(endImg);
                endView.setFitWidth(350);
                endView.setFitHeight(276);
                btnEnd.setGraphic(endView);
            } catch (Exception e) {
                btnEnd.setText("끝내기");
            }
            btnEnd.setOnAction(e -> future.complete(0));

            buttonBox.getChildren().addAll(restart, btnEnd);
            content.getChildren().addAll(winnerText, buttonBox);

            // 4) StackPane에 컨텐츠 추가 (배경 위)
            root.getChildren().add(content);

            // 최종적으로 frame에 붙이기
            frame.getChildren().add(root);
        });

        try {
            // 사용자의 버튼 선택을 기다림
            int result = future.get();

            // 결과 후 창 닫기
            Platform.runLater(() -> primaryStage.close());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    
    // player 0~3
    public void turnChange(int turn) {
        Platform.runLater(() -> {
            for(int i = 0; i < turn_label.length; i++) {
                turn_label[i].setVisible(false);
            }
            turn_label[turn].setVisible(true);
        });
    }
    
    // 윷 던진 결과를 UI에 보여주는 메서드: 인수로 윷 던진 결과의 횟수 배열 필요
    // (index 0-> 도, 1-> 개, 2->걸, 3-> 윷, 4->모: 각 인덱스에 이동 횟수 저장)
    public void yutStateUpdate(int[] state_arr) {
        Platform.runLater(() -> {
            for(int i = 0; i < 6; i++) {
                yut_state_text[i].setText(Integer.toString(state_arr[i]));
            }
        });
    }
    
    public void gameEnd(int player) {
        Platform.runLater(() -> {
            String message = "Player " + (player + 1) + " is Winner!";
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game End");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.initOwner(primaryStage);
            
            alert.showAndWait();
            primaryStage.close();
        });
    }
    
    // 원 모양 패널을 만들기 위한 JavaFX 클래스
    public class CirclePane extends Pane {
        private final int diameter;
        private Circle circle;
        
        public CirclePane(int diameter) {
            this.diameter = diameter;
            setStyle("-fx-background-color: transparent;");
            setPrefSize(diameter, diameter);
            
            // 원형 배경 생성
            circle = new Circle(diameter / 2.0);
            circle.setCenterX(diameter / 2.0);
            circle.setCenterY(diameter / 2.0);
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.TRANSPARENT);
            
            getChildren().add(circle);
        }
        
        // 마우스 이벤트 처리 범위를 원 내부로 제한
        @Override
        public boolean contains(double x, double y) {
            double radius = diameter / 2.0;
            double centerX = diameter / 2.0;
            double centerY = diameter / 2.0;
            double dx = x - centerX;
            double dy = y - centerY;
            return dx * dx + dy * dy <= radius * radius;
        }
    }
    
    // RGB Color를 CSS 색상 코드로 변환하는 유틸리티 메서드
    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
    }
}
