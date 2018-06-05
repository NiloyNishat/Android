package com.example.android.safehome.UIHandler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.safehome.R;

import java.util.Objects;


public class SettingsFragment extends Fragment {
    private View view;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.option_fragment, container, false);
        handleLinearLayout();

        return view;
    }

    private void handleLinearLayout() {
        LinearLayout linearLayout;
        linearLayout = view.findViewById(R.id.option_layout);
        linearLayout.bringToFront();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
    }

}
