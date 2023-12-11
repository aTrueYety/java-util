package main.minmax;

import main.input.Input;

/**
 * Four in a row game. The board is represented as a 7x6 grid with a blank character for empty, X
 * for player 1 and O for player 2.
 */
public class FourInaRow {
  private FourInaRowState state;

  public FourInaRow() {
    this.state = new FourInaRowState(0L, 0L, true);
  }

  public void start() {
    // do {
    //   System.out.println(state);
    //   if (state.is1sTurn()) {
    //     System.out.println("Player 1's turn");
    //   } else {
    //     System.out.println("Player 2's turn");
    //   }
    //   int move = Input.getInput(Input.INTEGER, Input.SCANNER, 0, 6, "Enter a column");
    //   state = state.makeMove(move);
    // } while (!state.isLeaf());
  }

}
