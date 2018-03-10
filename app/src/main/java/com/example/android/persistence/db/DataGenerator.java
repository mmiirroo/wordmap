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

package com.example.android.persistence.db;

import com.example.android.persistence.db.entity.CommentEntity;
import com.example.android.persistence.db.entity.LocationEntity;
import com.example.android.persistence.model.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Generates data to pre-populate the database
 */
public class DataGenerator {

    private static final String[] COMMENTS = new String[]{
            "Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6"};

    public static List<LocationEntity> generateLocations() {
        List<LocationEntity> locations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LocationEntity location = new LocationEntity(i, "auto", "desc",
                    36.197164d + 0.01d * i, 120.518861d + 0.01d * i);
            locations.add(location);
        }
        return locations;
    }

    public static List<CommentEntity> generateCommentsForLocations(
            final List<LocationEntity> locations) {
        List<CommentEntity> comments = new ArrayList<>();
        Random rnd = new Random();

        for (Location location : locations) {
            int commentsNumber = rnd.nextInt(5) + 1;
            for (int i = 0; i < commentsNumber; i++) {
                CommentEntity comment = new CommentEntity();
                comment.setLocationId(location.getId());
                comment.setText(COMMENTS[i] + " for " + location.getName());
                comment.setPostedAt(new Date(System.currentTimeMillis()
                        - TimeUnit.DAYS.toMillis(commentsNumber - i) + TimeUnit.HOURS.toMillis(i)));
                comments.add(comment);
            }
        }

        return comments;
    }
}
