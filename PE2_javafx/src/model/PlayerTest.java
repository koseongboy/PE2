package model;

import view.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class PlayerTest {
  UI ui;
  Player player, player2;
  Piece p1, p2, p3;
  Board board;
  ArrayList<Piece> source, source2;

  @BeforeEach
  public void setUp() {
    ui = new UI();
    board = new Board(4);
    player = new Player(1, 2, ui);
    player2 = new Player(2, 2, ui);
    p1 = player.getPieces().get(0);
    p2 = player.getPieces().get(1);
    p3 = player2.getPieces().get(0);
    for(int i = 0; i < 6; i ++) {
      player.permittedMoves[i] = 100 + i;
    }
    source = new ArrayList<>();
    source2 = new ArrayList<>();
  }


  @Test
  @DisplayName("clearPermittedMoves 테스트")
  public void test_clearPermittedMoves() {

    player.clearPermittedMoves();
    assertEquals(player.permittedMoves[0], 0);
    assertEquals(player.permittedMoves[1], 0);
    assertEquals(player.permittedMoves[2], 0);
    assertEquals(player.permittedMoves[3], 0);
    assertEquals(player.permittedMoves[4], 0);
    assertEquals(player.permittedMoves[5], 0);
  }

  @Test
  @DisplayName("thrownYut 테스트")
  public void test_thrownYut() {
    for (int i = 0; i < 100; i++) {
      int yutResult = Yut.throwYut();
      int throwCount = player.permittedThrows;
      int[] prevPermittedMoves = player.permittedMoves.clone();
      player.thrownYut(yutResult);
      if (yutResult == 3 || yutResult == 4) {
        assertEquals(throwCount, player.permittedThrows);
      }
      assertEquals(player.permittedMoves[yutResult], prevPermittedMoves[yutResult] + 1);
    }
  }

  @Test
  @DisplayName("movePiece 테스트 1: 완주 시 true를 반환하는가?")
  public void test_movePiece_1() {
    boolean result = false;
    source.add(p2);
    board.followPath(source, 5);
    board.followPath(source, 0);

    source2.add(p1);
    board.followPath(source2, 5);
    result = player.movePiece(0, 0, board);
    assertTrue(result);
  }

  @Test
  @DisplayName("movePiece 테스트 2: 말을 잡았을 때 처리를 제대로 하는가?")
  public void test_movePiece_2() {
    int prevPermittedThrows = player.permittedThrows;
    source.add(p3);
    board.followPath(source, 0);
    player.movePiece(0, 0, board);
    assertEquals(prevPermittedThrows + 1, player.permittedThrows); 
  }

  @Test
  @DisplayName("clearPermittedThrows 테스트")
  public void test_clearPermittedThrows() {
    player.permittedThrows = 100;
    player.clearPermittedThrows();
    assertEquals(player.permittedThrows, 1);
  }




  
}
