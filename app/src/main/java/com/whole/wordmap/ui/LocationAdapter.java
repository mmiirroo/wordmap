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

package com.whole.wordmap.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.whole.wordmap.R;
import com.whole.wordmap.databinding.LocationItemBinding;
import com.whole.wordmap.model.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    List<? extends Location> mLocationList;

    @Nullable
    private final LocationClickCallback mLocationClickCallback;

    public LocationAdapter(@Nullable LocationClickCallback clickCallback) {
        mLocationClickCallback = clickCallback;
    }

    public void setLocationList(final List<? extends Location> locationList) {
        if (mLocationList == null) {
            mLocationList = locationList;
            notifyItemRangeInserted(0, locationList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mLocationList.size();
                }

                @Override
                public int getNewListSize() {
                    return locationList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mLocationList.get(oldItemPosition).getId() ==
                            locationList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Location newLocation = locationList.get(newItemPosition);
                    Location oldLocation = mLocationList.get(oldItemPosition);
                    return newLocation.getId() == oldLocation.getId()
                            && newLocation.getName().equals(oldLocation.getName())
                            && (newLocation.getLatitude()- oldLocation.getLatitude()) < 0.0001d
                            && (newLocation.getLongitude()- oldLocation.getLongitude()) < 0.0001d
                            && newLocation.getDescription().equals(oldLocation.getDescription());
                }
            });
            mLocationList = locationList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LocationItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.location_item,
                        parent, false);
        binding.setCallback(mLocationClickCallback);
        return new LocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        holder.binding.setLocation(mLocationList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mLocationList == null ? 0 : mLocationList.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {

        final LocationItemBinding binding;

        public LocationViewHolder(LocationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
