drop table if exists control_bayonet_mid;

drop table if exists control_library_mid;

drop table if exists control_task;

drop table if exists face_bayonet;

drop table if exists face_library;

drop table if exists portrait;

drop table if exists warning_information;

create table control_bayonet_mid
(
   id                   bigint(32) not null comment 'id',
   id_control_task      bigint(32) comment '布控id',
   id_face_bayonet      bigint(32) comment '摄像头id',
   is_valid             varchar(1) comment '是否有效 Y有效 N无效',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   create_by            varchar(32) comment '创建人',
   update_by            varchar(32) comment '更新人',
   primary key (id)
);

alter table control_bayonet_mid comment '布控和摄像头中间表';

create table control_library_mid
(
   id                   bigint(32) not null comment 'id',
   id_control_task      bigint(32) comment '布控id',
   id_factory           bigint(32) comment '库id',
   is_valid             varchar(1) comment '是否有效 Y有效 N无效',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   update_by            varchar(32) comment '更新人',
   create_by            varchar(32) comment '创建人',
   primary key (id)
);

alter table control_library_mid comment '布控和库中间表';

create table control_task
(
   id_control_task      bigint(32) character set utf8 not null comment 'id',
   control_threshold    double default NULL comment '布控阈值',
   task_name            varchar(255) character set utf8 comment '布控名称',
   remarks              text character set utf8 comment '备注信息',
   extra_meta           text comment '额外信息',
   is_valid             varchar(1) character set utf8 comment '是否有效 Y有效 N无效',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   create_by            varchar(32) character set utf8 comment '创建人',
   update_by            varchar(32) character set utf8 comment '更新人',
   primary key (id_control_task)
)
ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '布控任务' ROW_FORMAT = Dynamic;

alter table control_task comment '布控表';

create table face_bayonet
(
   id_face_bayonet      bigint(32) character set utf8 not null comment 'id',
   bayonet_name         varchar(12) character set utf8 comment '摄像头名称',
   bayonet_address      varchar(255) character set utf8 comment '摄像头地址',
   bayonet_type         varchar(100) character set utf8 comment '摄像头类型',
   remarks              text character set utf8 comment '备注',
   extra_meta           text comment '额外信息',
   is_valid             varchar(1) character set utf8 comment '是否有效 Y有效 N无效',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   create_by            varchar(32) character set utf8 comment '创建人',
   update_by            varchar(32) character set utf8 comment '更新人',
   primary key (id_face_bayonet)
)
ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '卡口表' ROW_FORMAT = Dynamic;

alter table face_bayonet comment '摄像头表';

create table face_library
(
   id_factory           bigint(32) character set utf8 not null comment 'id',
   factory_name         varchar(255) character set utf8 comment '库名称',
   factory_type         varchar(2) character set utf8 comment '库类型',
   remarks              varchar(255) character set utf8 comment '备注',
   extra_meta           text comment '额外信息',
   is_valid             varchar(1) character set utf8 comment '是否有效 Y有效 N无效',
   create_time          datetime comment '创建时间',
   create_by            varchar(32) character set utf8 comment '创建人',
   update_time          datetime comment '更新时间',
   update_by            varchar(32) character set utf8 comment '更新人',
   primary key (id_factory)
)
ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '库表' ROW_FORMAT = Dynamic;

alter table face_library comment '库表';

create table portrait
(
   id_portrait          bigint(32) character set utf8 not null comment 'id',
   id_factory           bigint(32) comment '库id',
   id_faceid            varchar(32) comment '人脸id',
   url                  text character set utf8 comment '人脸图地址',
   background_url       text character set utf8 comment '背景图地址',
   name                 varchar(100) character set utf8 comment '姓名',
   sex                  varchar(10) character set utf8 comment '性别',
   birth_date           datetime comment '出生日期',
   id_card              varchar(18) character set utf8 comment '身份证号码',
   featId               varchar(32) character set utf8 comment '特征id',
   extra_meta           text comment '额外信息',
   face_rect            varchar(255) comment '人脸坐标位置',
   feature              text comment '特征信息',
   is_valid             varchar(1) character set utf8 comment '是否有效 Y有效 N无效',
   create_time          datetime comment '创建时间',
   create_by            varchar(32) character set utf8 comment '创建人',
   update_time          datetime comment '更新时间',
   update_by            varchar(32) character set utf8 comment '更新人',
   primary key (id_portrait)
)
ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '人像表' ROW_FORMAT = Dynamic;

alter table portrait comment '图片表';

create table warning_information
(
   id_warning_information bigint(32) character set utf8 not null comment 'id',
   id_control_task      varchar(32) character set utf8 comment '布控id',
   id_factory           varchar(32) comment '库id',
   id_face_bayonet      varchar(32) comment '摄像头id',
   face_bgurl           varchar(255) comment '背景图片',
   face_rect            varchar(255) comment '人脸坐标位置',
   remarks              text comment '备注信息',
   score                double default NULL comment '相似度',
   extra_meta           text comment '额外信息',
   is_valid             varchar(1) character set utf8 comment '是否有效 Y有效 N无效',
   create_time          datetime comment '创建时间',
   create_by            varchar(32) character set utf8 comment '创建人',
   update_time          datetime comment '更新时间',
   update_by            varchar(32) character set utf8 comment '更新人',
   primary key (id_warning_information)
)
ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '预警信息' ROW_FORMAT = Dynamic;

alter table warning_information comment '报警表';

