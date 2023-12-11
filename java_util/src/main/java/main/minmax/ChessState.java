package main.minmax;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Chess state.
 */
public class ChessState implements MinMax.State<ChessState> {
  private Piece[][] board;
  private boolean isWhiteTurn;

  /**
   * Creates a new Chess state.
   */
  ChessState() {
    board = new Piece[8][8];
    isWhiteTurn = true;
    // white
    board[0][0] = new Rook(true);
    board[1][0] = new Knight(true);
    board[2][0] = new Bishop(true);
    board[3][0] = new Queen(true);
    board[4][0] = new King(true);
    board[5][0] = new Bishop(true);
    board[6][0] = new Knight(true);
    board[7][0] = new Rook(true);
    for (int i = 0; i < 8; i++) {
      board[i][1] = new Pawn(true);
    }
    // black
    board[0][7] = new Rook(false);
    board[1][7] = new Knight(false);
    board[2][7] = new Bishop(false);
    board[3][7] = new Queen(false);
    board[4][7] = new King(false);
    board[5][7] = new Bishop(false);
    board[6][7] = new Knight(false);
    board[7][7] = new Rook(false);
    for (int i = 0; i < 8; i++) {
      board[i][6] = new Pawn(false);
    }
  }

  ChessState(Piece[][] board, boolean isWhiteTurn) {
    this.board = board;
    this.isWhiteTurn = isWhiteTurn;
  }

  private boolean moveInBoard(int x, int y) {
    return x >= 0 && x < 8 && y >= 0 && y < 8;
  }

  /**
   * Makes the given move on the given board. Does not check if the move is valid or modify the
   * current board. Does not change the turn.
   *
   * @param x the x coordinate of the piece to move
   * @param y the y coordinate of the piece to move
   * @param moveX the x coordinate to move to
   * @param moveY the y coordinate to move to
   * @return the new Chess state
   */
  private ChessState makeMove(int x, int y, int moveX, int moveY) {
    Piece[][] newBoard = new Piece[8][8];
    for (int i = 0; i < 8; i++) {
      System.arraycopy(board[i], 0, newBoard[i], 0, 8);
    }
    newBoard[moveX][moveY] = newBoard[x][y];
    newBoard[x][y] = null;
    return new ChessState(newBoard, isWhiteTurn);
  }

  private void changeTurn() {
    isWhiteTurn = !isWhiteTurn;
  }

  /**
   * Gets all the moves from the given x and y in the direction of i and j. This assumes that the
   * piece at x and y is not null and the correct color to move.
   *
   * @param x starting x
   * @param y starting y
   * @param i direction x
   * @param j direction y
   * @return all the moves from the given x and y in the direction of i and j
   */
  private ArrayList<ChessState> getMovesInDirection(int x, int y, int i, int j) {
    ArrayList<ChessState> moves = new ArrayList<>();
    // start at the piece
    int moveX = x + i;
    int moveY = y + j;
    while (moveInBoard(moveX, moveY)) {
      if (board[moveX][moveY] == null) {
        // empty
        moves.add(makeMove(x, y, moveX, moveY));
        moveX += i;
        moveY += j;
      } else if (board[moveX][moveY].isWhite() != isWhiteTurn) {
        // enemy
        moves.add(makeMove(x, y, moveX, moveY));
        break;
      } else {
        // ally
        break;
      }
    }
    for (ChessState move : moves) {
      move.changeTurn();
    }
    return moves;
  }

  class Piece {
    private boolean isWhite;

    Piece(boolean isWhite) {
      this.isWhite = isWhite;
    }

    public boolean isWhite() {
      return isWhite;
    }

    ArrayList<ChessState> getMoves(int x, int y) {
      return new ArrayList<>();
    }
  }

  // TODO: implement castling, en passant, pawn promotion and check
  class King extends Piece {
    King(boolean isWhite) {
      super(isWhite);
    }

    @Override
    ArrayList<ChessState> getMoves(int x, int y) {
      ArrayList<ChessState> moves = new ArrayList<>();
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (moveInBoard(x + i, y + j) // in board
              || board[x + i][y + j] == null // empty
              || board[x + i][y + j].isWhite() != isWhite() // enemy
          ) {
            moves.add(makeMove(x, y, x + i, y + j));
          }
        }
      }
      for (ChessState move : moves) {
        move.changeTurn();
      }
      return moves;
    }
  }

  class Queen extends Piece {
    Queen(boolean isWhite) {
      super(isWhite);
    }

    @Override
    ArrayList<ChessState> getMoves(int x, int y) {
      ArrayList<ChessState> moves = new ArrayList<>();
      // all directions
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          // skip 0, 0
          if (i == 0 && j == 0) {
            continue;
          }
          moves.addAll(getMovesInDirection(x, y, i, j));
        }
      }
      for (ChessState move : moves) {
        move.changeTurn();
      }
      return moves;
    }
  }

  class Bishop extends Piece {
    Bishop(boolean isWhite) {
      super(isWhite);
    }

    @Override
    ArrayList<ChessState> getMoves(int x, int y) {
      ArrayList<ChessState> moves = new ArrayList<>();
      // diagonals
      for (int i = -1; i <= 1; i += 2) {
        for (int j = -1; j <= 1; j += 2) {
          moves.addAll(getMovesInDirection(x, y, i, j));
        }
      }
      for (ChessState move : moves) {
        move.changeTurn();
      }
      return moves;
    }
  }

  class Knight extends Piece {
    Knight(boolean isWhite) {
      super(isWhite);
    }

    @Override
    ArrayList<ChessState> getMoves(int x, int y) {
      ArrayList<ChessState> moves = new ArrayList<>();
      // all directions
      for (int i = -2; i <= 2; i++) {
        for (int j = -2; j <= 2; j++) {
          if (
              // skip diagonals and 0, 0
              (i == j || i == -j)
              // skip out of board
              || !moveInBoard(x + i, y + j)
              // skip ally
              || (board[x + i][y + j] != null && board[x + i][y + j].isWhite() == isWhite())
          ) {
            continue;
          }
          moves.add(makeMove(x, y, x + i, y + j));
        }
      }
      for (ChessState move : moves) {
        move.changeTurn();
      }
      return moves;
    }
  }

  class Rook extends Piece {
    Rook(boolean isWhite) {
      super(isWhite);
    }

    @Override
    ArrayList<ChessState> getMoves(int x, int y) {
      ArrayList<ChessState> moves = new ArrayList<>();
      // all directions
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          // skip diagonals and 0, 0
          if ((i == j || i == -j) || (i == 0 && j == 0)) {
            continue;
          }
          moves.addAll(getMovesInDirection(x, y, i, j));
        }
      }
      for (ChessState move : moves) {
        move.changeTurn();
      }
      return moves;
    }
  }

  class Pawn extends Piece {
    Pawn(boolean isWhite) {
      super(isWhite);
    }

    @Override
    ArrayList<ChessState> getMoves(int x, int y) {
      ArrayList<ChessState> moves = new ArrayList<>();
      // direction
      int dir = isWhite() ? 1 : -1;
      // move one forward
      if (moveInBoard(x, y + dir)
          && board[x][y + dir] == null // empty
      ) {
        moves.add(makeMove(x, y, x, y + dir));
      }
      // move two forward
      if (moveInBoard(x, y + 2 * dir) 
          && board[x][y + dir] == null // empty
          && board[x][y + 2 * dir] == null // empty
          && ((-dir + 1) / 2 * 8 - dir) == y // on starting row
      ) {
        moves.add(makeMove(x, y, x, y + 2 * dir));
      }
      // capture
      if (moveInBoard(x + 1, y + dir) 
          && board[x + 1][y + dir] != null // not empty
          && board[x + 1][y + dir].isWhite() != isWhite() // enemy
      ) {
        moves.add(makeMove(x, y, x + 1, y + dir));
      }
      if (moveInBoard(x - 1, y + dir) 
          && board[x - 1][y + dir] != null // not empty
          && board[x - 1][y + dir].isWhite() != isWhite() // enemy
      ) {
        moves.add(makeMove(x, y, x - 1, y + dir));
      }
      // en passant
      if (moveInBoard(x + 1, y + dir) 
          && board[x + 1][y + dir] != null // not empty
          && board[x + 1][y + dir].isWhite() != isWhite() // enemy
          && ((-dir + 1) / 2 * 8 - dir) == y // on row
      ) {
        moves.add(makeMove(x, y, x + 1, y + dir));
      }
      if (moveInBoard(x - 1, y + dir) 
          && board[x - 1][y + dir] != null // not empty
          && board[x - 1][y + dir].isWhite() != isWhite() // enemy
      ) {
        moves.add(makeMove(x, y, x - 1, y + dir));
      }
      return moves;
    }
  }

  // TODO: implement
  @Override
  public float evaluate() {
    return 0;
  }

  // TODO: implement
  @Override
  public boolean isLeaf() {
    return false;
  }

  @Override
  public boolean isMaxTurn() {
    return isWhiteTurn;
  }

  /**
   * Gets all the children of this state.
   *
   * @return all the children of this state
   */
  @Override
  public List<ChessState> getChildren() {
    ArrayList<ChessState> moves = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Piece piece = board[i][j];
        if (piece != null && piece.isWhite() == isWhiteTurn) {
          moves.addAll(piece.getMoves(i, j));
        }
      }
    }
    return moves;
  }
}