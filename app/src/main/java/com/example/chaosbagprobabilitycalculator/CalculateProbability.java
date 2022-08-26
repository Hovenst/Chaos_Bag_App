package com.example.chaosbagprobabilitycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;

public class CalculateProbability extends AppCompatActivity {
    public static final String EXTRA_LIST_M = "com.example.createbagmanual.LIST";
    public static final String EXTRA_LIST_S = "com.example.createbagscenario.LIST";
    public boolean[] nonterminals = {false, false, false, false};
    public int[] symbolVals = {-1,-1,-2,-3};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_probability);

        /* Create storage object for chaos bag
            Index represents a token:
             0: star token
             1: tentacle token
             2: skull
             3: hood
             4: broken token
             5: tentacle fist
             6-15: +1..-8
             16: bless
             17: curse
            Value at each index corresponds quantity of token in bag
         */
        int[] bag = new int[18];


        // Receive intent and get chaos bag count
        // TODO: Receive intent from scenario bag create
        //    - Determine symbol values and secondary effect text
        Intent intent = getIntent();
        int[] list = intent.getIntArrayExtra((EXTRA_LIST_M));

        // Translate received list into chaos bag object
        for (int i = 0; i < 18; i++)
        {
            // Read values from received list or put 0 for bless/curse tokens
            bag[i] = i < 16 ? list[i] : 0;
        }


        /* BASIC: Display contents of bag on startup
            Translate bag (int list) to a string
            - use a dynamic package to hold tokens
            - iterate through bag
        */
        String bagString = bagToString(bag);
        TextView textView = findViewById(R.id.bagDisplay);
        textView.setText(bagString);
        /*
            Display table of probability values for the given chaos bag
            - Basic: fixed table with values from >= 20 to <= -10
            - Goals:
                - Only display value entries that have a positive probability (i.e. don't show 0%)
                - Display probability of secondary effects (from symbols)
                - Display probability of conditional modifiers (skull tokens, etc.)
                - Display probability in terms of chance to succeed, rather than for each value
                    - Maybe have a setting? Display chance of value, chance to fail, chance to succeed?
         */
        int[] bagCopy = copyOf(bag, bag.length);
        // Probability array: 31 elements
        // [0]..[19]: -20..-1, [20]:0, [21]..[30]:+1..+10
        float[] probs = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        probs = calcProb(bagCopy, probs, 0, 1);
        // displayProbsStatic(probs);

        // Programmatically populate table with probability data
        displayProbsDynamic(probs);
    }

    // Creates a string of short character sequences representing the contents of the bag
    public String bagToString(int[] bag)
    {
        String next, contents = "";
        for (int i=0; i<18; i++)
        {
            for (int j=bag[i]; j>0; j--)
            {
                next = iToT(i);
                if (i!=17 || j!=1) next = next.concat(", ");
                contents = contents.concat(next);
            }
        }
        return contents;
    }

    // Translates index of list received to parent with the symbol of the associated token
    public String iToT(int i)
    {
        String token;
        switch (i) {
            case 0: token = "St"; break;
            case 1: token = "Tn"; break;
            case 2: token = "Sk"; break;
            case 3: token = "Hd"; break;
            case 4: token = "Bt"; break;
            case 5: token = "Tf"; break;
            case 6: token = "+1"; break;
            case 7: token = "0"; break;
            case 8: token = "-1"; break;
            case 9: token = "-2"; break;
            case 10: token = "-3"; break;
            case 11: token = "-4"; break;
            case 12: token = "-5"; break;
            case 13: token = "-6"; break;
            case 14: token = "-7"; break;
            case 15: token = "-8"; break;
            case 16: token = "Bl"; break;
            case 17: token = "Cr"; break;
            // Error case
            default: token = "XX"; break;
        }
        return token;
    }
    // Translates index of chaos bag to the value of that token
    public int iToMod(int i)
    {
        if (i==0) return 1; // Default star value to +1 for now
        else if (i==1) return -30;
        else if (i>1&&i<6) return symbolVals[i-2]; // Obtain value of symbol
        else if (i>5&&i<16) return 7-i;
        else if (i==16) return 2;
        else return -2; // No default case, assumption is valid input
    }

    // Translates token modifier to index of probability array
    public int modToi(int m)
    {
        if (m<-20) return 0;
        else if (m>10) return 30;
        else return m+20;
    }


    // Function to display contents of bag
    // TODO: 2- Figure out format for displaying bag on button press
    public void displayBag()
    {
        // Create place to display bag?
        // Populate something with objects?
    }

    // TODO: Create function to update bag

    /* TODO: Function to calculate probabilities of chaos bag values
        1- Format of output? (Lists? Arrays? List depth?)
        2- Include probabilities of secondary functions

        How do we want to accumulate probabilities for nonterminals? It seems like a recursive
            function would simplify certain parts of the process, but we need to be sure we know
            how data is passed and saved between functions in java. Pointers? Nested functions
            taking accumulators and returning the modified versions?
     */

    /*
       Function is currently recursive to calculate nonterminal token values
       Note: recursive calling when there are many curse/bless tokens in the bag can take a long time.
       Any ways to tail-recur or increase efficiency would be very helpful

       TODO: How to calculate nonterminals more efficiently (count each level of depth as opposed to
        each token?)
        - Idea: Calculate all terminal values. Then, calculate nonterminals by interacting with the
            terminal probability table
     */
    public float[] calcProb(int[] bag, float[] probs, int tokenmod, float probmod)
    {
        // Maybe: Stop computing once probability to reach this depth of bag is less than 1%
        //if (probmod < 0.01) return probs;
        /* List to hold probabilities of each modifier value
            0: <= -20
            1..19: -19..-1
            20: 0
            21..29: +1..+9
            30: >= +10
         */
        float count = 0;
        int i, modIndex;

        /* TODO: Record probability of:
            - Extra failure effects from symbols
            - Unconditional symbol effects
            - Chance to see a symbol token
         */
        for (i=0;i<bag.length;i++) count += bag[i]; // Calculate number of tokens in bag

        for (i=0;i<bag.length;i++)
        {
            for (int j=bag[i];j>0;j--)
            {
                if (i!=1) { // Don't count probability of success for autofail token
                    // Check to see if symbol token says "draw another"
                    if ( ((i>1&&i<6) && nonterminals[i-2]) || i>15)
                    {
                        // TODO: Test the recursive call, haven't done that yet
                        // "Remove" token from bag copy for recursive call
                        fill(bag, i, i, (bag[i]-1));
                        /*
                        Recursive call, do a full calculation of the remaining bag, with all tokens
                         changed in value by the modifier of this nonterminal, and with probability
                         weight divided by the chance of drawing this token
                        */
                        probs = calcProb(bag, probs, iToMod(i), probmod/count);
                        // "Return" token to bag for further counting on this recursive level
                        fill(bag, i, i, (bag[i]+1));
                    } else { // Token is a terminal, add probability normally
                        modIndex = modToi(iToMod(i) + tokenmod); // Determine index of probability entry to update
                        /*
                            Probability added is the chance to draw this token divided by any modifier
                        for recursive call(s)
                        */
                        probs[modIndex] += probmod/count;
                    }
                }
            }
        }
        return probs;
    }

    /* TODO:
        - Modify values displayed to percentages
        - Change organization to be "up by" rather than by value modifier
     */
    public void displayProbsStatic(float[] ps)
    {
        /* Translate value modifier of draw into the "up by" amount required to pass
           Essentially, flip the table and accumulate from -20 to +10
         */

        TableLayout t = findViewById(R.id.tableLayout);

        // Store elements of TableView into a 2-dimensional array
        TextView[][] table = new TextView[31][3];
        for (int i = 0; i < 31; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                table[i][j] = (TextView) ((TableRow) t.getChildAt(i)).getChildAt(j);
            }
        }


        // Accumulator to add up individual probabilities, making a "chance to pass" for each element of the final table
        float acc = 0;
        boolean first = true;
        // Issue: need to show tablerow of where your chances start being 0
        // On ps, this is the index above the highest-index, nonzero-value element


        for (int i = 30; i > -1; i--)
        {
            if (ps[i] == 0)
            {
                // No change in probability for this value, hide the 3 elements of this tablerow
                table[i][0].setVisibility(View.GONE);
                table[i][1].setVisibility(View.GONE);
                table[i][2].setVisibility(View.GONE);
            }
            else {
                if (first)
                {
                    // make visible previous row
                    if (i != 30)
                    {
                        table[i+1][0].setVisibility(View.VISIBLE);
                        table[i+1][1].setVisibility(View.VISIBLE);
                        table[i+1][1].setText("% 0");
                        table[i+1][2].setVisibility(View.VISIBLE);
                    }
                    first = false;
                }
                // TODO: add 'extra effects' data
                // TODO: Make formatting line up more
                acc += ps[i];
                table[i][1].setText(String.format("%% %.0f", acc*100));
            }

        }
    }

    public void displayProbsDynamic(float [] ps)
    {
        // Get ID of tablelayout
        TableLayout t = findViewById(R.id.tableLayout);

        // accumulate values of ps so that they more closely resemble the final table
        float acc = 0;
        int first = -1;
        boolean[] nonzero = new boolean[31];
        for (int i = 30; i > -1; i--)
        {
            // Get placeholder for last element (0%) to be added to table
            if (ps[i] == 0) {
                nonzero[i] = false;
            } else {
                nonzero[i] = true;
                if (first < 0 && i < 30 && !nonzero[i+1]) { first = i+1; }
            }
            ps[i] += acc;
            acc = ps[i];
        }
        // Get format for template view
        /*  0: <= +10
            1..9: +9..+1
            10: 0
            11..29: -1..-19
            30: >= -20

         */
        String val, pass, effect;
        float den = getResources().getDisplayMetrics().density;
        for (int i = 0; i < 31; i++)
        {
            // Also print first
            if (nonzero[i] || i == first) {
                // Create formatted text to fit in the view
                // Format: +/- up/down by value, %chance to pass, %symbol fail effects
                // Value: +/-

                // TODO: create string for token fail effects
                val = String.format("%-+4d", 20-i);
                pass = String.format("%% %-4.0f", ps[i]*100);
                effect = "";

                // Create view with formatted text
                TableRow row = new TableRow(this);

                TextView value = new TextView(this);
                value.setWidth((int) (47 * den));
                value.setTextSize(20);
                value.setText(val);
                row.addView(value);

                TextView passchance = new TextView(this);
                passchance.setWidth((int) (64 * den));
                passchance.setTextSize(20);
                passchance.setText(pass);
                row.addView(passchance);

                // Add view to next position in tablelayout
                t.addView(row);
            }
        }



    }

}