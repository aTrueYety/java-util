package main.minmax;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a ThreeInaRow state.
 */
public class ThreeInaRowState implements MinMax.State<ThreeInaRowState> {
  private final int[][] board;
  private final boolean isMaxTurn;

  /**
   * Creates a new ThreeInaRow state.
   *
   * @param board the 3x3 board with 0 for empty, 1 for X and 2 for O
   * @param isMaxTurn true if it is this turn
   */
  public ThreeInaRowState(int[][] board, boolean isMaxTurn) {
    if (board.length != 3 || board[0].length != 3) {
      throw new IllegalArgumentException("board must be 3x3");
    }
    this.board = board;
    this.isMaxTurn = isMaxTurn;
  }

  public int[][] getBoard() {
    return board;
  }

  @Override
  public float evaluate() {
    // rows
    for (int[] row : board) {
      if (row[0] == row[1] && row[1] == row[2] && row[0] != 0) {
        return row[0] == 1 ? 1 : -1;
      }
    }

    // columns
    for (int i = 0; i < 3; i++) {
      if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != 0) {
        return board[0][i] == 1 ? 1 : -1;
      }
    }

    // diagonals
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != 0) {
      return board[0][0] == 1 ? 1 : -1;
    }
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != 0) {
      return board[0][2] == 1 ? 1 : -1;
    }

    // no winner
    return 0;
  }

  @Override
  public boolean isLeaf() {
    if (evaluate() != 0) {
      return true;
    }
    for (int[] row : board) {
      for (int i : row) {
        if (i == 0) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean isMaxTurn() {
    return isMaxTurn;
  }

  @Override
  public List<ThreeInaRowState> getChildren() {
    List<ThreeInaRowState> children = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        // empty field
        if (board[i][j] == 0) {
          int[][] newBoard = new int[3][3];
          for (int k = 0; k < 3; k++) {
            System.arraycopy(board[k], 0, newBoard[k], 0, 3);
          }
          newBoard[i][j] = isMaxTurn ? 1 : 2;
          children.add(new ThreeInaRowState(newBoard, !isMaxTurn));
        }
      }
    }
    return children;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("-------------");
    for (int[] row : board) {
      sb.append("\n|");
      for (int i : row) {
        sb.append(i == 0 ? "   |" : i == 1 ? " X |" : " O |");
      }
      sb.append("\n-------------");
    }
    return sb.toString();
  }
}
