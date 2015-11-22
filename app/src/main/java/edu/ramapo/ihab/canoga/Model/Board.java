//Name: Ihab Hamid
//Project: Java/Android Canoga Game
//Class: OPL
//Date: 11/15/2015
package edu.ramapo.ihab.canoga.Model;

import android.util.SparseBooleanArray;
import java.io.Serializable;

public class Board implements Serializable {
    private SparseBooleanArray m_humanRow = new SparseBooleanArray();
    private SparseBooleanArray m_computerRow = new SparseBooleanArray();
    private int m_maxSquares;


    /**
     * Class constructor
     */
    public Board(){
        setMaxSquares(0);
    }

    /**
     * Overloaded class constructor
     * @param a_numberOfRows    size of board
     */
    public Board(int a_numberOfRows)
    {
        setMaxSquares(a_numberOfRows);
        initializeRows(getMaxSquares());

    }

    /**
     * Copy constructor
     * @param anyBoard  new board
     */
    public Board(Board anyBoard){

        this.m_humanRow = anyBoard.m_humanRow;
        this.m_computerRow = anyBoard.m_computerRow;
    }

    /**
     * Check if a given square is covered
     * @param a_player  whose player's board
     * @param a_square  number of square
     * @return  true if covered, false if not
     */
    public boolean isCovered(Player a_player, int a_square)
    {
        if (a_player.getPlayerType().equalsIgnoreCase("Human")) {
            if (m_humanRow.get(a_square)) return true;
            else return false;
        }
        else {
            if (m_computerRow.get(a_square)) return true;
            else return false;
        }
    }

    /**
     * return size of board
     * @return  m_maxSquare size of board
     */
    public int getMaxSquares(){
        return m_maxSquares;
    }

    /**
     * return computer's row
     * @return  m_computerRow   computer's row of squares
     */
    public SparseBooleanArray getComputerRow() {
        return m_computerRow;
    }

    /**
     * return human's row
     * @return  m_humanRow  human's row of squares
     */
    public SparseBooleanArray getHumanRow() {
        return m_humanRow;
    }


    /**
     * Set size of board
     * @param a_maxRows new size of board
     */
    public void setMaxSquares(int a_maxRows)
    {
        m_maxSquares = a_maxRows;
    }

    /**
     * set computer's row
     * @param anyComputerRow    new computer row
     */
    public void setComputerRow(SparseBooleanArray anyComputerRow) {
        m_computerRow = anyComputerRow;
    }

    /**
     * set human's row
     * @param anyHumanRow   new human row
     */
    public void setHumanRow(SparseBooleanArray anyHumanRow) {
        m_humanRow = anyHumanRow;
    }

    /**
     * initialize both computer and human rows with false values
     * @param a_numberOfRows    size of board
     */
    public void initializeRows(int a_numberOfRows) {
        for (int i = 0; i < m_maxSquares; i++) {
            m_humanRow.put(i+1,false);
            m_computerRow.put(i+1,false);
        }
    }
}
