package model;

import view.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PentagonBoardTest {

    Player player1;
    Player player2;
    Piece p1, p2, p3, p4;
    ArrayList<Player> players;
    Board board;
    ArrayList<Piece> source, source2;
    Node curNode, prevNode;
    UI ui = new UI();

    @BeforeEach
    public void setUp() {


        board = new Board(5);

        // Create 1 piece each for players
        player1 = new Player(1, 2, ui );
        player2 = new Player(2, 2, ui);

        // Get pieces from players
        p1 = player1.getPieces().get(0);
        p2 = player2.getPieces().get(0);
        p3 = player1.getPieces().get(1);
        p4 = player2.getPieces().get(1);

        p1.setStatus(Piece.State.WAITING);
        p2.setStatus(Piece.State.WAITING);
        p3.setStatus(Piece.State.WAITING);
        p4.setStatus(Piece.State.WAITING);

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);


        curNode = board.getNode(2);
        prevNode = board.getNode(1);

        source = new ArrayList<>();
        source2 = new ArrayList<>();
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
        source.add(p1);
        board.nodePieceMap.put(Board.start, source);
        ArrayList<Piece> result = board.getStackedPieces(Board.start);
        assertEquals(1, result.size());
        assertTrue(result.contains(p1));
    }

    @Test
    public void testGetStackedPiecesWithMultiplePieces() {
        p1.setPosition(Board.start);
        p3.setPosition(Board.start);
        p1.setStatus(Piece.State.OVERLAPPED);
        p3.setStatus(Piece.State.OVERLAPPED);
        source.add(p1);
        source.add(p3);
        board.nodePieceMap.put(Board.start, source);

        ArrayList<Piece> result = board.getStackedPieces(Board.start);
        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p3));
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
        source.add(p2);
        board.nodePieceMap.put(Board.start, source);
        
        source2.add(p1);
        boolean result = board.followPath(source2, 5); // Bakdo

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
        source.add(p1);
        board.nodePieceMap.put(Board.start, source);


        source2.add(p3);
        boolean result = board.followPath(source2, 5);
        
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
        
        source2.add(p3);
        boolean result = board.followPath(source2, 0);

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

        source2.add(p2);
        boolean result = board.followPath(source2, 0);

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

        source2.add(p1);
        board.followPath(source2, 1); //우리 말 개 -> 백도
        boolean result = board.followPath(source2, 5);

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

        source2.add(p1);
        board.followPath(source2, 1); //우리 말 개 -> 백도
        boolean result = board.followPath(source2, 5);

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
        assertEquals(board.getNode(28), p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
    }

    @Test
    public void test_roll_forward_center() {
        source.add(p1);
        board.followPath(source, 4); //모 걸 도
        board.followPath(source, 2); 
        boolean result = board.followPath(source, 0);

        assertFalse(result);
        assertEquals(board.getNode(26), p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());

        source.clear();
        source.add(p2);
        board.followPath(source, 4); //모 모
        result = board.followPath(source, 4);

        assertFalse(result); //result가 true가 나오는 조건은 잡았을 때만
        assertEquals(board.getNode(34), p2.getPosition());
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

        source2.add(p1);
        board.followPath(source2, 0);
        boolean result = board.followPath(source2, 0);

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

        source2.add(p1);
        board.followPath(source2, 0);
        boolean result = board.followPath(source2, 0);

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

    @Test
    @DisplayName("중앙 노드에 있는 말이 백도가 나왔을 때 이전에 왔던 길로 돌아가는 지")
    public void test_center_backdo() {

        //오른 쪽 위에서 온 말이 백도
        source.add(p1);
        board.followPath(source, 4);
        board.followPath(source, 2);
        boolean result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(board.getNode(29), p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());

        //위에서 온 말이 백도
        source.clear();
        source.add(p2);
        board.followPath(source, 3);
        board.followPath(source, 4);
        board.followPath(source, 0);
        board.followPath(source, 2);
        result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(board.getNode(31), p2.getPosition());
        assertEquals(Piece.State.ON_BOARD, p2.getStatus());

        //왼 쪽 위에서 온 말이 백도
        source.clear();
        source.add(p3);
        board.followPath(source, 3);
        board.followPath(source, 4);
        board.followPath(source, 4);
        board.followPath(source, 0);
        board.followPath(source, 2);
        result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(board.getNode(33), p3.getPosition());
        assertEquals(Piece.State.ON_BOARD, p3.getStatus());

    }

    @Test
    @DisplayName("왼 쪽 아래 꼭지점 노드에서 백도")
    public void test_vertical_backdo_1() {
        //경로 1 (윷 모 모 도 백도)
        source.add(p1);
        board.followPath(source, 3);
        board.followPath(source, 4);
        board.followPath(source, 4);
        board.followPath(source, 4);
        board.followPath(source, 0);
        boolean result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(board.getNode(19), p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());

        //경로 2(모 모 도 백도)

        source.clear();
        source.add(p2);
        board.followPath(source, 4);
        board.followPath(source, 4);
        board.followPath(source, 0);
        result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(board.getNode(34), p2.getPosition());
        assertEquals(Piece.State.ON_BOARD, p2.getStatus());
    }

    @Test
    @DisplayName("도착 지점에서 백도")
    public void test_vertical_backdo_2() {
        //경로 1 (모 걸 걸 백도)
        source.add(p1);
        board.followPath(source, 4);
        board.followPath(source, 2);
        board.followPath(source, 2);
        boolean result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(board.getNode(25), p1.getPosition());
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());

        //경로 2(모 모 모 도 백도)

        source.clear();
        source.add(p2);
        board.followPath(source, 4);
        board.followPath(source, 4);
        board.followPath(source, 4);
        board.followPath(source, 0);
        result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(board.getNode(24), p2.getPosition());
        assertEquals(Piece.State.ON_BOARD, p2.getStatus());
        
        //경로 3(도 백도 백도)

        source.clear();
        source.add(p3);
        board.followPath(source, 0);
        board.followPath(source, 5);
        result = board.followPath(source, 5);

        assertFalse(result);
        assertEquals(board.getNode(0), p3.getPosition());
        assertEquals(Piece.State.ON_BOARD, p3.getStatus());
    }


}
