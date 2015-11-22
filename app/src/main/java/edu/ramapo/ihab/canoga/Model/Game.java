//Name: Ihab Hamid
//Project: Java/Android Canoga Game
//Class: OPL
//Date: 11/15/2015
package edu.ramapo.ihab.canoga.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import edu.ramapo.ihab.canoga.Model.Computer.Moves;

public class Game  implements Serializable {
    private static Board m_board;
    private int m_playerDiceSum;
    private int m_computerDiceSum;
    private int m_gameRule;
    private boolean m_gameStarted;
    private boolean m_isWonGame;
    private boolean m_firstPlay;
    private boolean m_didHumanMove;
    private boolean m_didComputerMove;
    private boolean m_isLoaded;
    private boolean m_diceFile;
    private Player m_firstPlayer;

    /**
     * Class constructor
     */
    public Game()
    {
        m_firstPlay = true;
        m_isLoaded = false;
        m_diceFile = false;
        m_didHumanMove = false;
        m_didComputerMove = false;
    }

    /**
     * Overloaded copy constructor
     * @param anyGame   new Game to be copied
     */
    public Game(Game anyGame){
        m_board.setComputerRow(anyGame.m_board.getComputerRow());
        m_board.setHumanRow(anyGame.m_board.getHumanRow());
        m_playerDiceSum = anyGame.m_playerDiceSum;
        m_computerDiceSum = anyGame.m_playerDiceSum;
        m_gameRule = anyGame.m_gameRule;
        m_gameStarted = anyGame.m_gameStarted;
        m_isWonGame = anyGame.m_isWonGame;
        m_firstPlay = anyGame.m_firstPlay;
        m_didHumanMove = anyGame.m_didHumanMove;
        m_didComputerMove = anyGame.m_didComputerMove;
        m_isLoaded = anyGame.m_isLoaded;
        m_diceFile = anyGame.m_diceFile;
        m_firstPlayer = anyGame.m_firstPlayer;
    }

    /**
     * Get number of game board size
     * @return  m_gameRule  game board size
     */
    public int getGameRule()
    {
        return m_gameRule;
    }

    /**
     * check load dice file flag
     * @return  true if load dice file, false if not
     */
    public boolean getIsDiceFile()
    {
        return m_diceFile;
    }


    /**
     * get winner of round
     * @return  return the player that won game
     */
    public Player getWinner()
    {
        if (Tournament.getHuman().isWon()) return Tournament.getHuman();
        else return Tournament.getComputer();
    }

    /**
     * get the player who went first
     * @return  return the player that went first
     */
    public Player getFirstPlayer()
    {
        if (Tournament.getHuman().getWentFirst()) return Tournament.getHuman();
        else return Tournament.getComputer();
    }

    /**
     * get board object
     * @return m_board  game board object
     */
    public Board getBoardObjet()
    {
        return m_board;
    }

    /**
     * Check if a play won the round
     * @return  true if game is won, false if not
     */
    public boolean isWon()
    {
        if (Tournament.getHuman().isWon() || Tournament.getComputer().isWon()) return true;
        else return false;
    }

    /**
     * check if ths is the first turn being played
     * @return  true if first play, first if not
     */
    public boolean isFirstPlay()
    {
        if (m_firstPlay) return true;
        else return false;
    }

    /**
     * Get if computer moved flag
     * @return  boolean flag    if computer moved or not
     */
    public boolean getDidComputerMove(){
        return m_didComputerMove;
    }

    /**
     * Get if human moved flag
     * @return  boolean flag    if human moved or not
     */
    public boolean getDidHumanMove(){
        return m_didHumanMove;
    }

    /**
     * Get player's rolled dice sum
     * @return  m_playerDiceSum player's rolled dice sum
     */
    public int getPlayerDiceSum(){
        return m_playerDiceSum;
    }

    /**
     * Get computer's rolled dice sum
     * @return  m_computerDiceSum   computer's rolled dice sum
     */
    public int getComputerDiceSum(){
        return m_computerDiceSum;
    }

    /**
     * set load dice file flag
     * @param a_flag    true or false flag
     */
    public void setIsDiceFile(boolean a_flag)
    {
        m_diceFile = a_flag;
    }

    /**
     * set game board size
     * @param a_gameRule    new game board size
     */
    public void setGameRule(int a_gameRule)
    {
        m_gameRule = a_gameRule;
        Tournament.getHuman().setGameRule(m_gameRule);
    }

    /**
     * set player's win flag
     * @param a_player  new boolean flag
     */
    public void setWon(Player a_player)
    {
        if (a_player.getPlayerType().equalsIgnoreCase("Human"))
        {
            Tournament.getHuman().setIsWon();

        }
        if (a_player.getPlayerType().equalsIgnoreCase("Computer"))
        {
            Tournament.getComputer().setIsWon();

        }
    }

    /**
     * toggle first play flag
     */
    public void setIsFirstPlay()
    {
        m_firstPlay = !m_firstPlay;
    }

    /**
     * set first play flag
     * @param anyFlag   new boolean flag
     */
    public void setIsFirstPlay(boolean anyFlag)
    {
        m_firstPlay = anyFlag;
    }

    /**
     * set a loaded game round: players, board, game flags such as turn, first player, etc
     * @param a_computerRow computer's parsed row from serialized file
     * @param a_humanRow    human's parsed row from serialized file
     * @param anyFirstPlayer    first player
     */
    public void setLoadedRound(List<String> a_computerRow, List<String> a_humanRow, Player anyFirstPlayer) {
        m_board = new Board(m_gameRule);
        Tournament.getHuman().setConnectedBoard(m_board);
        Tournament.getComputer().setConnectedBoard(m_board);
        m_playerDiceSum = 0;
        m_computerDiceSum = 0;
        m_firstPlay = false;
        m_didHumanMove = true;
        m_didComputerMove = true;
        m_firstPlayer = anyFirstPlayer;

        if (Tournament.getHuman().getAdvantage() != 0) {
            Tournament.getHuman().setCoverSquare(Tournament.getHuman().getAdvantage());
        }
        if (Tournament.getComputer().getAdvantage() != 0) {
            Tournament.getComputer().setCoverSquare(Tournament.getComputer().getAdvantage());
        }

        for (int i = 0; i < a_computerRow.size(); i++) {
            if (a_computerRow.get(i).equalsIgnoreCase("*")) {
                Tournament.getComputer().setCoverSquare(i + 1);
            }
        }
        for (int i = 0; i < a_humanRow.size(); i++) {
            if (a_humanRow.get(i).equalsIgnoreCase("*")) {
                Tournament.getHuman().setCoverSquare(i + 1);
            }
        }
    }

    /**
     * set up new round: players, board, game flags such as turn, first player, etc.
     */
    public void setNewRound()
    {
        m_board = new Board(m_gameRule);
        Tournament.getHuman().setConnectedBoard(m_board);
        Tournament.getComputer().setConnectedBoard(m_board);
        Tournament.getHuman().addScore(0);
        Tournament.getComputer().addScore(0);
        m_playerDiceSum = 0;
        m_computerDiceSum = 0;

        if (Tournament.getHuman().getAdvantage() != 0) {
            Tournament.getHuman().setCoverSquare(Tournament.getHuman().getAdvantage());
        }
        if (Tournament.getComputer().getAdvantage() != 0) {
            Tournament.getComputer().setCoverSquare(Tournament.getComputer().getAdvantage());
        }
    }

    /**
     * set if human moved flag
     * @param anyFlag   new boolean flag
     */
    public void setDidHumanMove(boolean anyFlag){
        m_didHumanMove = anyFlag;
    }

    /**
     * set if computer moved flag
     * @param anyFlag   new boolean flag
     */
    public void setDidComputerMove(boolean anyFlag){
        m_didComputerMove = anyFlag;
    }

    /**
     * Set which player goes first
     */
    public void setFirstPlayer()
    {
            if (!m_diceFile) {
                rollDiceHuman();
                rollDiceComputer();
            } else if (m_diceFile) {
                if (Tournament.getHuman().m_diceRolls.isEmpty()) {
                    m_diceFile = false;
                } else {
                    m_playerDiceSum = Tournament.getHuman().m_diceRolls.remove();

                    m_computerDiceSum = Tournament.getHuman().m_diceRolls.remove();
                }
            }
            if (m_playerDiceSum == m_computerDiceSum) {
            }
            else if (m_playerDiceSum > m_computerDiceSum) {
                m_firstPlayer = Tournament.getHuman();
                Tournament.getHuman().setTurn();
                Tournament.getHuman().setWentFirst(true);
                Tournament.getComputer().setWentFirst(false);
            }
            else if (m_computerDiceSum > m_playerDiceSum) {
                m_firstPlayer = Tournament.getComputer();
                Tournament.getComputer().setTurn();
                Tournament.getComputer().setWentFirst(true);
                Tournament.getHuman().setWentFirst(false);
            }
    }

    /**
     * Serialize the game object's state
     * @return
     */
    public String saveGame() {
        StringBuffer savedGame = new StringBuffer();
        savedGame.append("Computer:\n");
        //Write computer squares to file
        savedGame.append("\tSquares: ");
        for (int i = 1; i <= m_gameRule; i++) {
            if (Tournament.getComputer().isCoverable(Tournament.getComputer(), i)) {
                savedGame.append(i + " ");
            }
            if (!Tournament.getComputer().isCoverable(Tournament.getComputer(), i)) {
                savedGame.append("* ");
            }
        }
        savedGame.append("\n");

        //Write computer's score
        savedGame.append("\tScore:" + Tournament.getComputer().getScore());
        savedGame.append("\n");
        savedGame.append("\n");


        //human stats
        savedGame.append("Human:\n");
        //Write human's squares
        savedGame.append("\tSquares: ");
        for (int i = 1; i <= m_gameRule; i++) {
            if (Tournament.getHuman().isCoverable(Tournament.getHuman(), i)) {
                savedGame.append(i+" ");
            }
            if (!Tournament.getHuman().isCoverable(Tournament.getHuman(), i)) {
                savedGame.append("* ");
            }
        }

        savedGame.append("\n");

        //Write human's score
        savedGame.append("\tScore:" + Tournament.getHuman().getScore());
        savedGame.append("\n");
        savedGame.append("\n");

        // save turns
        savedGame.append("First Turn: " + getFirstPlayer().getPlayerType()+"\n");
        savedGame.append("Next Turn: ");
        if (Tournament.getHuman().isTurn()) {
            savedGame.append("Human\n");
        }
        else if (Tournament.getComputer().isTurn()) {
            savedGame.append("Computer\n");
        }
        return savedGame.toString();
    }

    /**
     * set if it's a loaded game
     * @param a_flag    new is loaded boolean flag
     */
    public void setIsLoaded(boolean a_flag)
    {
        m_isLoaded = a_flag;
    }

    /**
     * Roll human dice then set sum to m_playerDiceSum
     */
    public void rollDiceHuman()
    {
        if (m_diceFile) {
            if (Tournament.getHuman().m_diceRolls.isEmpty()) {
                m_diceFile = false;
            } else {
                m_playerDiceSum = Tournament.getHuman().m_diceRolls.remove();
                return;
            }
        }
        Random rn = new Random(System.currentTimeMillis());
        int value = 0;
        int value1 = rn.nextInt(6 - 1 + 1) + 1;
        int value2 = rn.nextInt(6 - 1 + 1) + 1;
        value = value1 + value2;
        m_playerDiceSum = value;
    }

    /**
     * Roll computer dice then set sum to m_computerDiceSum
     */
    public void rollDiceComputer()
    {
        if (m_diceFile) {
            if (Tournament.getHuman().m_diceRolls.isEmpty()) {
                m_diceFile = false;
            } else {
                m_computerDiceSum = Tournament.getHuman().m_diceRolls.remove();
                return;
            }
        }
        Random rn = new Random(System.currentTimeMillis());
        int value = 0;
        int value1 = rn.nextInt(6 - 1 + 1) + 1;
        int value2 = rn.nextInt(6 - 1 + 1) + 1;
        value = value1 + value2;
        m_computerDiceSum = value;
    }

    /**
     * Roll one die for hum then set sum to m_playerDiceSum
     */
    public void rollDieHuman()
    {
        if (m_diceFile) {
            if (Tournament.getHuman().m_diceRolls.isEmpty()) {
                m_diceFile = false;
            } else {
                m_playerDiceSum = Tournament.getHuman().m_diceRolls.remove();
                return;
            }
        }
        Random rn = new Random(System.currentTimeMillis());

        int value;
        value = rn.nextInt(6 - 1 + 1) + 1;
        m_playerDiceSum = value;
    }

    /**
     * roll one die for computer and set to m_computerDiceSum
     */
    public void rollDieComputer()
    {

        if (m_diceFile) {
            if (Tournament.getHuman().m_diceRolls.isEmpty()) {
                m_diceFile = false;
            } else {
                m_computerDiceSum = Tournament.getHuman().m_diceRolls.remove();
                return;
            }
        }
        Random rn = new Random(System.currentTimeMillis());

        int value;
        value = rn.nextInt(6 - 1 + 1) + 1;
        m_computerDiceSum = value;
    }

    /**
     * Method to make computer play a turn
     * @return String   type of move made
     */
    public String playComputerMove(){
        checkDieMode();

        int squareDieCounter = 0;
        for (int i = 1; i <= 4; i++) {
            if (Tournament.getComputer().isCoverable(Tournament.getComputer(), i)) {
                squareDieCounter++;
            }
        }
        //load from dice file if activated
        if (m_diceFile) {
            if (Tournament.getHuman().m_diceRolls.isEmpty()) {
                m_diceFile = false;
            }
            else {
                m_computerDiceSum = Tournament.getHuman().m_diceRolls.remove();
            }
        }
        if (!m_diceFile) {
            if (squareDieCounter == 4 && Tournament.getComputer().isOneDieMode()) {
                rollDieComputer();
            } else {
                rollDiceComputer();
            }
        }
        Tournament.getComputer().setBestMove(Tournament.getComputer(), Tournament.getHuman(), m_gameRule, m_computerDiceSum, isFirstPlay());
        // display moves made by computer
        if (!Moves.getIsUncoverMove() && !Moves.getIsCoverMove()) {
            return "nomove";
        }
        if (Moves.getIsCoverMove() && (Moves.getCoverSet().size() >= Moves.getUncoverSet().size())) {
            for (int i = 0; i < Moves.getCoverSet().size(); i++) {
                if (Moves.getCoverSet().get(i) != 0) {
                    Tournament.getComputer().setCoverSquare(Moves.getCoverSet().get(i));
                    setDidComputerMove(true);
                }

            }
            return "cover";
        }

        if (Moves.getIsUncoverMove() && (Moves.getUncoverSet().size() >= Moves.getCoverSet().size())) {
            for (int i = 0; i < Moves.getUncoverSet().size(); i++) {
                if (Moves.getUncoverSet().get(i) != 0) {
                    Tournament.getHuman().setUncoverSquare(Moves.getUncoverSet().get(i));
                    setDidComputerMove(true);
                }
            }
            return "uncover";
        }
        return "";
    }

    /**
     * Check if game is won
     * @return  boolean flag    true if won, false if not
     */
    public boolean checkWon(){
        // check if human won
        int winHumanRowCounter = 0;
        int winComputerRowCounter = 0;
        for (int i = 1; i <= m_gameRule; i++) {
            if (m_board.getHumanRow().get(i) == true) {
                winHumanRowCounter++;
            }
            // by covering own squares
            if (winHumanRowCounter == m_gameRule) {
                setWon(Tournament.getHuman());
                Tournament.getHuman().setWonByCover();
                return true;
            }

            if (m_board.getComputerRow().get(i) == false) {
                winComputerRowCounter++;
            }
            //by uncovering computer's squares
            if (winComputerRowCounter == m_gameRule && !isFirstPlay() && m_didComputerMove) {
                setWon(Tournament.getHuman());
                Tournament.getHuman().setWonByUncover();
                return true;
            }
        }

        // check if computer won
        winHumanRowCounter = 0;
        winComputerRowCounter = 0;
        for (int i = 1; i <= m_gameRule; i++) {
            if (m_board.getHumanRow().get(i) == false) {
                winHumanRowCounter++;
            }
            // by uncovering all humans squares
            if (winHumanRowCounter == m_gameRule && !isFirstPlay() && m_didHumanMove) {
                setWon(Tournament.getComputer());
                Tournament.getComputer().setWonByUncover();
                return true;
            }
            if (m_board.getComputerRow().get(i) == true) {
                winComputerRowCounter++;
            }
            // by covering all own squares
            if (winComputerRowCounter == m_gameRule) {
                setWon(Tournament.getComputer());
                Tournament.getComputer().setWonByCover();
                return true;
            }
        }
        return false;
    }


    /**
     * Check each player if they can use one die rolls
     */
    public void checkDieMode(){

        int dieModeCounter = 0;
        for (int i = 7; i <= m_gameRule; i++) {
            if (m_board.getHumanRow().get(i) == true) {
                dieModeCounter++;
            }
        }
        if (dieModeCounter == ((m_gameRule + 1) - 7))
        {
            Tournament.getHuman().setOneDieMode(true);
        }
        else{
            Tournament.getHuman().setOneDieMode(false);
        }

        // can we use one die?
        dieModeCounter = 0;
        for (int i = 7; i <= m_gameRule; i++) {
            if (m_board.getComputerRow().get(i) == true) {
                dieModeCounter++;
            }
        }
        if (dieModeCounter == ((m_gameRule + 1) - 7))
        {
            Tournament.getComputer().setOneDieMode(true);
        }
        else{
            Tournament.getComputer().setOneDieMode(false);
        }
    }

}
