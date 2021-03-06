/*
 * stm32WindowSensor: RF window sensors: STM32L + RFM69 + Android
 *
 * Copyright (C) 2019. Mikhail Kulesh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a copy of the GNU General
 * Public License along with this program.
 */

package com.mkulesh.znet;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ServerFragment extends BaseFragment implements View.OnClickListener
{
    public ServerFragment()
    {
        // Empty constructor required for fragment subclasses
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        initializeFragment(inflater, container, R.layout.server_fragment);

        final Button buttonServerConnect = rootView.findViewById(R.id.button_server_connect);
        buttonServerConnect.setOnClickListener(this);

        ((EditText) rootView.findViewById(R.id.field_server_name)).setText(preferences.getString(
                ServerFragment.SERVER_NAME, "supermicro"));
        ((EditText) rootView.findViewById(R.id.field_server_port)).setText(Integer.toString(preferences.getInt(
                ServerFragment.SERVER_PORT, 5017)));
        ((EditText) rootView.findViewById(R.id.field_server_password)).setText(preferences.getString(
                ServerFragment.SERVER_PASSWORD, ""));

        update();
        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.button_server_connect)
        {
            final String serverName = ((EditText) rootView.findViewById(R.id.field_server_name)).getText().toString();
            final String serverPortStr = ((EditText) rootView.findViewById(R.id.field_server_port)).getText().toString();
            final int serverPort = Integer.parseInt(serverPortStr);
            final String serverPassword = ((EditText) rootView.findViewById(R.id.field_server_password)).getText().toString();
            if (activity.connectToServer(serverName, serverPort, serverPassword))
            {
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putString(SERVER_NAME, serverName);
                prefEditor.putInt(SERVER_PORT, serverPort);
                prefEditor.putString(SERVER_PASSWORD, serverPassword);
                prefEditor.apply();
            }
        }
    }

    @Override
    public void update()
    {
        super.update();
        int line = 1;
        for (Pair<String, String> e : activity.getStateManager().getServerState())
        {
            ((TextView) rootView.findViewWithTag("SERVER_STATE_KEY_" + line)).setText(e.first);
            ((TextView) rootView.findViewWithTag("SERVER_STATE_VALUE_" + line)).setText(e.second);
            line++;
        }
    }
}
