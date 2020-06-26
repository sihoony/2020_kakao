package com.example.problem.distribution.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDistributionReceiver is a Querydsl query type for DistributionReceiver
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDistributionReceiver extends EntityPathBase<DistributionReceiver> {

    private static final long serialVersionUID = -553924237L;

    public static final QDistributionReceiver distributionReceiver = new QDistributionReceiver("distributionReceiver");

    public final com.example.problem.assets.entity.QBaseTimeEntity _super = new com.example.problem.assets.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final EnumPath<ReceiverStatus> status = createEnum("status", ReceiverStatus.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QDistributionReceiver(String variable) {
        super(DistributionReceiver.class, forVariable(variable));
    }

    public QDistributionReceiver(Path<? extends DistributionReceiver> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDistributionReceiver(PathMetadata metadata) {
        super(DistributionReceiver.class, metadata);
    }

}

