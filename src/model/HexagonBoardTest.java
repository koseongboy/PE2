package model;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HexagonBoardTest {

    Player player1;
    Player player2;
    Piece p1, p2, p3, p4;
    ArrayList<Player> players;
    Board board;
    ArrayList<Piece> source;
    Node curNode, prevNode;

    @BeforeEach
    public void setUp() {
        // Create 1 piece each for players
        player1 = new Player(2);
        player2 = new Player(2);

        // Get pieces from players
        p1 = player1.getPiece(0);
        p2 = player2.getPiece(0);
        p3 = player1.getPiece(1);
        p4 = player2.getPiece(1);

        p1.setStatus(Piece.State.WAITING);
        p2.setStatus(Piece.State.WAITING);
        p3.setStatus(Piece.State.WAITING);
        p4.setStatus(Piece.State.WAITING);

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        board = new Board(6, players);
        curNode = board.getNode(2);
        prevNode = board.getNode(1);

        source = new ArrayList<>();
    }

    @Test
    public void test_if_stack_is_empty_when_init() {
        Node position = Board.start;
        ArrayList<Piece> result = board.getStackedPieces(position);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetStackedPiecesWithOnePiece() {
        p1.setPosition(Board.start);
        p1.setStatus(Piece.State.ON_BOARD);
        ArrayList<Piece> result = board.getStackedPieces(Board.start);
        assertEquals(1, result.size());
        assertTrue(result.contains(p1));
    }

    @Test
    public void testGetStackedPiecesWithMultiplePieces() {
        p1.setPosition(Board.start);
        p2.setPosition(Board.start);
        p1.setStatus(Piece.State.OVERLAPPED);
        p2.setStatus(Piece.State.ON_BOARD);

        ArrayList<Piece> result = board.getStackedPieces(Board.start);
        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    @Test
    public void test_firstroll_baekdo_normal_move() {
        ArrayList<Piece> source = new ArrayList<>();
        source.add(p1);

        boolean result = board.followPath(source, 5); // 5 represents "bakdo"

        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
        assertEquals(Board.start, p1.getPosition());
        assertFalse(result);
    }

    @Test
    public void test_firstroll_baekdo_catching() {
        // Place p2 (opponent) on the start
        p2.setPosition(Board.start);
        p2.setStatus(Piece.State.ON_BOARD);

        source.add(p1);

        boolean result = board.followPath(source, 5); // Bakdo

        assertTrue(result); // Should have caught p2
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
        assertEquals(Piece.State.WAITING, p2.getStatus());
        assertEquals(board.getNode(0), p1.getPosition());
        assertNull(p2.getPosition());

    }

    @Test
    public void test_firstroll_baekdo_grouping() {
        p1.setPosition(Board.start);
        p1.setStatus(Piece.State.ON_BOARD);

        source.add(p3);
        boolean result = board.followPath(source, 5);
        
        assertFalse(result); // grouping should not give chance to roll one more time
        assertEquals(Piece.State.OVERLAPPED, p1.getStatus());
        assertEquals(Piece.State.OVERLAPPED, p3.getStatus());
        assertEquals(p1.getPosition(), p3.getPosition());
    }

    @Test
    public void test_firstroll_forward_normal() {
        source.add(p1);

        boolean result = board.followPath(source, 0);

        assertFalse(result);
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
        assertEquals(board.getNode(1), p1.getPosition());
    }

    @Test
    public void test_firstroll_forward_grouping() {
        source.add(p1);
        board.followPath(source, 0);
        source.clear();
        
        source.add(p3);
        boolean result = board.followPath(source, 0);

        assertFalse(result);
        assertEquals(Piece.State.OVERLAPPED, p1.getStatus());
        assertEquals(Piece.State.OVERLAPPED, p3.getStatus());
        assertEquals(p1.getPosition(), p3.getPosition());
        assertEquals(board.getNode(1), p1.getPosition());
    }

    @Test
    public void test_firstroll_forward_catching() {
        source.add(p1);
        board.followPath(source, 0);
        source.clear();

        source.add(p2);
        boolean result = board.followPath(source, 0);

        assertTrue(result);
        assertEquals(Piece.State.WAITING, p1.getStatus());
        assertEquals(Piece.State.ON_BOARD, p2.getStatus());
        assertEquals(board.getNode(1), p2.getPosition());
        assertNull(p1.getPosition());
    }

    @Test
    public void test_roll_backdo_backdo() {
        source.add(p1);
        board.followPath(source, 5);
        source.clear();

        source.add(p1);
        boolean result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
        assertEquals(board.getNode(0), p1.getPosition());
    }

    @Test
    public void test_roll_backdo_normal() {
        source.add(p1);
        board.followPath(source, 1); //먼저 개로 보내고
        boolean result = board.followPath(source, 5); //백도로 돌렸을 때 위치는 도여야 함

        assertFalse(result);
        assertEquals(prevNode, p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
    }

    @Test
    public void test_roll_backdo_catching(){
        source.add(p2);
        board.followPath(source, 0); //상대편 말 도
        source.clear();

        source.add(p1);
        board.followPath(source, 1); //우리 말 개 -> 백도
        boolean result = board.followPath(source, 5);

        assertTrue(result);
        assertEquals(prevNode, p1.getPosition());
        assertEquals(null, p2.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
        assertEquals(Piece.State.WAITING, p2.getStatus());
    }

    @Test
    public void test_roll_backdo_grouping() {
        source.add(p3);
        board.followPath(source, 0); //우리편 말1 도
        source.clear();

        source.add(p1);
        board.followPath(source, 1); //우리 말 개 -> 백도
        boolean result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(prevNode, p1.getPosition());
        assertEquals(prevNode, p3.getPosition());
        assertEquals(Piece.State.OVERLAPPED, p1.getStatus());
        assertEquals(Piece.State.OVERLAPPED, p3.getStatus());
    }

    @Test
    public void test_roll_forward_normal() {
        source.add(p1);
        board.followPath(source, 0); //우선 도
        boolean result = board.followPath(source, 0); //한 번 더 도

        assertFalse(result);
        assertEquals(board.getNode(2), p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
    }

    @Test
    public void test_roll_forward_vertical() {
        source.add(p1);
        board.followPath(source, 4); //모 도
        boolean result = board.followPath(source, 0); 

        assertFalse(result);
        assertEquals(board.getNode(33), p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
    }

    @Test
    public void test_roll_forward_center() {
        source.add(p1);
        board.followPath(source, 4); //모 걸 도
        board.followPath(source, 2); 
        boolean result = board.followPath(source, 0);

        assertFalse(result);
        assertEquals(board.getNode(31), p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());

        source.clear();
        source.add(p2);
        board.followPath(source, 4); //모 모
        result = board.followPath(source, 4);

        assertFalse(result); //result가 true가 나오는 조건은 잡았을 때만
        assertEquals(board.getNode(41), p2.getPosition());
        assertEquals(Piece.State.ON_BOARD, p2.getStatus());
    }

    @Test
    public void test_roll_forward_goal() {
        
        source.add(p1);
        board.followPath(source, 4);
        board.followPath(source, 4);
        board.followPath(source, 4);
        boolean result = board.followPath(source, 1);

        assertFalse(result);
        assertEquals(null, p1.getPosition());
        assertEquals(Piece.State.FINISHED, p1.getStatus());
    }

    @Test
    public void test_roll_forward_catching() {
        source.add(p2);
        board.followPath(source, 1);
        source.clear();
        source.add(p1);
        board.followPath(source, 0);
        boolean result = board.followPath(source, 0);

        assertTrue(result);
        assertEquals(board.getNode(2), p1.getPosition());
        assertEquals(null, p2.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
        assertEquals(Piece.State.WAITING, p2.getStatus());
    }

    @Test
    public void test_roll_forward_grouping() {
        source.add(p3);
        board.followPath(source, 1);
        source.clear();
        source.add(p1);
        board.followPath(source, 0);
        boolean result = board.followPath(source, 0);
        source.clear();

        assertFalse(result);
        assertEquals(board.getNode(2), p1.getPosition());
        assertEquals(board.getNode(2), p3.getPosition());
        assertEquals(Piece.State.OVERLAPPED, p1.getStatus());
        assertEquals(Piece.State.OVERLAPPED, p3.getStatus());

        result = board.followPath(p1.getStackedPieces(), 0);           //업은 말이 같이 움직이는 지 테스트
        assertFalse(result);
        assertEquals(board.getNode(3), p1.getPosition());
        assertEquals(board.getNode(3), p3.getPosition());
        assertEquals(Piece.State.OVERLAPPED, p1.getStatus());
        assertEquals(Piece.State.OVERLAPPED, p3.getStatus());
    }


}
