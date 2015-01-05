package com.yagamo_style.densitychecker;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFragment extends Fragment {
    private MyCallbacks mCallbacks;

    public interface MyCallbacks {
        void onClickDpiCard(View v);
    }

    public MainFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof MyCallbacks) {
            this.mCallbacks = (MyCallbacks) activity;
        } else {
            throw new IllegalStateException("Activity need to implement MainFragment.MyCallbacks.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Map<String, String> map = getDeviceInfo();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.layout_cards);

        for (String key : map.keySet()) {
            View view = inflater.inflate(R.layout.card, null);
            ((TextView) view.findViewById(R.id.label_property)).setText(key);
            ((TextView) view.findViewById(R.id.text_property)).setText(map.get(key));
            linearLayout.addView(view);
        }

        return rootView;
    }

    private Map<String, String> getDeviceInfo() {
        Map<String, String> map = new LinkedHashMap<>();

        String deviceInfo = new StringBuilder(120)
                .append(Build.MANUFACTURER + " ")
                .append(Build.MODEL + "\n")
                .append("Android" + Build.VERSION.RELEASE)
                .append("(API level " + Build.VERSION.SDK_INT + ")")
                .toString();
        map.put("device", deviceInfo);

        // recognizing by suffix of values dir.
        Resources resources = getResources();

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);

        map.put("density(category)", resources.getString(R.string.density));
        map.put("density(ratio to medium)", String.valueOf(metrics.density));
        map.put("scaledDensity", String.valueOf(metrics.scaledDensity));
        map.put("densityDpi", String.valueOf(metrics.densityDpi + " dpi"));
        map.put("xdpi", String.valueOf(metrics.xdpi + " dpi"));
        map.put("ydpi", String.valueOf(metrics.ydpi + " dpi"));

        StringBuilder screenSize = new StringBuilder(resources.getString(R.string.screen_size));
        screenSize.append("(");

        // getting screen size.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1) {
            screenSize.append(display.getWidth() + " x " + display.getHeight());
        } else {
            Point size = new Point();
            display.getSize(size);
            screenSize.append(size.x + " x " + size.y);
        }

        screenSize.append(")");
        map.put("screen size", screenSize.toString());
        return map;
    }
}
