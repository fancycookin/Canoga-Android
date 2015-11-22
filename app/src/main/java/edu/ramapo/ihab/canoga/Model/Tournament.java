//Name: Ihab Hamid
//Project: Java/Android Canoga Game
//Class: OPL
//Date: 11/15/2015
package edu.ramapo.ihab.canoga.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tournament implements Serializable {
    private static Human m_human;
    private static Computer m_computer;
    private int m_rounds;
    private Game m_game;
    private boolean m_diceFile;

    /**
     * Class constructor
     */
    public Tournament() {
        m_rounds = 1;
        m_computer = new Computer("Computer",0,0,0,0, false, false, false, false, false, false, new LinkedList<Integer>());
        m_human = new Human("Human",0,0,0,0, false, false, false, false, false, false, new LinkedList<Integer>());
        m_diceFile = false;
    }

    /**
     * Return game object
     * @return  m_game  game object
     */
    public Game getGame() {
        return m_game;
    }


    /**
     * Return computer object
     * @return  m_computer  computer object
     */
    public static Computer getComputer() {
        return m_computer;
    }

    /**
     * Return human object
     * @return  m_human human object
     */
    public static Human getHuman() {
        return m_human;
    }

    /**
     * Return the number of rounds played
     * @return  m_rounds    number of rounds played
     */
    public int getRounds() {
        return m_rounds;
    }

    /**
     * check if dice file to be loaded
     * @return  m_diceFile  dice file to be loaded flag
     */
    public boolean isDiceFile() {
        return m_diceFile;
    }

    /**
     * set the dice file flag
     * @param a_flag    true or false flag
     */
    public void setDiceFile(boolean a_flag) {
        m_diceFile = a_flag;
    }

    /**
     * calculate handicap after finishing a round
     * @param newGame   the game of which the handicap will be calculated
     */
    public void handicap(Game newGame) {
        if (newGame.getWinner().getPlayerType() == "Human" && newGame.getFirstPlayer().getPlayerType() == "Human") {
            m_computer.setAdvantage(m_human.getRoundScore());
        } else if (newGame.getWinner().getPlayerType() == "Computer" && newGame.getFirstPlayer().getPlayerType() == "Computer") {
            m_human.setAdvantage(m_computer.getRoundScore());
        } else if (newGame.getWinner().getPlayerType() == "Human" && newGame.getFirstPlayer().getPlayerType() == "Computer") {
            m_human.setAdvantage(m_human.getRoundScore());
        } else if (newGame.getWinner().getPlayerType() == "Computer" && newGame.getFirstPlayer().getPlayerType() == "Human") {
            m_computer.setAdvantage(m_computer.getRoundScore());
        }
    }

    /**
     * create a string of who won the round
     * @return  String  who won the game
     */
    public String displayWinner() {
        if (m_human.getScore() > m_computer.getScore()) {
            return "Winner: Human!";
        } else if (m_computer.getScore() > m_human.getScore()) {
            return "Winner: Computer!";
        } else if (m_computer.getScore() == m_human.getScore()) {
            return "It's a draw! Noob!";
        }
        return "No win won! You broke the game!";
    }

    /**
     * Load game from a specific state
     * @param inputStream   read file to load the game from
     * @return  boolean true if successful, false if failed to load
     */
    public boolean loadGame(BufferedReader inputStream) {
        String line;
        String firstTurn;
        String nextTurn;
        List<String> computerRow = new ArrayList<String>();
        List<String> humanRow = new ArrayList<String>();
        int computerScore = 0;
        int humanScore = 0;

        try {
            // Computer label
            inputStream.readLine();

            // Computer Squares
            line = inputStream.readLine();
            Pattern squares = Pattern.compile("(\\d+|[*]|-)");
            Matcher findCompSquares = squares.matcher(line);
            while (findCompSquares.find()) {
                computerRow.add(findCompSquares.group());
            }

            // Computer Score
            line = inputStream.readLine();
            Pattern score = Pattern.compile("\\d+");
            Matcher findCompScore = score.matcher(line);
            findCompScore.find();
            computerScore = Integer.parseInt(findCompScore.group());

            //space
            inputStream.readLine();

            // Human Label
            inputStream.readLine();

            // Human Squares
            line = inputStream.readLine();
            Matcher findHumSquares = squares.matcher(line);
            while (findHumSquares.find()) {
                humanRow.add(findHumSquares.group());
            }

            // Human Score
            line = inputStream.readLine();
            Matcher findHumScore = score.matcher(line);
            findHumScore.find();
            humanScore = Integer.parseInt(findHumScore.group());

            // Space
            inputStream.readLine();

            // First Turn
            line = inputStream.readLine();
            Pattern findName = Pattern.compile("Human|Computer");
            Matcher findFirstTurn = findName.matcher(line);
            findFirstTurn.find();
            firstTurn = findFirstTurn.group();

            // Next Turn
            line = inputStream.readLine();
            Matcher findNextTurn = findName.matcher(line);
            findNextTurn.find();
            nextTurn = findNextTurn.group();

            m_computer.setScore(computerScore);
            m_human.setScore(humanScore);
            m_human.setGameRule(humanRow.size());

            m_game = new Game();
            m_game.setIsLoaded(true);
            m_game.setGameRule(humanRow.size());

            if (firstTurn.equalsIgnoreCase("Computer")) {
                m_computer.setWentFirst(true);
                m_human.setWentFirst(false);
                m_game.setLoadedRound(computerRow, humanRow, m_computer);
            }
            if (firstTurn.equalsIgnoreCase("Human")) {
                m_computer.setWentFirst(false);
                m_human.setWentFirst(true);
                m_game.setLoadedRound(computerRow, humanRow, m_human);
            }
            if (nextTurn.equalsIgnoreCase("Computer")) {
                m_computer.setTurn(true);
                m_human.setTurn(false);
            }
            if (nextTurn.equalsIgnoreCase("Human")) {
                m_computer.setTurn(false);
                m_human.setTurn(true);
            }

            if (m_diceFile) {
                m_game.setIsDiceFile(true);
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
