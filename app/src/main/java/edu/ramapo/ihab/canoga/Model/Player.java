//Name: Ihab Hamid
//Project: Java/Android Canoga Game
//Class: OPL
//Date: 11/15/2015
package edu.ramapo.ihab.canoga.Model;

import android.content.Context;
import java.io.BufferedReader;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Player implements Serializable{

    // player's board
    private static Board m_playerBoard;
    private int m_score, m_wins, m_roundScore, m_advantagePoints;
    private boolean m_isTurn, m_isOneDieMode, m_isWon, m_wentFirst, m_wonByCover, m_wonByUncover;
    private String m_playerType;

    public Queue<Integer> m_diceRolls;

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
    public Player(String playerType, int m_score, int m_roundScore, int m_advantagePoints, int m_wins,
                  boolean m_isOneDieMode, boolean m_isTurn, boolean m_isWon, boolean m_wentFirst,
                  boolean m_wonByCover, boolean m_wonByUncover, LinkedList<Integer> m_diceRolls )
    {
        setPlayerType(playerType);
        this.m_score = m_score;
        this.m_roundScore = m_roundScore;
        this.m_advantagePoints = m_advantagePoints;
        this.m_wins = m_wins;
        this.m_isOneDieMode = m_isOneDieMode;
        this.m_isTurn = m_isTurn;
        this.m_isWon = m_isWon;
        this.m_wentFirst = m_wentFirst;
        this.m_wonByCover = m_wonByCover;
        this.m_wonByUncover = m_wonByUncover;
        this.m_diceRolls = m_diceRolls;
    }

    public abstract String getPlayerType();

    public abstract int getWins();

    public abstract int getScore();

    public abstract int getAdvantage();

    public abstract Board getPlayerBoard();

    public abstract int getRoundScore();

    public abstract boolean getWentFirst();

    public abstract String getWonBy();

    public abstract boolean isOneDieMode();

    public abstract boolean isWon();

    public abstract boolean isTurn();

    public abstract boolean isCoverable(Player a_player, int a_square);

    public abstract boolean isUncoverable(Player a_player, int a_square);

    public abstract void setPlayerType(String a_playerType);

    public abstract void setConnectedBoard(Board a_board);

    public abstract void setRoundScore(int a_score);

    public abstract void setOneDieMode(boolean a_flag);

    public abstract void setWin(int a_wins);

    public abstract void addWin();

    public abstract void setScore(int a_score);

    public abstract void addScore(int a_score);

    public abstract void setTurn();

    public abstract void setTurn(boolean a_flag);

    public abstract void setIsWon();

    public abstract void setWentFirst(boolean a_bool);

    public abstract void clearFlags();

    public abstract void setCoverSquare(int a_square);

    public abstract void setUncoverSquare(int a_square);

    public abstract void setWonByCover();

    public abstract void setWonByUncover();

    public abstract void setAdvantage(int a_roundScore);

    public abstract void loadDiceFile(BufferedReader inputReader,Context context);

}
