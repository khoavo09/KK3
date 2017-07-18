package com.example.khoavo.kk3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonFragment extends Fragment {


    public ButtonFragment() {
        // Required empty public constructor
    }
    private RadioButton radioTaxButton;
    private RadioGroup TaxRadioGroup;
    private RadioButton NoTaxRadioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_button, container, false);

        Button submitButton = (Button)v.findViewById(R.id.submitB);
        TaxRadioGroup =  (RadioGroup)v.findViewById(R.id.TaxRadioButtonGroup);

        NoTaxRadioButton = (RadioButton)v.findViewById(R.id.NOradioButton);
        NoTaxRadioButton.setChecked(true);
        final Bundle bundle = new Bundle();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.myBundle.putBoolean ("START",true);
                if(NoTaxRadioButton.isChecked()) {
                    Toast.makeText(getContext(), "NOOOO", Toast.LENGTH_SHORT).show();
                    MainActivity.myBundle.putBoolean("TAX", false);
                }
                else {
                    Toast.makeText(getContext(), "YESSSS", Toast.LENGTH_SHORT).show();
                    MainActivity.myBundle.putBoolean("TAX", true);
                }

            }
        });

        return v;
    }

}
