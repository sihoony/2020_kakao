package com.kakao.problem.distribution.domain;

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

    private static final long serialVersionUID = 2014544506L;

    public static final QDistributionReceiver distributionReceiver = new QDistributionReceiver("distributionReceiver");

    public final com.kakao.problem.assets.entity.QBaseTimeEntity _super = new com.kakao.problem.assets.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> receiverId = createNumber("receiverId", Long.class);

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

