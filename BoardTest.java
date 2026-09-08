package model;

import model.Board;
import model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testInitialState() {
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
        assertEquals(Player.X, board.getCurrentTurn());
        assertNull(board.getWinner());
    }

    @Test
    public void testTurnAlternation() {
        board.mark(0, 0); // X plays
        assertEquals(Player.O, board.getCurrentTurn());

        board.mark(0, 1); // O plays
        assertEquals(Player.X, board.getCurrentTurn());
    }

    @Test
    public void testOutOfBoundsMovesAreIgnored() {
        int[][] invalidCoords = {
                {-1, 0},
                {0, -1},
                {3, 0},
                {0, 3},
                {-1, 3}
        };

        for (int[] coord : invalidCoords) {
            board.mark(coord[0], coord[1]);
            assertEquals(Player.X, board.getCurrentTurn());
            assertTrue(board.isInProgressMode());
        }
    }

    @Test
    public void testOccupiedCellMoveIgnored() {
        board.mark(0, 0); // X marks (0, 0)
        assertEquals(Player.O, board.getCurrentTurn());

        board.mark(0, 0); // O attempts same spot
        assertEquals(Player.O, board.getCurrentTurn());
    }

    @Test
    public void testHorizontalWin() {
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X wins

        assertTrue(board.isInFinishedMode());
        assertFalse(board.isInProgressMode());
        assertEquals(Player.X, board.getWinner());
    }

    @Test
    public void testVerticalWin() {
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 0); // X
        board.mark(0, 2); // O
        board.mark(2, 0); // X wins

        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, board.getWinner());
    }

    @Test
    public void testMainDiagonalWin() {
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 1); // X
        board.mark(0, 2); // O
        board.mark(2, 2); // X wins

        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, board.getWinner());
    }

    @Test
    public void testAntiDiagonalWin() {
        board.mark(0, 1); // X
        board.mark(0, 2); // O
        board.mark(1, 2); // X
        board.mark(1, 1); // O
        board.mark(2, 2); // X
        board.mark(2, 0); // O wins

        assertTrue(board.isInFinishedMode());
        assertEquals(Player.O, board.getWinner());
    }

    @Test
    public void testMovesIgnoredAfterGameOver() {
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X wins

        board.mark(2, 2); // Move attempted post-win

        assertEquals(Player.X, board.getWinner());
        assertTrue(board.isInFinishedMode());
    }

    @Test
    public void testRestartResetsBoard() {
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X wins

        board.restart();

        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
        assertNull(board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());

        // Confirm previously filled cell is cleared and playable
        board.mark(0, 0);
        assertEquals(Player.O, board.getCurrentTurn());
    }
}