//Name: Ihab Hamid
//Project: Java/Android Canoga Game
//Class: OPL
//Date: 11/15/2015
package edu.ramapo.ihab.canoga.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.ramapo.ihab.canoga.Model.Computer;
import edu.ramapo.ihab.canoga.Model.Game;
import edu.ramapo.ihab.canoga.Model.Tournament;
import edu.ramapo.ihab.canoga.R;

public class PlayActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder saveBuilder;
    Tournament currentTournament;
    Button helpButton;
    Game newGame;
    List selections = new ArrayList<Integer>();
    int numSelections;

    /**
     * On creation of this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        helpButton = (Button) findViewById(R.id.button_help);
        helpButton.setOnClickListener(helpHandler);
        currentTournament = (Tournament) getIntent().getSerializableExtra(MainActivity.EXTRA_TOURNAMENT);
        boolean isLoaded = (boolean)getIntent().getSerializableExtra(MainActivity.EXTRA_ISLOADED);

        // if it is a new game, start a new game
        if (!isLoaded) {
            newGame = new Game();
            if (currentTournament.isDiceFile()) {
                //load dice file if user requested it
                newGame.setIsDiceFile(true);
            }
            // ask for board size
            boardSizeDialog();
        }
        else if(isLoaded){
            // if loaded game, read the file in and feed it into tournament
            currentTournament = (Tournament)getIntent().getSerializableExtra(MainActivity.EXTRA_TOURNAMENT);
            newGame = new Game(currentTournament.getGame());
            if (currentTournament.isDiceFile()) {
                //load dice file
                newGame.setIsDiceFile(true);
            }
            //update buttons on board
            updateLabels();
            //proceed to play game as normal
            playRound();
        }
    }

    View.OnClickListener helpHandler = new View.OnClickListener() {
        public void onClick(View v) {
            // it was the 1st button
            // user asked for help so help them
            askHelp();
        }
    };

    /**
     * Board Size Dialog
     * prompt the user with options to choose board size
     */
    public void boardSizeDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Pick the board size");
        dialogBuilder.setItems(R.array.board_size_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                if (which == 0) {
                    // if user picks 9
                    // set game board size
                    newGame.setGameRule(9);
                    // hide buttons 10 and 11 for computer and human
                    findViewById(R.id.hum_button10).setVisibility(View.GONE);
                    findViewById(R.id.cpu_button10).setVisibility(View.GONE);
                    findViewById(R.id.cpu_button11).setVisibility(View.GONE);
                    findViewById(R.id.hum_button11).setVisibility(View.GONE);
                    // start a new round
                    newGame.setNewRound();
                    // prompt user with first turn roll dialog
                    firstTurnRollDialog();

                } else if (which == 1) {
                    // if user picks 10
                    // set game board size
                    newGame.setGameRule(10);
                    // hide buttons 11 for computer and human
                    findViewById(R.id.hum_button11).setVisibility(View.GONE);
                    findViewById(R.id.cpu_button11).setVisibility(View.GONE);
                    // show buttons 10 for computer and human
                    findViewById(R.id.hum_button10).setVisibility(View.VISIBLE);
                    findViewById(R.id.cpu_button10).setVisibility(View.VISIBLE);
                    // start a new round
                    newGame.setNewRound();
                    // prompt user with first turn roll dialog
                    firstTurnRollDialog();


                } else if (which == 2) {
                    //if user picks 11
                    // set game board size
                    newGame.setGameRule(11);
                    // show buttons 11 and 10 for both computer and human
                    findViewById(R.id.hum_button11).setVisibility(View.VISIBLE);
                    findViewById(R.id.cpu_button11).setVisibility(View.VISIBLE);
                    findViewById(R.id.cpu_button10).setVisibility(View.VISIBLE);
                    findViewById(R.id.hum_button10).setVisibility(View.VISIBLE);
                    // start a new round
                    newGame.setNewRound();
                    // prompt user with first turn roll dialog
                    firstTurnRollDialog();
                }
            }
        });
        AlertDialog dialogBoardSize = dialogBuilder.create();
        dialogBoardSize.show();
    }

    /**
     * first turn roll dialog
     * prompt user with dialog to determine who plays first
     */
    public void firstTurnRollDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("First Turn");
        dialogBuilder.setMessage("Roll the dice to determine who runs first!");
        dialogBuilder.setPositiveButton("Roll", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // run determine first player method
                TextView t = (TextView) findViewById(R.id.text_status);
                while (newGame.getPlayerDiceSum() == newGame.getComputerDiceSum()) {
                    newGame.setFirstPlayer();
                    if(newGame.getPlayerDiceSum() == newGame.getComputerDiceSum()){
                        t.append("\nBoth players rolled: "+newGame.getPlayerDiceSum() +"\nRolling again...");
                    }
                }
                Toast.makeText(getApplicationContext(), "Dice was rolled", Toast.LENGTH_SHORT).show();
                t.append("\nHuman rolled: " + newGame.getPlayerDiceSum() + ", Computer rolled: " + newGame.getComputerDiceSum());
                if (currentTournament.getHuman().isTurn()) {
                    // player going first
                    Toast.makeText(getApplicationContext(), "You are going first", Toast.LENGTH_LONG).show();
                    t.append("\nYou are going first!");
                    t = (TextView) findViewById(R.id.text_turn);
                    t.setText("Turn: Human");
                    t = (TextView) findViewById(R.id.text_roll);
                    t.setText("Roll: " + newGame.getPlayerDiceSum());
                    findViewById(R.id.button_cover).setEnabled(true);
                    findViewById(R.id.button_uncover).setEnabled(true);
                    findViewById(R.id.button_help).setEnabled(true);
                    // prompt user with roll choice (die or dice)
                    rollChoice();
                } else {
                    // computer going first
                    Toast.makeText(getApplicationContext(), "Computer is going first", Toast.LENGTH_LONG).show();
                    t.append("\nComputer is going first!");
                    disableAllCpuSquares(newGame);
                    disableAllHumSquares(newGame);
                    t = (TextView) findViewById(R.id.text_turn);
                    t.setText("Turn: Computer");
                    t = (TextView) findViewById(R.id.text_roll);
                    t.setText("Roll: " + newGame.getComputerDiceSum());
                    findViewById(R.id.button_cover).setEnabled(false);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    findViewById(R.id.button_help).setEnabled(false);
                    //start playing round
                    playRound();
                }
                updateLabels();
            }
        });
        AlertDialog dialogRollDice = dialogBuilder.create();
        dialogRollDice.show();
    }

    /**
     * save file name dialog
     * prompt user to enter save file name
     */
    public void saveFileNameDialog(){
        saveBuilder = new AlertDialog.Builder(this);
        saveBuilder.setTitle("Save file name?");
        final EditText input = new EditText(this);
        saveBuilder.setView(input);
        saveBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fileName = input.getText().toString();
                String savedGame = newGame.saveGame();
                try {
                    // get file name form the textview then save it in the app's package context
                    FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    fos.write(savedGame.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Game Saved!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        saveBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = saveBuilder.create();
        dialog.show();

    }

    /**
     * Play round
     * play round as normal by taking turns and switching back and forth between players
     * until someone wins the game
     */
    public void playRound(){
        if(checkGameOver()){
            //did someone one? if so get out
            return;
        }

        // check each player for one die mode
        newGame.checkDieMode();
        if(currentTournament.getHuman().isTurn()){
            // human's turn
            rollChoice();
            TextView t = (TextView) findViewById(R.id.text_turn);
            t.setText("Turn: Human");
        }
        else{
            // computer's turn
            TextView t = (TextView) findViewById(R.id.text_turn);
            t.setText("Turn: Computer");
            String moveKind = "";
            moveKind = newGame.playComputerMove();
            if(moveKind == "cover") {
                // cover move incoming by computer
                updateLabels();
                t = (TextView) findViewById(R.id.text_status);
                t.append("\nComputer rolled: " + newGame.getComputerDiceSum());
                t = (TextView) findViewById(R.id.text_status);
                t.append("\nComputer decided to cover own squares.\nComputer plays defensively to take control over more squares.");
                for (int i = 0; i < Computer.Moves.getCoverSet().size(); i++) {
                    if (Computer.Moves.getCoverSet().get(i) != 0) {
                        t.append("\nComputer covered square #" + Computer.Moves.getCoverSet().get(i) + " on it's own row.");
                    }
                }
                t.setMovementMethod(new ScrollingMovementMethod());
                playRound();
            }
            if(moveKind == "uncover") {
                // uncover move incoming by computer
                updateLabels();
                t = (TextView) findViewById(R.id.text_status);
                t.append("\nComputer rolled: " + newGame.getComputerDiceSum());
                t = (TextView) findViewById(R.id.text_status);
                t.append("\nComputer decided to uncover your squares.\nComputer plays aggressively to prevent you from winning");
                for (int i = 0; i < Computer.Moves.getUncoverSet().size(); i++) {
                    if (Computer.Moves.getUncoverSet().get(i) != 0) {
                        t.append("\nComputer uncovered square #" + Computer.Moves.getUncoverSet().get(i) + " on your row.");
                    }
                }
                t.setMovementMethod(new ScrollingMovementMethod());
                playRound();
            }

            if(moveKind =="nomove") {
                // no move to be made by computer
                // ran out of moves
                t = (TextView) findViewById(R.id.text_status);
                t.append("\nComputer rolled: " + newGame.getComputerDiceSum());
                t.append("\nNo move made by computer.\nEnding Turn...");

                newGame.setIsFirstPlay(false);
                updateLabels();
                // switch turns
                currentTournament.getHuman().setTurn(true);
                currentTournament.getComputer().setTurn(false);

                // prompt user to save
                if(newGame.getDidComputerMove() && newGame.getDidHumanMove()) {
                    dialogBuilder.setTitle("Save Game");
                    dialogBuilder.setMessage("Computer's turn ended. Would you like to save and quit?");
                    //create a save file dialog
                    dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            currentTournament.getHuman().setTurn(true);
                            currentTournament.getComputer().setTurn(false);

                            saveFileNameDialog();
                        }
                    });
                }

                if(!newGame.getDidHumanMove() || !newGame.getDidComputerMove()){
                    // computer's turn is over
                    dialogBuilder.setTitle("Computer's Turn ended");
                }

                dialogBuilder.setNegativeButton("Resume", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user doesnt wanna save
                        currentTournament.getHuman().setTurn(true);
                        currentTournament.getComputer().setTurn(false);
                        playRound();
                        dialog.cancel();
                    }
                });
                dialogBuilder.show();
            }
        }
    }

    /**
     * prompt user with dialog to see their roll choice
     * either die roll (when applicable) or dice roll
     */
    public void rollChoice() {
        AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(this);
        dialogBuilder2.setTitle("Your Turn!");

        // check if game has been won already
        if(checkGameOver()){
            return;
        }

        // check if user can even roll die
        newGame.checkDieMode();

        if (currentTournament.getHuman().isOneDieMode()) {
            // one die mode enabled
            dialogBuilder2.setItems(R.array.roll_choice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        // roll dice
                        newGame.rollDiceHuman();
                    } else if (which == 1) {
                        //roll one die
                        newGame.rollDieHuman();
                    }

                    TextView t = (TextView) findViewById(R.id.text_status);
                    t.append("\nYou rolled a " + newGame.getPlayerDiceSum() + ". Make a move.");

                    t = (TextView) findViewById(R.id.text_roll);
                    t.setText("Roll: " + newGame.getPlayerDiceSum());

                    makeMove();

                }
            });
        } else {
            // no one die mode
            dialogBuilder2.setPositiveButton("Roll Dice", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // roll dice
                    newGame.rollDiceHuman();
                    TextView t = (TextView) findViewById(R.id.text_status);
                    t.append("\nYou rolled a " + newGame.getPlayerDiceSum() + ". Make a move.");

                    t = (TextView) findViewById(R.id.text_roll);
                    t.setText("Roll: " + newGame.getPlayerDiceSum());
                    // apply move
                    makeMove();
                }

            });
        }
        AlertDialog dialogRollChoice = dialogBuilder2.create();
        dialogRollChoice.show();
    }

    /**
     * Map the text labels to update Turn, score, points, and wins
     */
    public void updateLabels() {

        //update human score
        TextView t = (TextView) findViewById(R.id.text_humScore);
        t.setText("Score: " + currentTournament.getHuman().getScore());

        //update human wins
        t = (TextView) findViewById(R.id.text_humWins);
        t.setText("Wins: " + currentTournament.getHuman().getWins());

        //update computer score
        t = (TextView) findViewById(R.id.text_cpuScore);
        t.setText("Score: " + currentTournament.getComputer().getScore());

        //update computer wins
        t = (TextView) findViewById(R.id.text_cpuWins);
        t.setText("Wins: " + currentTournament.getComputer().getWins());

        // map buttons
        mapButtons(newGame);

    }

    /**
     *  map buttons and update covered and uncovered squares on GUI
     * @param anyGame   game that will grab the state of squares from
     */
    public void mapButtons(Game anyGame) {
        String humButtonName = "";
        String cpuButtonName = "";

        // update board size on screen
        if(newGame.getGameRule() == 9) {
            findViewById(R.id.hum_button10).setVisibility(View.GONE);
            findViewById(R.id.hum_button11).setVisibility(View.GONE);
            findViewById(R.id.cpu_button10).setVisibility(View.GONE);
            findViewById(R.id.cpu_button11).setVisibility(View.GONE);

        }
        if(newGame.getGameRule() == 10){
            findViewById(R.id.hum_button11).setVisibility(View.GONE);
            findViewById(R.id.cpu_button11).setVisibility(View.GONE);
        }

        // initialize and update button labels
        for (int i = 1; i <= anyGame.getBoardObjet().getHumanRow().size(); i++) {
            humButtonName = "hum_button" + (i);
            cpuButtonName = "cpu_button" + (i);

            int resID = getResources().getIdentifier(humButtonName,
                    "id", getPackageName());

            if (!anyGame.getBoardObjet().getHumanRow().get(i)) {
                findViewById(resID).setBackgroundColor(Color.RED);
            }

            if (anyGame.getBoardObjet().getHumanRow().get(i)) {
                findViewById(resID).setBackgroundColor(Color.GREEN);
            }

            resID = getResources().getIdentifier(cpuButtonName,
                    "id", getPackageName());

            if (!anyGame.getBoardObjet().getComputerRow().get(i)) {
                findViewById(resID).setBackgroundColor(Color.RED);
            }

            if (anyGame.getBoardObjet().getComputerRow().get(i)) {
                findViewById(resID).setBackgroundColor(Color.GREEN);
            }
        }
    }

    /**
     * Enable or disable command buttons depending on what the player can actually do
     * This method also enables selection of up to 4 squares on one row at a time
     */
    public void makeMove() {
        // for help reasons and to see if we can cover or uncover gg
        if(checkGameOver()){
            return;
        }

        dialogBuilder.setTitle("Save Game");

        Tournament.getComputer().setBestMove(Tournament.getHuman(), Tournament.getComputer(), newGame.getGameRule(), newGame.getPlayerDiceSum(), newGame.isFirstPlay());

        TextView t = (TextView) findViewById(R.id.text_roll);
        t.setText("Roll: " + newGame.getPlayerDiceSum());

        if (Computer.Moves.getIsCoverMove() && !Computer.Moves.getIsUncoverMove()) {
            t = (TextView) findViewById(R.id.text_status);
            t.append("\nYou can only cover your board");
            findViewById(R.id.button_cover).setEnabled(true);
            findViewById(R.id.button_uncover).setEnabled(false);
            findViewById(R.id.button_help).setEnabled(true);

            disableAllCpuSquares(newGame);
            enableAllHumSquares(newGame);

        }
        if (!Computer.Moves.getIsCoverMove() && Computer.Moves.getIsUncoverMove()) {
            t = (TextView) findViewById(R.id.text_status);
            t.append("\nYou can only uncover the computers board");
            findViewById(R.id.button_cover).setEnabled(false);
            findViewById(R.id.button_uncover).setEnabled(true);
            findViewById(R.id.button_help).setEnabled(true);

            enableAllCpuSquares(newGame);
            disableAllHumSquares(newGame);

        }
        if (Computer.Moves.getIsCoverMove() && Computer.Moves.getIsUncoverMove()) {
            t = (TextView) findViewById(R.id.text_status);
            t.append("\nYou can cover or uncover.");
            findViewById(R.id.button_cover).setEnabled(true);
            findViewById(R.id.button_uncover).setEnabled(true);
            findViewById(R.id.button_help).setEnabled(true);

            enableAllCpuSquares(newGame);
            enableAllHumSquares(newGame);

        }
        if (!Computer.Moves.getIsCoverMove() && !Computer.Moves.getIsUncoverMove()) {
            Toast.makeText(getApplicationContext(), "You rolled a " + newGame.getPlayerDiceSum() + ". No more moves avaliable.", Toast.LENGTH_LONG).show();
            t = (TextView) findViewById(R.id.text_status);
            t.append("\nNo more moves avaliable.");
            newGame.setIsFirstPlay(false);
            findViewById(R.id.button_cover).setEnabled(false);
            findViewById(R.id.button_uncover).setEnabled(false);
            findViewById(R.id.button_help).setEnabled(false);

            disableAllCpuSquares(newGame);
            disableAllHumSquares(newGame);

            if(!newGame.getDidHumanMove() || !newGame.getDidComputerMove()){
                // player's turn ended but computer didn't go yet, don't prompt to save
                dialogBuilder.setTitle("Your turn ended.");
            }

            if(newGame.getDidComputerMove() && newGame.getDidHumanMove()) {
                // player's turn ended prompt to save
                dialogBuilder.setMessage("Your turn ended. Would you like to save and quit?");
                //create a save file dialog
                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // switch turns
                        currentTournament.getHuman().setTurn(false);
                        currentTournament.getComputer().setTurn(true);
                        // save here
                        saveFileNameDialog();
                    }
                });
            }
            dialogBuilder.setNegativeButton("Resume", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentTournament.getHuman().setTurn(false);
                    currentTournament.getComputer().setTurn(true);
                    playRound();
                    dialog.cancel();
                }
            });
            dialogBuilder.show();
        }
        selections.clear();
    }

    /**
     * update log to show help that the user requested
     */
    public void askHelp() {
        Tournament.getComputer().setBestMove(Tournament.getHuman(), Tournament.getComputer(), newGame.getGameRule(), newGame.getPlayerDiceSum(), newGame.isFirstPlay());
        TextView t = (TextView) findViewById(R.id.text_status);
        if (Computer.Moves.getIsCoverMove() && (Computer.Moves.getCoverSet().size() >= Computer.Moves.getUncoverSet().size())) {
            if (!Computer.Moves.getIsUncoverMove()) {
                // only cover move is available
                t.append("\nYou should cover because that's the only move available.");
            } else {
                // cover over uncover rationale
                t.append("\nYou should cover your own squares to take control of more squares on the board.");
            }
            for (int i = 0; i < Computer.Moves.getCoverSet().size(); i++) {
                // prompt what squares should be covered
                if (Computer.Moves.getCoverSet().get(i) != 0) {
                    t.append("\nYou should cover square #" + Computer.Moves.getCoverSet().get(i) + " on your row.");
                }
            }
            return;
        }

        // uncover help
        if (Computer.Moves.getIsUncoverMove() && (Computer.Moves.getUncoverSet().size() >= Computer.Moves.getCoverSet().size())) {
            if (!Computer.Moves.getIsCoverMove()) {
                // only uncover move available
                t.append("\nYou should uncover because that's the only move available.");
            } else {
                // uncover over cover rationale
                t.append("\nYou should uncover computer's squares to take control of more squares on the board.");
            }
            for (int i = 0; i < Computer.Moves.getUncoverSet().size(); i++) {
                // prompt what squares should be uncovered
                if (Computer.Moves.getUncoverSet().get(i) != 0) {
                    t.append("\nYou should uncover square #" + Computer.Moves.getUncoverSet().get(i) + " on computer's side.");
                }
            }
            return;
        }
    }

    /**
     * allows user to selected up to 4 squares on a single row at a time
     * @param view
     */
    public void markSelections(View view) {
        numSelections++;
        if (numSelections <= 4) {
            switch (view.getId()) {
                case R.id.cpu_button1:
                    // button 1 on computer row selected
                    findViewById(R.id.cpu_button1).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button1).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(1);
                    break;

                case R.id.cpu_button2:
                    // button 2 on computer row selected
                    findViewById(R.id.cpu_button2).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button2).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(2);
                    break;

                case R.id.cpu_button3:
                    // button 3 on computer row selected
                    findViewById(R.id.cpu_button3).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button3).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(3);
                    break;

                case R.id.cpu_button4:
                    // button 4 on computer row selected
                    findViewById(R.id.cpu_button4).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button4).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(4);
                    break;

                case R.id.cpu_button5:
                    // button 5 on computer row selected
                    findViewById(R.id.cpu_button5).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button5).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(5);
                    break;

                case R.id.cpu_button6:
                    // button 6 on computer row selected
                    findViewById(R.id.cpu_button6).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button6).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(6);
                    break;

                case R.id.cpu_button7:
                    // button 7 on computer row selected
                    findViewById(R.id.cpu_button7).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button7).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(7);
                    break;

                case R.id.cpu_button8:
                    // button 8 on computer row selected
                    findViewById(R.id.cpu_button8).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button8).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(8);
                    break;

                case R.id.cpu_button9:
                    // button 9 on computer row selected
                    findViewById(R.id.cpu_button9).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button9).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(9);
                    break;

                case R.id.cpu_button10:
                    // button 10 on computer row selected
                    findViewById(R.id.cpu_button10).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button10).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(10);
                    break;

                case R.id.cpu_button11:
                    // button 11 on computer row selected
                    findViewById(R.id.cpu_button11).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.cpu_button11).setClickable(false);
                    disableAllHumSquares(newGame);
                    findViewById(R.id.button_cover).setEnabled(false);
                    selections.add(11);
                    break;


                case R.id.hum_button1:
                    // button 1 on human row selected
                    findViewById(R.id.hum_button1).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button1).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(1);
                    break;

                case R.id.hum_button2:
                    // button 2 on human row selected
                    findViewById(R.id.hum_button2).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button2).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(2);
                    break;

                case R.id.hum_button3:
                    // button 3 on human row selected
                    findViewById(R.id.hum_button3).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button3).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(3);
                    break;

                case R.id.hum_button4:
                    // button 4 on human row selected
                    findViewById(R.id.hum_button4).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button4).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(4);
                    break;

                case R.id.hum_button5:
                    // button 5 on human row selected
                    findViewById(R.id.hum_button5).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button5).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(5);
                    break;

                case R.id.hum_button6:
                    // button 6 on human row selected
                    findViewById(R.id.hum_button6).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button6).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(6);
                    break;

                case R.id.hum_button7:
                    // button 7 on human row selected
                    findViewById(R.id.hum_button7).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button7).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(7);
                    break;

                case R.id.hum_button8:
                    // button 8 on human row selected
                    findViewById(R.id.hum_button8).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button8).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(8);
                    break;
                case R.id.hum_button9:
                    // button 9 on human row selected
                    findViewById(R.id.hum_button9).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button9).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(9);
                    break;
                case R.id.hum_button10:
                    // button 10 on human row selected
                    findViewById(R.id.hum_button10).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button10).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(10);
                    break;
                case R.id.hum_button11:
                    // button 11 on human row selected
                    findViewById(R.id.hum_button11).setBackgroundColor(Color.CYAN);
                    findViewById(R.id.hum_button11).setClickable(false);
                    disableAllCpuSquares(newGame);
                    findViewById(R.id.button_uncover).setEnabled(false);
                    selections.add(11);
                    break;
            }
        }
    }

    /**
     * disable all cpu squares to be selected
     * this is invoked when player selects a square on own row
     * @param anyGame   game running
     */
    public void disableAllCpuSquares(Game anyGame) {
        String cpuButtonName = "";
        for (int i = 0; i < anyGame.getBoardObjet().getHumanRow().size(); i++) {
            cpuButtonName = "cpu_button" + (i + 1);

            int resID = getResources().getIdentifier(cpuButtonName,
                    "id", getPackageName());

            findViewById(resID).setClickable(false);
        }

    }

    /**
     * enable all cpu squares to be selected
     * this is invoked when player needs to start making selections
     * @param anyGame   game running
     */
    public void enableAllCpuSquares(Game anyGame) {
        String cpuButtonName = "";
        for (int i = 0; i < anyGame.getBoardObjet().getHumanRow().size(); i++) {
            cpuButtonName = "cpu_button" + (i + 1);

            int resID = getResources().getIdentifier(cpuButtonName,
                    "id", getPackageName());

            findViewById(resID).setClickable(true);
        }

    }


    /**
     * disable all human squares to be selected
     * this is invoked when player selects a square on computer row
     * @param anyGame
     */
    public void disableAllHumSquares(Game anyGame) {
        String cpuButtonName = "";
        for (int i = 0; i < anyGame.getBoardObjet().getHumanRow().size(); i++) {
            cpuButtonName = "hum_button" + (i + 1);

            int resID = getResources().getIdentifier(cpuButtonName,
                    "id", getPackageName());

            findViewById(resID).setClickable(false);
        }

    }


    /**
     * enable all human squares to be selected
     * this is invoked when player needs to start making selections
     * @param anyGame   game running
     */
    public void enableAllHumSquares(Game anyGame) {
        String cpuButtonName = "";
        for (int i = 0; i < anyGame.getBoardObjet().getHumanRow().size(); i++) {
            cpuButtonName = "hum_button" + (i + 1);

            int resID = getResources().getIdentifier(cpuButtonName,
                    "id", getPackageName());

            findViewById(resID).setClickable(true);
        }

    }

    /**
     * get selections and apply cover operations if applicable
     * @param view  this view
     */
    public void coverSquares(View view){
        TextView t = (TextView)findViewById(R.id.text_status);
        int selectionTotal = 0;
        boolean validSquares = false;

        // if cover
        for (int i = 0; i < selections.size(); i++) {
            if (Tournament.getHuman().isCoverable(Tournament.getHuman(), (int) selections.get(i))) {
                validSquares = true;
            } else {
                Toast.makeText(getApplicationContext(), "Cannot cover square #" + selections.get(i) + " because it is already covered.\nSelect again.", Toast.LENGTH_LONG).show();
                validSquares = false;
                clearSelections();
                makeMove();
                break;
            }
        }

        if(validSquares) {
            for (int i = 0; i < selections.size(); i++) {
                selectionTotal += (int) selections.get(i);
            }
            if (selectionTotal == newGame.getPlayerDiceSum()) {
                for (int i = 0; i < selections.size(); i++) {
                    currentTournament.getHuman().setCoverSquare((int) selections.get(i));
                    t.append("\nYou covered square #" + selections.get(i));
                    newGame.setDidHumanMove(true);
                }
                clearSelections();
                rollChoice();
            } else {
                Toast.makeText(getApplicationContext(), "Sum of squares selected does not equal sum of pips. Try again!", Toast.LENGTH_LONG).show();
                clearSelections();
                makeMove();
            }
        }
        mapButtons(newGame);
    }

    /**
     * get selections and apply uncover operations if applicable
     * @param view  this view
     */
    public void uncoverSquares(View view){

        TextView t = (TextView)findViewById(R.id.text_status);
        int selectionTotal = 0;
        boolean validSquares = false;

        // if cover
        for (int i = 0; i < selections.size(); i++) {
            if (Tournament.getHuman().isUncoverable(Tournament.getComputer(), (int) selections.get(i)) && Tournament.getComputer().getAdvantage() == (int) selections.get(i) && newGame.isFirstPlay()) {
                // cant cover because its an advantage square
                validSquares = false;
                Toast.makeText(getApplicationContext(), "Cannot cover square #" + selections.get(i) + " because it is an advantage square.\nSelect again.", Toast.LENGTH_LONG).show();
                break;
            }
            if (Tournament.getHuman().isUncoverable(Tournament.getComputer(), (int) selections.get(i))) {
                validSquares = true;
            }
            else {
                // cant uncover because it is not covered
                Toast.makeText(getApplicationContext(), "Cannot uncover square #" + selections.get(i) + " because it is not covered.\nSelect again.", Toast.LENGTH_LONG).show();
                validSquares = false;
                clearSelections();
                makeMove();
                break;
            }
        }

        if(validSquares) {
            for (int i = 0; i < selections.size(); i++) {
                selectionTotal += (int) selections.get(i);
            }
            if (selectionTotal == newGame.getPlayerDiceSum()) {
                for (int i = 0; i < selections.size(); i++) {
                    // invalid sum of squares
                    currentTournament.getComputer().setUncoverSquare((int) selections.get(i));
                    t.append("\nYou uncovered square #" + selections.get(i));
                    newGame.setDidHumanMove(true);
                }
                clearSelections();
                rollChoice();
            } else {
                Toast.makeText(getApplicationContext(), "Sum of squares selected does not equal sum of pips. Try again!", Toast.LENGTH_LONG).show();
                clearSelections();
                makeMove();
            }
        }
        mapButtons(newGame);
    }

    /**
     * remove all user selections
     */
    public void clearSelections(){
        numSelections = 0;
        selections.clear();
    }

    /**
     * check if someone won the game
     * if so prompt user with details of win
     * @return
     */
    public boolean checkGameOver(){
        final AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(this);
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Game Over!");
        if(!newGame.isFirstPlay()) {
            if (newGame.checkWon()) {
                if (newGame.getWinner().getPlayerType() == "Human") {
                    //sum of all uncovered squares of computer
                    int scoreSum = 0;
                    Tournament.getHuman().setIsWon();
                    if (Tournament.getHuman().getWonBy() == "cover") {
                        for (int i = 1; i <= newGame.getGameRule(); i++) {
                            if (Tournament.getHuman().isCoverable(Tournament.getComputer(), i)) {
                                scoreSum += i;
                            }
                        }
                        Tournament.getHuman().addScore(scoreSum);
                        Tournament.getHuman().setRoundScore(scoreSum);
                        Tournament.getHuman().addWin();

                        dialogBuilder.setMessage("You won the round by covering all your squares!\nWins: " + Tournament.getHuman().getWins() + "\nScore: " + Tournament.getHuman().getScore() + "\nRound Score: " + Tournament.getHuman().getRoundScore() + "\nPlay again?");

                    } else if (Tournament.getHuman().getWonBy() == "uncover") {
                        for (int i = 1; i <= newGame.getGameRule(); i++) {
                            if (Tournament.getHuman().isUncoverable(Tournament.getHuman(), i)) {
                                scoreSum += i;
                            }
                        }
                        Tournament.getHuman().addScore(scoreSum);
                        Tournament.getHuman().setRoundScore(scoreSum);
                        Tournament.getHuman().addWin();

                        dialogBuilder.setMessage("You won the round by uncovering all the computer's squares!\nWins: " + Tournament.getHuman().getWins() + "\nScore: " + Tournament.getHuman().getScore() + "\nRound Score: " + Tournament.getHuman().getRoundScore() + "\nPlay again?");
                    }

                } else if (newGame.getWinner().getPlayerType() == "Computer") {
                    //sum of all uncovered squares of computer
                    int scoreSum = 0;
                    Tournament.getComputer().setIsWon();
                    if (Tournament.getComputer().getWonBy() == "cover") {
                        for (int i = 1; i <= newGame.getGameRule(); i++) {
                            if (Tournament.getComputer().isCoverable(Tournament.getHuman(), i)) {
                                scoreSum += i;
                            }
                        }
                        Tournament.getComputer().addScore(scoreSum);
                        Tournament.getComputer().setRoundScore(scoreSum);
                        Tournament.getComputer().addWin();
                        dialogBuilder.setMessage("Computer won the round by covering all of it's squares!\nWins: " + Tournament.getComputer().getWins() + "\nScore: " + Tournament.getComputer().getScore() + "\nRound Score: " + Tournament.getComputer().getRoundScore() + "\nPlay again?");
                    } else if (Tournament.getComputer().getWonBy() == "uncover") {
                        for (int i = 1; i <= newGame.getGameRule(); i++) {
                            if (Tournament.getComputer().isUncoverable(Tournament.getComputer(), i)) {
                                scoreSum += i;
                            }
                        }
                        Tournament.getComputer().addScore(scoreSum);
                        Tournament.getComputer().setRoundScore(scoreSum);
                        Tournament.getComputer().addWin();
                        dialogBuilder.setMessage("Computer won the round by uncovering all of your squares!\nWins: " + Tournament.getComputer().getWins() + "\nScore: " + Tournament.getComputer().getScore() + "\nRound Score: " + Tournament.getComputer().getRoundScore() + "\nPlay again?");
                    }
                }
                dialogBuilder.setPositiveButton("New Round", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // new round
                        currentTournament.handicap(newGame);
                        currentTournament.getHuman().clearFlags();
                        currentTournament.getComputer().clearFlags();

                        newGame = new Game();
                        if (currentTournament.isDiceFile()) {
                            newGame.setIsDiceFile(true);
                        }
                        boardSizeDialog();
                    }
                });

                dialogBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // announce winner!

                        dialogBuilder2.setMessage(currentTournament.displayWinner() + "\nHuman Score: " + currentTournament.getHuman().getScore() + "\nComputer Score: " + currentTournament.getComputer().getScore());
                        dialogBuilder2.setNeutralButton("Woo!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialogBuilder2.create();
                        dialogBuilder2.show();
                    }
                });
                dialogBuilder.show();
                return true;
            }
        }
        return false;
    }
}
