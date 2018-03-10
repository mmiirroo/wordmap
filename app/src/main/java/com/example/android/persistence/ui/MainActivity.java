/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.persistence.R;
import com.example.android.persistence.model.Location;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Add location list fragment if this is first creation
        if (savedInstanceState == null) {
            LocationListFragment fragment = new LocationListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, LocationListFragment.TAG).commit();
        }
    }

    /** Shows the location detail fragment */
    public void show(Location location) {

        LocationFragment locationFragment = LocationFragment.forProduct(location.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("location")
                .replace(R.id.fragment_container,
                        locationFragment, null).commit();
    }
}
