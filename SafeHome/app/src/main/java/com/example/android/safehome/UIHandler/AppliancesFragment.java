package com.example.android.safehome.UIHandler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.safehome.R;

import java.util.Objects;

public class AppliancesFragment extends Fragment {
    private View view;

    public AppliancesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appliances_fragment, container, false);

        handleArrowClick();

        return view;
    }

    private void handleArrowClick() {
        pl.droidsonroids.gif.GifImageButton gif_downArrow;
        gif_downArrow = view.findViewById(R.id.gif_down_arrow);

        gif_downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "close appliances", Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getActivity()).onBackPressed();

            }
        });

    }

}
