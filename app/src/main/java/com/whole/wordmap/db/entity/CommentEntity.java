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

package com.whole.wordmap.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.whole.wordmap.model.Comment;

import java.util.Date;

@Entity(tableName = "comments",
        foreignKeys = {
                @ForeignKey(entity = LocationEntity.class,
                        parentColumns = "id",
                        childColumns = "locationId",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "locationId")
        })
public class CommentEntity implements Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int locationId;
    private String text;
    private Date postedAt;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public CommentEntity() {
    }

    public CommentEntity(int id, int locationId, String text, Date postedAt) {
        this.id = id;
        this.locationId = locationId;
        this.text = text;
        this.postedAt = postedAt;
    }
}
