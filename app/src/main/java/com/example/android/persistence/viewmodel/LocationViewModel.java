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

package com.example.android.persistence.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.android.persistence.BasicApp;
import com.example.android.persistence.DataRepository;
import com.example.android.persistence.db.entity.CommentEntity;
import com.example.android.persistence.db.entity.LocationEntity;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private final LiveData<LocationEntity> mObservableLocation;

    public ObservableField<LocationEntity> location = new ObservableField<>();

    private final int mLocationId;

    private final LiveData<List<CommentEntity>> mObservableComments;

    public LocationViewModel(@NonNull Application application, DataRepository repository,
                             final int locationId) {
        super(application);
        mLocationId = locationId;

        mObservableComments = repository.loadComments(mLocationId);
        mObservableLocation = repository.loadLocation(mLocationId);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<CommentEntity>> getComments() {
        return mObservableComments;
    }

    public LiveData<LocationEntity> getObservableLocation() {
        return mObservableLocation;
    }

    public void setLocation(LocationEntity location) {
        this.location.set(location);
    }

    /**
     * A creator is used to inject the location ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the location ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mLocationId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int locationId) {
            mApplication = application;
            mLocationId = locationId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new LocationViewModel(mApplication, mRepository, mLocationId);
        }
    }
}
