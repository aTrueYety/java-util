package main.minmax;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Four In a Row state. The board is represented as a 7x6 grid with a blank character
 * for empty, X for player 1 and O for player 2.
 */
public class FourInaRowState implements MinMax.State<FourInaRowState> {
  private final long board1; // bitmaps for player 1
  private final long board2; // bitmaps for player 2
  private final boolean is1sTurn;
  private final byte length;
  private final byte height;

  /**
   * Creates a new state with the given board and turn.
   *
   * @param board1 the bitmap for player 1
   * @param board2 the bitmap for player 2
   * @param is1sTurn true if it is player 1's turn
   * @param length the length of the board
   * @param height the height of the board
   * @throws IllegalArgumentException if the board is larger than 64 tiles or the length or height
   *      are less than 4
   */
  public FourInaRowState(long board1, long board2, boolean is1sTurn, byte length, byte height) 
      throws IllegalArgumentException {
    if (length * height > 64) { // TODO: maybe cast to short to avoid overflow
      throw new IllegalArgumentException("board must be 64 tiles or less");
    }
    if (length < 4 | height < 4) {
      throw new IllegalArgumentException("length and height must be grater or equal to 4");
    }
    this.board1 = board1;
    this.board2 = board2;
    this.is1sTurn = is1sTurn;
    this.length = length;
    this.height = height;
  }

  /**
   * Creates a new state with the given turn and board as a 6X7 board.
   *
   * @param board1 the bitmap for player 1
   * @param board2 the bitmap for player 2
   * @param is1sTurn true if it is player 1's turn
   */
  public FourInaRowState(long board1, long board2, boolean is1sTurn) {
    this.board1 = board1;
    this.board2 = board2;
    this.is1sTurn = is1sTurn;
    this.length = 6;
    this.height = 7;
  }

  public boolean is1sTurn() {
    return is1sTurn;
  }

  /**
   * Reads the board at the given position.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return 0 for empty, 1 for player 1 and 2 for player 2
   */
  private byte get(byte x, byte y) {
    byte one = 1;
    byte two = 2;
    byte zero = 0;
    return
          (board1 & (1L << y * length + x)) != 0 ? one
        : (board2 & (1L << y * length + x)) != 0 ? two
        : zero;
  }

  /**
   * Scans across the given range of the board with a filter.
   *
   * @param startY the start y
   * @param startX the start x
   * @param endY the end y
   * @param endX the end x
   * @param baseFilter the filter
   * @return long.MAX_VALUE if player 1 wins, long.MIN_VALUE if player 2 wins, null if no one wins
   */
  private Float scanFilter(byte startY, byte startX, byte endY, byte endX, long baseFilter) {
    for (byte y = startY; y < endY; y++) {
      for (byte x = startX; x < endX; x++) {
        long filter = (baseFilter << (y * length + x));
        if ((board1 & filter) == filter) {
          return Float.MAX_VALUE;
        }
        if ((board2 & filter) == filter) {
          return Float.MIN_VALUE;
        }
      }
    }
    return null;
  }

  @Override
  public boolean isLeaf() {
    // full board
    if ((board1 | board2) == -1L) {
      return true;
    }
    long baseFilter;
    Float res;
    // horizontal
    res = scanFilter((byte) 0, (byte) 0, height, (byte) (length - 4), 15L);
    if (res != null) {
      return true;
    }
    // vertical
    baseFilter = 1L + 1L << length + 1L << 2 * length + 1L << 3 * length;
    res = scanFilter((byte) 0, (byte) 0, (byte) (height - 4), length, baseFilter);
    if (res != null) {
      return true;
    }
    // diagonal right
    baseFilter = 1L + 1L << (length + 1) + 1L << 2 * (length + 1) + 1L << 3 * (length + 1);
    res = scanFilter((byte) 0, (byte) 0, (byte) (height - 4), (byte) (length - 4), baseFilter);
    if (res != null) {
      return true;
    }
    // diagonal left
    baseFilter = 1L << (length - 1) + 1L << (length - 1) * 2 + 1L << (length - 1) * 3;
    res = scanFilter((byte) 0, (byte) 3, (byte) (height - 4), length, baseFilter);
    if (res != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isMaxTurn() {
    return is1sTurn();
  }

  @Override
  public float evaluate() {
    // full board
    if ((board1 | board2) == -1L) {
      return 0;
    }
    long baseFilter;
    Float res;
    // horizontal
    res = scanFilter((byte) 0, (byte) 0, height, (byte) (length - 4), 15L);
    if (res != null) {
      return res;
    }
    // vertical
    baseFilter = 1L + 1L << length + 1L << 2 * length + 1L << 3 * length;
    res = scanFilter((byte) 0, (byte) 0, (byte) (height - 4), length, baseFilter);
    if (res != null) {
      return res;
    }
    // diagonal right
    baseFilter = 1L + 1L << (length + 1) + 1L << 2 * (length + 1) + 1L << 3 * (length + 1);
    res = scanFilter((byte) 0, (byte) 0, (byte) (height - 4), (byte) (length - 4), baseFilter);
    if (res != null) {
      return res;
    }
    // diagonal left
    baseFilter = 1L << (length - 1) + 1L << (length - 1) * 2 + 1L << (length - 1) * 3;
    res = scanFilter((byte) 0, (byte) 3, (byte) (height - 4), length, baseFilter);
    if (res != null) {
      return res;
    }
    return 0;
  }

  @Override
  public List<FourInaRowState> getChildren() {
    List<FourInaRowState> children = new ArrayList<>();
    for (byte x = 0; x < length; x++) {
      byte y = 0;
      while (y < height) {
        if (get(x, y) == 0) {
          children.add(
            new FourInaRowState(
              board1 | ((is1sTurn ? 1L : 0) << y * length + x),
              board2 | ((is1sTurn ? 0 : 1L) << y * length + x), 
              !is1sTurn, length, height));
          break;
        }
        ++y;
      }
    }
    return children;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(" ");
    // Header
    for (byte x = 0; x < length; x++) {
      sb.append(" " + x + "  ");
    }
    sb.append("\n");
    // each row
    for (int y = (height - 1); y >= 0; y--) {
      for (int x = 0; x < length; x++) {
        sb.append("+---");
      }
      sb.append("+\n|");
      // each tile
      for (int x = 0; x < length; x++) {
        byte tile = get((byte) x, (byte) y);
        sb.append(tile == 1 ? " X " : tile == 2 ? " O " : "   ");
        sb.append("|");
      }
      sb.append("\n");
    }
    for (int x = 0; x < length; x++) {
      sb.append("+---");
    }
    sb.append("+");
    return sb.toString();
  }
}
