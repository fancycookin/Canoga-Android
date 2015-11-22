//Name: Ihab Hamid
//Project: Java/Android Canoga Game
//Class: OPL
//Date: 11/15/2015
package edu.ramapo.ihab.canoga.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import edu.ramapo.ihab.canoga.R;
import edu.ramapo.ihab.canoga.Model.*;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    public final static String EXTRA_TOURNAMENT = "edu.ramapo.ihab.canoga.Model.Tournament";
    public final static String EXTRA_ISLOADED = "edu.ramapo.ihab.canoga.Model.Tournament.Load";
    public boolean isLoadedGame = false;
    Tournament currentTournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTournament = new Tournament();

        Button newGame = (Button)findViewById(R.id.button_newGame);
        /**
         * OnLongClickListener
         * for when user holds the new game button
         * this will allow user to select and load a dice file
         */
        newGame.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                // loaded dice
                dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Select Dice File");
                final String[] myFiles = getApplicationContext().fileList();
                dialogBuilder.setItems(myFiles, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //BufferedReader which will be used to read game data from.
                            BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput(myFiles[which])));
                            currentTournament.setDiceFile(true);
                            currentTournament.getHuman().loadDiceFile(inputReader,MainActivity.this);
                            Toast.makeText(getApplicationContext(), "Loaded Dice!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogBuilder.create();
                dialogBuilder.show();
                return true;
            }
        });
    }

    /**
     * Called when users clicks button_newgame
     * @param view  this view
     */
    public void newGame(View view) {

        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        intent.putExtra(EXTRA_TOURNAMENT, currentTournament);
        intent.putExtra(EXTRA_ISLOADED, isLoadedGame);
        startActivity(intent);

    }

    /**
     * Dialog to enable user to select and load a game file
     * @param view  this view
     */
    public void loadGameDialog(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Load Game");
        final String[] myFiles = getApplicationContext().fileList();

        dialogBuilder.setItems(myFiles, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                try {
                    //BufferedReader which will be used to read game data from.
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput(myFiles[which])));
                    currentTournament.loadGame(inputReader);
                    isLoadedGame = true;

                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    intent.putExtra(EXTRA_TOURNAMENT, currentTournament);
                    intent.putExtra(EXTRA_ISLOADED, isLoadedGame);
                    Toast.makeText(getApplicationContext(), "File Loaded!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();
    }

    /**
     * button_deleteSave functionality
     * when user clicks the delete save file button, it is promoted to select a load/dice file
     * and delete it
     * @param view  this view
     */
    public void deleteSave(View view) {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] myFiles = getApplicationContext().fileList();
        // Chain together various setter methods to set the dialog
        // characteristics
        builder.setTitle("Delete Save Files");
        builder.setItems(myFiles,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        File dir = getFilesDir();
                        File file = new File(dir, myFiles[which]);
                        file.delete();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
