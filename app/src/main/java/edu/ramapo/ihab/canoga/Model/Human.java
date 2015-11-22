//Name: Ihab Hamid
//Project: Java/Android Canoga Game
//Class: OPL
//Date: 11/15/2015

package edu.ramapo.ihab.canoga.Model;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Human extends Player implements Serializable{

    private static Board m_playerBoard;
    private int m_score, m_wins, m_roundScore, m_advantagePoints, m_gameRule;
    private boolean m_isTurn, m_isOneDieMode, m_isWon, m_wentFirst, m_wonByCover, m_wonByUncover;
    private String m_playerType;

    /**
     * Class Constructor
     *
     * @param playerType    type of player
     * @param m_score   player's score
     * @param m_roundScore  player's round score
     * @param m_advantagePoints player's advantage points
     * @param m_wins    player's number of wins
     * @param m_isOneDieMode    is player able to throw one die?
     * @param m_isTurn  is it player's turn?
     * @param m_isWon   did player win?
     * @param m_wentFirst   did player go first?
     * @param m_wonByCover  did player win by covering own squares?
     * @param m_wonByUncover    did player win by uncovering opponent's squares
     * @param m_diceRolls   dice roll queue read from a file
     */
    public Human(String playerType, int m_score, int m_roundScore, int m_advantagePoints, int m_wins,
                 boolean m_isOneDieMode, boolean m_isTurn, boolean m_isWon, boolean m_wentFirst,
                 boolean m_wonByCover, boolean m_wonByUncover, LinkedList<Integer> m_diceRolls){
        super(playerType, m_score, m_roundScore, m_advantagePoints, m_wins,
         m_isOneDieMode, m_isTurn, m_isWon, m_wentFirst,
         m_wonByCover, m_wonByUncover, m_diceRolls);
        setPlayerType(playerType);
        m_gameRule = 0;
    }

    /**
     * Get the number of wins for a player
     * @return m_wins   number of wins
     */
    public final int getWins()
    {
        return m_wins;
    }

    /**
     * Get the score of a player
     * @return m_score  score of a player
     */
    public final int getScore()
    {
        return m_score;
    }

    /**
     * Return number of advantage square
     * @return m_advantagePoints    advantage square
     */
    public final int getAdvantage() {
        return m_advantagePoints;
    }


    /**
     * check if player can roll one die
     * @return m_isOneDieMode   one die flag
     */
    public final boolean isOneDieMode()
    {
        if (m_isOneDieMode) return true;
        else return false;
    }


    /**
     * Check if player's turn
     * @return m_isTurn player turn flag
     */
    public final boolean isTurn()
    {
        if (m_isTurn) return true;
        else return false;
    }

    /**
     * Check if a certain square is coverable by a certain player
     * @param a_player  the player that has the row to be checked
     * @param a_square  the square to be checked
     * @return boolean  true if coverable false if not
     */
    public final boolean isCoverable(Player a_player, int a_square)
    {
        if (m_playerBoard.isCovered(a_player, a_square)) return false;
        else return true;
    }

    /**
     * Check if a certain square is uncoverable for a player
     * @param a_player  the player requesting the check
     * @param a_square  the square to be checked
     * @return  true if uncoverable false if not
     */
    public final boolean isUncoverable(Player a_player, int a_square)
    {
        if (m_playerBoard.isCovered(a_player, a_square)) return true;
        return false;
    }

    /**
     * Return the total round score
     * @return m_roundScore total round score
     */
    public final int getRoundScore()
    {
        return m_roundScore;
    }

    /**
     * check if player went first
     * @return m_wentFirst  player went first flag
     */
    public final boolean getWentFirst()
    {
        return m_wentFirst;
    }

    /**
     * check if player has won
     * @return m_isWon did player win flag
     */
    public final boolean isWon()
    {
        return m_isWon;
    }

    /**
     * return if player won by cover or uncover
     * @return "cover" if player won by covering "uncover" if player won by uncover
     */
    public final String getWonBy()
    {
        if (m_wonByCover) {
            return "cover";
        }
        else {
            return "uncover";
        }
    }

    /**
     * return player type
     * @return m_playerType player's type
     */
    public final String getPlayerType()
    {
        return m_playerType;
    }


    /**
     * get the player's board object
     * @return  m_playerBoard   the player's board
     */
    public final Board getPlayerBoard() {
        return m_playerBoard;
    }

    /**
     * set the size of the board
     * @param a_gameRule    new size of board
     */
    public void setGameRule(int a_gameRule)
    {
        m_gameRule = a_gameRule;
    }


    /**
     * set player's score
     * @param a_score   new score
     */
    public void setScore(int a_score)
    {
        m_score = a_score;
    }

    /**
     * calculate the advantage points when a player wins
     * @param a_roundScore  the sum of points of the won round
     */
    public void setAdvantage(int a_roundScore)
    {
        int advantageSum = a_roundScore;
        int sum = 0;
        do
        {
            int digit1 = advantageSum % 10;
            advantageSum /= 10;
            int digit2 = advantageSum % 10;
            advantageSum /= 10;

            sum = digit1 + digit2;
            advantageSum = sum;

            //print digit
        } while (advantageSum > 9);

        m_advantagePoints = advantageSum;
    }

    /**
     * connect a player to a board object
     * @param a_board   the board to be the end connection
     */
    public void setConnectedBoard(Board a_board)
    {
        m_playerBoard = a_board;
    }

    /**
     * turn the one die flag on or off
     * @param a_flag    true or false flag
     */
    public void setOneDieMode(boolean a_flag)
    {
        m_isOneDieMode = a_flag;
    }

    /**
     * set the player's turn flag
     * @param a_flag    true or false flag
     */
    public void setTurn(boolean a_flag)
    {
        m_isTurn = a_flag;
    }


    /**
     * set the total round score to a sepecific number
     * @param a_score   new total round score
     */
    public void setRoundScore(int a_score)
    {
        m_roundScore = a_score;
    }

    /**
     * set the number of wins for a player
     * @param a_wins    new number of wins
     */
    public void setWin(int a_wins)
    {
        m_wins = a_wins;
    }

    /**
     * increment the number of wins
     */
    public void addWin()
    {
        m_wins++;
    }

    /**
     * add a given number of points to the player's score
     * @param a_score   additional score
     */
    public void addScore(int a_score)
    {
        m_score += a_score;
    }

    /**
     * toggle the player's turn flag
     */
    public void setTurn()
    {
        m_isTurn = !m_isTurn;
    }

    /**
     * set a player to win
     */
    public void setIsWon()
    {
        m_isWon = true;
    }

    /**
     * set that a player went first
     * @param a_bool    true or false flag
     */
    public void setWentFirst(boolean a_bool)
    {
        m_wentFirst = a_bool;
    }

    /**
     * cover a given square number
     * @param a_square  given square number
     */
    public void setCoverSquare(int a_square)
    {
        if (a_square != 0) {
            if (m_playerType.equalsIgnoreCase("Human")) {
                m_playerBoard.getHumanRow().put(a_square,true);
            }
            else {
                m_playerBoard.getComputerRow().put(a_square,true);
            }
        }
    }

    /**
     * uncover a given square number
     * @param a_square  given square number
     */
    public void setUncoverSquare(int a_square)
    {
        if (a_square != 0) {
            if (m_playerType.equalsIgnoreCase("Human")) {
                m_playerBoard.getHumanRow().put(a_square,false);
            }
            else {
                m_playerBoard.getComputerRow().put(a_square,false);
            }
        }
    }

    /**
     * set that player has won by covering
     */
    public void setWonByCover()
    {
        m_wonByCover = true;
    }

    /**
     * set that a player has won by uncovering
     */
    public void setWonByUncover()
    {
        m_wonByUncover = true;
    }

    /**
     * set player type
     * @param a_playerType  string that holds player's type
     */
    public void setPlayerType(String a_playerType)
    {
        if(a_playerType.equalsIgnoreCase("Human") || a_playerType.equalsIgnoreCase("Computer"))
            m_playerType = a_playerType;
    }

    /**
     * load the file in which the dice rolls are saved
     * @param inputReader   the file that contains the dice rolls
     * @param context   activity context
     */
    public void loadDiceFile(BufferedReader inputReader,Context context)
    {

        final String[] myFiles = context.getApplicationContext().fileList();
        // Chain together various setter methods to set the dialog
        // characteristics

        try {
            //BufferedReader which will be used to read game data from.
            //BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput("dice1")));
            String input = "";
            while((input = inputReader.readLine()) != null) {
                Pattern squares = Pattern.compile("\\d+");
                Matcher findDigits = squares.matcher(input);
                int sum = 0;
                while (findDigits.find()) {
                    sum += Integer.parseInt(findDigits.group());

                }
                m_diceRolls.add(sum);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * clear the win, turn, one die mode, round score, and win type flags
     */
    public void clearFlags()
    {
        m_isOneDieMode = false;
        m_isTurn = false;
        m_isWon = false;
        m_wonByCover = false;
        m_wonByUncover = false;
        m_roundScore = 0;
    }
}
