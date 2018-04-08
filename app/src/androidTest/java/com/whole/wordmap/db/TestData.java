package com.whole.wordmap.db;

import com.whole.wordmap.db.entity.CommentEntity;
import com.whole.wordmap.db.entity.LocationEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Utility class that holds values to be used for testing.
 */
public class TestData {

    static final LocationEntity PRODUCT_ENTITY = new LocationEntity(1, "name", "desc",
            3);
    static final LocationEntity PRODUCT_ENTITY2 = new LocationEntity(2, "name2", "desc2",
            20);

    static final List<LocationEntity> PRODUCTS = Arrays.asList(PRODUCT_ENTITY, PRODUCT_ENTITY2);

    static final CommentEntity COMMENT_ENTITY = new CommentEntity(1, PRODUCT_ENTITY.getId(),
            "desc", new Date());
    static final CommentEntity COMMENT_ENTITY2 = new CommentEntity(2,
            PRODUCT_ENTITY2.getId(), "desc2", new Date());

    static final List<CommentEntity> COMMENTS = Arrays.asList(COMMENT_ENTITY, COMMENT_ENTITY2);


}
