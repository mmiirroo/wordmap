package com.whole.wordmap;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.whole.wordmap.db.AppDatabase;
import com.whole.wordmap.db.entity.CommentEntity;
import com.whole.wordmap.db.entity.LocationEntity;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<LocationEntity>> mObservableLocations;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableLocations = new MediatorLiveData<>();

        mObservableLocations.addSource(mDatabase.locationDao().loadAllLocations(),
                locationEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableLocations.postValue(locationEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<LocationEntity>> getLocations() {
        return mObservableLocations;
    }

    public LiveData<LocationEntity> loadLocation(final int productId) {
        return mDatabase.locationDao().loadLocation(productId);
    }

    public LiveData<List<CommentEntity>> loadComments(final int productId) {
        return mDatabase.commentDao().loadComments(productId);
    }

    public void insert(LocationEntity le) {
        mDatabase.locationDao().insert(le);
    }
}
