package com.example.problem.distribution.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QExpireTime is a Querydsl query type for ExpireTime
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QExpireTime extends BeanPath<ExpireTime> {

    private static final long serialVersionUID = -966875796L;

    public static final QExpireTime expireTime1 = new QExpireTime("expireTime1");

    public final DateTimePath<java.time.LocalDateTime> expireTime = createDateTime("expireTime", java.time.LocalDateTime.class);

    public QExpireTime(String variable) {
        super(ExpireTime.class, forVariable(variable));
    }

    public QExpireTime(Path<? extends ExpireTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExpireTime(PathMetadata metadata) {
        super(ExpireTime.class, metadata);
    }

}

