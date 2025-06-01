package model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    Player player1;
    Player player2;
    Piece p1, p2;
    ArrayList<Player> players;
    Board board;

    @BeforeEach
    public void setUp() {
        // Create 1 piece each for players
        player1 = new Player(1);
        player2 = new Player(1);

        // Get pieces from players
        p1 = player1.getPiece(0);
        p2 = player2.getPiece(0);

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        board = new Board(4, players);
    }

    @Test
    public void testGetStackedPiecesWhenEmpty() {
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
    public void testFollowPath_FirstMove_Bakdo_EmptyStart() {
        p1.setStatus(Piece.State.WAITING);
        ArrayList<Piece> source = new ArrayList<>();
        source.add(p1);

        boolean result = board.followPath(source, 5); // 5 represents "bakdo"

        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
        assertEquals(Board.start, p1.getPosition());
        assertFalse(result);
    }

    @Test
    public void testFollowPath_FirstMove_Bakdo_Catching() {
        // Place p2 (opponent) on the start
        p2.setPosition(Board.start);
        p2.setStatus(Piece.State.ON_BOARD);

        p1.setStatus(Piece.State.WAITING);
        ArrayList<Piece> source = new ArrayList<>();
        source.add(p1);

        boolean result = board.followPath(source, 5); // Bakdo
        assertTrue(result); // Should have caught p2
        assertEquals(Piece.State.ON_BOARD, p1.getStatus());
        assertEquals(Piece.State.WAITING, p2.getStatus());
        assertNull(p2.getPosition());
    }

    @Test
    public void testFollowPath_FirstMove_NormalMove_Grouping() {
        // Create a new player with 2 pieces
        player1 = new Player(2);
        p1 = player1.getPiece(0);
        p2 = player1.getPiece(1);
        players = new ArrayList<>();
        players.add(player1);
        players.add(new Player(1));  // dummy opponent
        board = new Board(4, players);

        p1.setStatus(Piece.State.WAITING);
        p2.setStatus(Piece.State.WAITING);

        board.followPath(new ArrayList<>(Collections.singletonList(p1)), 1);
        board.followPath(new ArrayList<>(Collections.singletonList(p2)), 1);

        assertEquals(Piece.State.OVERLAPPED, p1.getStatus());
        assertEquals(Piece.State.OVERLAPPED, p2.getStatus());
        assertEquals(p1.getPosition(), p2.getPosition());
    }
}
