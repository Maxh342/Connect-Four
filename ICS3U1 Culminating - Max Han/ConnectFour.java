/**
* The ConnectFour class.
*
* This class represents a Connect Four (TM)
* game, which allows two players to drop
* checkers into a grid until one achieves
* four checkers in a straight line.
*/

import java.io.*;

public class ConnectFour {

   ConnectFourGUI gui;
   
   // declare all constants here
   final int EMPTY = 0; 
   final int NUMROW = 6;
   final int NUMCOL = 7;
   final int WINCOUNT = 4;
   final String GAMEFILEFOLDER = "gamefiles";
   
   // declare all "global" variables here
   int[][] board = new int [NUMROW][NUMCOL];
   int curPlayer;
   
   /* 
      Method Name: resetBoard Method
      Parameter: None 
      Return Type: None
      Purpose: Sets all values to in the array to 0 (empty)
   */
   
   public void resetBoard() {
      //Loops through the entire board
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            //Sets the slots to empty 
            board[i][j] = EMPTY; 
         }
      }
      //Resets current player and icon
      curPlayer = 1;
      gui.setNextPlayer(curPlayer);
   }
  
   /* 
      Method Name: locateEmptySlot Method
      Parameter: int colNum
      Return Type: Integer
      Purpose: Finds the bottom most empty slot in a column. If column is full method returns -1 otherwise 
      returns the row number.
   */
   
   public int locateEmptySlot (int colNum) {
      //Loops through each row
      for (int i = NUMROW - 1; i >= 0; i--) {
         if (board[i][colNum] == EMPTY) {
            //return row num
            return i;
         }
      }
      // returns -1
      return -1;
   }
   
   /* 
      Method Name: boardFull Method
      Parameter: None
      Return Type: Boolean
      Purpose: Checks to see if the entire board is full. If board is full it returns true otherwise
      returns false. 
   */
   
   public boolean boardFull () {
      //Loops through each column 
      for (int i = 0; i < NUMCOL; i++) {
         //checks to see if column is full
         if (board[0][i] == EMPTY) {
            return false;
         }
      }
      //column is full
      return true;
   }
   
   /* 
      Method Name: verticalConnect Method
      Parameter: int row, int col
      Return Type: Integer
      Purpose: Checks the number of consecutive pieces vertically formed from a given slot. The method starts
      from the given point and ONLY goes DOWN because it is the most recent piece making everything above 
      it empty. 
   */
   
   public int verticalConnect (int row, int col) {
      int count = 1; 
      int current = board[row][col];
     
      int rowNum = row + 1;
      //keeps going until reaches the edge of board or value is differenct then current
      while (rowNum < NUMROW && board[rowNum][col] == current) {
         count++;
         rowNum++;
      }
      //returns number of consecutive 
      return count; 
   }
   
   /* 
      Method Name: horizontalConnect Method
      Parameter: int row, int col
      Return Type: Integer
      Purpose: Checks the number of consecutive pieces horizontally formed from a given slot. Unlike the
      verticalConnect Method, this method checks both left and right. 
   */
   
   public int horizontalConnect (int row, int col) {
      int count = 1; 
      int current = board[row][col];
      
      //checks left from given slot
      int colNum = col - 1;
      //keeps going left until reaches the edge or the value is different 
      while (colNum >= 0 && board[row][colNum] == current) {
         count++;
         colNum--;
      }
      
      //checks right from given slot 
      colNum = col + 1;
      //keeps going right until reaches the edge or the value is different
      while (colNum < NUMCOL && board[row][colNum] == current) {
         count++;
         colNum++;
      }
      //returns number of consecutive 
      return count; 
   }

   /* 
      Method Name: diagonalConnect1 Method
      Parameter: int row, int col
      Return Type: Integer
      Purpose: Checks the number of consecutive pieces diagonally formed from a given slot. This method checks
      diagonally from top left to  bottom right and checks both ways. 
   */
   
   public int diagonalConnect1 (int row, int col) {
      int count = 1;
      int current = board[row][col];
   
      int colNum = col + 1;
      int rowNum = row + 1;
      //keeps going towards bottom right of the board and stops when the value is different or edge is reached
      while (colNum < NUMCOL && rowNum < NUMROW && board[rowNum][colNum] == current) {
         count++;
         colNum++; 
         rowNum++;
      }
      colNum = col - 1;
      rowNum = row - 1;
      
      //keeps going towards top left of the board and stops when the value is different or edge is reached
      while (colNum >= 0 && rowNum >= 0 && board[rowNum][colNum] == current) {
         count++;
         colNum--;
         rowNum--;
      }
      //returns number of consecutive 
      return count;
   }
   
   /* 
      Method Name: diagonalConnect2 Method
      Parameter: int row, int col 
      Return Type: Integer
      Purpose: Checks the number of consecutive pieces diagonally formed from a given slot. This method goes
      diagonally from top right to bottom left and check both ways.
   */
   
   public int diagonalConnect2 (int row, int col) {
      int count = 1;
      int current = board[row][col];
      
      int colNum = col - 1;
      int rowNum = row + 1;
      //keeps going to the top right of the board and stops when the value is different or edge is reached
      while (colNum >= 0 && rowNum < NUMROW && board[rowNum][colNum] == current) {
         count++;
         colNum--;
         rowNum++;
      }
      colNum = col + 1;
      rowNum = row - 1;
      
      //keeps going to the bottom left of the board and stops when the value is different or edge is reached
      while (colNum < NUMCOL && rowNum >= 0 && board[rowNum][colNum] == current) {
         count++;
         colNum++;
         rowNum--;
      }
      //returns number of consecutive 
      return count;
   }
   
   /* 
      Method Name: saveToFile Method
      Parameter: String fileName
      Return Type: Boolean
      Purpose: Checks if the game board is successfully saved to a file. The method returns true if succesful
      otherwise false.  
   */
   
   public boolean saveToFile (String fileName) {
      try {
         BufferedWriter out = new BufferedWriter (new FileWriter (GAMEFILEFOLDER + "/" + fileName));
         //writes out all the values of the board to the txt file
         for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
               out.write (board[i][j] + " ");
            }
            out.newLine();
         }
         //keeps track of next player
         out.write(curPlayer + "");
         out.close();
         return true;
      }
      catch (IOException iox) {
         return false; 
      }
   }
   
   /* 
      Method Name: loadFromFile Method
      Parameter: String fileName
      Return Type: Returns an integer
      Purpose: Checks if the game board is successfully loaded from a file. Returns true if successful
      otherwise false. 
   */
   
   public boolean loadFromFile (String fileName) {
      try {
         BufferedReader in = new BufferedReader (new FileReader (GAMEFILEFOLDER + "/" + fileName));
         //reads in all lines written on the txt file 
         for (int i = 0; i < NUMROW; i++) {
            //splits all values within the row by spaces
            String[] row = in.readLine().split(" ");
            for (int j = 0; j < NUMCOL; j++) {
                //loads values into the 2D array 
               board[i][j] = Integer.parseInt (row[j]);
            }
         }
         //retrieves next player 
         curPlayer = Integer.parseInt(in.readLine());
         //sets next player icon
         gui.setNextPlayer(curPlayer);
         in.close();
         return true; 
      }
      catch (IOException iox) {
         return false;
      }
   }
   
   public ConnectFour(ConnectFourGUI gui) {
      this.gui = gui;
      start();
   }
   
   /* 
      Method Name: play Method
      Parameter: int column
      Return Type: None
      Purpose: This method is called when a slot is clicked. This is where the logic of the game is initiated
      by calling the above methods. 
   */
   
   public void play (int column) {
      //Calls locateEmptySlot method to locate empty slot 
      int row = locateEmptySlot (column);
      //Checks if row is full or not 
      if (row != - 1) {
         board[row][column] = curPlayer; //Stores slot in board as cur player 
         gui.setPiece (row, column, curPlayer); //Places the icon 
         //Checks for winning condition
         if (verticalConnect (row, column) >= WINCOUNT || horizontalConnect (row, column) >= WINCOUNT || diagonalConnect1 (row, column) >= WINCOUNT || diagonalConnect2 (row, column) >= WINCOUNT) {
            //Displays winner message and resets board 
            gui.showWinnerMessage(curPlayer);
            gui.resetGameBoard();
            resetBoard();
         }
         //Checks for full board
         else if (boardFull()) {
            //Displays time game message and resets board 
            gui.showTieGameMessage();
            gui.resetGameBoard();
            resetBoard();
         }
         else { //No win nor tie 
            if (curPlayer == 1) { //If current player is 1
               curPlayer = 2; //Switches current player 2 to 1
               gui.setNextPlayer(curPlayer); //Switches image for next player 
            }
            else { //Current player is 2
               curPlayer = 1; //Switches current player 1 to 2
               gui.setNextPlayer(curPlayer); //Switches image for next player 
            }
         }
      }
   }
   
   /* 
      Method Name: start Method 
      Parameter: None
      Return Type: None
      Purpose: This method creates the board for the game and sets current player to player 1 because they
      start the game. 
   */
   
   public void start() {
      //Creates the array for board 
      board = new int [NUMROW][NUMCOL];
      //Sets current player to player 1
      curPlayer = 1; 
      gui.setNextPlayer(curPlayer);
   }
   
   /* 
      Method Name: updateGameBoard Method 
      Parameter: None
      Return Type: None
      Purpose: This method updates all graphics on the game board based on content in the 2D array. 
   */
   
   public void updateGameBoard() {
      //Loops through the rows 
      for (int row = 0; row < NUMROW; row++) {
         //Loops through the columns 
         for (int col = 0; col < NUMCOL; col++) {
            //Sets player slot to the board slot 
            int player = board[row][col];
            //Checks if player slot is empty
            if (player != EMPTY) {  
               //Not empty then sets piece on the board 
               gui.setPiece(row, col, player);
            }
         }
      }
   }
}