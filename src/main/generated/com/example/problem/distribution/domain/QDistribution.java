package com.example.problem.distribution.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDistribution is a Querydsl query type for Distribution
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDistribution extends EntityPathBase<Distribution> {

    private static final long serialVersionUID = 271130468L;

    public static final QDistribution distribution = new QDistribution("distribution");

    public final com.example.problem.assets.entity.QBaseTimeEntity _super = new com.example.problem.assets.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> people = createNumber("people", Long.class);

    public final ListPath<DistributionReceiver, QDistributionReceiver> receivers = this.<DistributionReceiver, QDistributionReceiver>createList("receivers", DistributionReceiver.class, QDistributionReceiver.class, PathInits.DIRECT2);

    public final StringPath roomId = createString("roomId");

    public final StringPath token = createString("token");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QDistribution(String variable) {
        super(Distribution.class, forVariable(variable));
    }

    public QDistribution(Path<? extends Distribution> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDistribution(PathMetadata metadata) {
        super(Distribution.class, metadata);
    }

}

