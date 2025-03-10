package com.example.chaosbagprobabilitycalculator;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateBagManual extends AppCompatActivity {

    /* TODO: Create a way to add additional information for symbols:
        - Whether or not it's a nonterminal
        - Value modifier
        - Variable modifier, if any
        - Conditions for variable modifier
        - Secondary effects
     */

    /* CreateBagManual lets the user create a custom token bag
        - Spinners provide an interface to select token quantities
        - On hitting the create button, the spinners' contents are read saved to an arraylist
        - The arraylist is transferred to CalculateProbability activity

     */

    public static final String EXTRA_LIST = "com.example.createbagmanual.LIST";
    public Spinner[] spins = new Spinner[19];
    //public AdapterView[] avs = new AdapterView[16];
    //public int[] spinList = {0,1,2,3};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bag_manual);

        // Populate a spinner list
        for (int i = 0; i < 19; i++)
        {
            spins[i] = (Spinner) ((ViewGroup) ((ViewGroup) ((ViewGroup) findViewById(R.id.scrollView2)).getChildAt(0)).getChildAt(i)).getChildAt(0);
        }

        // Create spinner templates for different tokens
        ArrayAdapter<CharSequence> adapter3 =
                ArrayAdapter.createFromResource(this, R.array.num_array_3, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapter8 =
                ArrayAdapter.createFromResource(this, R.array.num_array_8, android.R.layout.simple_spinner_item);
        adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapter10 =
                ArrayAdapter.createFromResource(this, R.array.num_array_10, android.R.layout.simple_spinner_item);
        adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Assign spinner templates to tokens
        for (int i = 0; i < 19; i++)
        {
            if (i > 15) {
                if (i == 18) {
                    spins[i].setAdapter(adapter8); // Frost: max 8
                } else {
                    spins[i].setAdapter(adapter10); // Bless/Curse: max 10
                }
            } else {
                spins[i].setAdapter(adapter3);
            }

        }


    }

    public void createBag(View view)
    {
        //System.out.println("Bag");
        // Convert contents of text boxes into an arraylist
        String t;
        int[] nums = new int[19];
        //ArrayList<Integer> nums = new ArrayList<>(16);

        // Read the current textview in each spinner to obtain value
        for (int i = 0; i < 19; i++) {
            t = ((TextView) spins[i].getChildAt(0)).getText().toString();
            nums[i] = Integer.parseInt(t);
        }
       /*
        counts[0] = ((EditText) findViewById(R.id.manualcount1)).getText().toString();
        counts[1] = ((EditText) findViewById(R.id.manualcount2)).getText().toString();
        counts[2] = ((EditText) findViewById(R.id.manualcount3)).getText().toString();
        counts[3] = ((EditText) findViewById(R.id.manualcount4)).getText().toString();
        counts[4] = ((EditText) findViewById(R.id.manualcount5)).getText().toString();
        counts[5] = ((EditText) findViewById(R.id.manualcount6)).getText().toString();
        counts[6] = ((EditText) findViewById(R.id.manualcount7)).getText().toString();
        counts[7] = ((EditText) findViewById(R.id.manualcount8)).getText().toString();
        counts[8] = ((EditText) findViewById(R.id.manualcount9)).getText().toString();
        counts[9] = ((EditText) findViewById(R.id.manualcount10)).getText().toString();
        counts[10] = ((EditText) findViewById(R.id.manualcount11)).getText().toString();
        counts[11] = ((EditText) findViewById(R.id.manualcount12)).getText().toString();
        counts[12] = ((EditText) findViewById(R.id.manualcount13)).getText().toString();
        counts[13] = ((EditText) findViewById(R.id.manualcount14)).getText().toString();
        counts[14] = ((EditText) findViewById(R.id.manualcount15)).getText().toString();
        counts[15] = ((EditText) findViewById(R.id.manualcount16)).getText().toString();
        */

        // I don't think this is necessary, delete?
        //for (int i=0;i<16;i++) { System.out.println(counts[i]); }

        // Not sure what this is for, but is probably redundant after imp. spinners
        /*
        for (int i=0;i<16;i++)
        {
            counts[i] = counts[i].replaceAll("\\D+", "");
            if (counts[i].equals("")) counts[i] = "0";
            nums.add(i, Integer.parseInt(counts[i]));
        }
         */

        // Send ArrayList of token values in a bundle to CalculateProbability
        Intent intent = new Intent(this, CalculateProbability.class);
        intent.putExtra(EXTRA_LIST, nums);
        startActivity(intent);
    }

    private class SpinnerManual implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            // pos = index of view within the adapter, so pos should = value
            // Determine which spinner object had an item selected
            // Translate this spinner's id to nums list index
            // Update proper entry in nums list with new value
            for (int i = 0; i < 2; i++)
            {
                // Go through spinner list to find one that equals the id of "parent"?
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}



