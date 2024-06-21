create table administrator
(
    id            int auto_increment
        primary key,
    phone         varchar(20) not null,
    name          varchar(20) not null,
    hospital_name varchar(20) null,
    password      varchar(20) not null,
    constraint administrator_id_uindex
        unique (id),
    constraint administrator_phone_uindex
        unique (phone)
);

create table project
(
    project_id   varchar(20)   not null
        primary key,
    description  text          null,
    project_name varchar(20)   not null,
    deleted      int default 0 not null,
    constraint project_projectId_uindex
        unique (project_id)
);

create table patient
(
    id                       int auto_increment
        primary key,
    alpha                    varchar(1)           not null,
    operate_date             date                 not null,
    project_id               varchar(20)          not null,
    patient_id_in_project    varchar(20)          not null,
    hospital_id_in_project   varchar(10)          not null,
    hospital_name_in_project varchar(20)          null,
    deleted                  tinyint(1) default 0 not null,
    constraint patient_id_uindex
        unique (id),
    constraint patient_fk_projectId
        foreign key (project_id) references project (project_id)
);

create table user
(
    id            int auto_increment
        primary key,
    phone         varchar(20)          not null,
    name          varchar(20)          not null,
    password      varchar(20)          not null,
    remark        text                 null,
    hospital_name varchar(20)          null,
    create_date   date                 not null,
    deleted       tinyint(1) default 0 not null,
    constraint user_id_uindex
        unique (id),
    constraint user_phone_uindex
        unique (phone)
);

create table assessment
(
    id          int auto_increment
        primary key,
    patient_id  int         not null,
    assess_time datetime    not null,
    assessor_id int         not null,
    assess_type varchar(20) not null,
    constraint assessment_id_uindex
        unique (id),
    constraint assessment_fk_assessor_id
        foreign key (assessor_id) references user (id),
    constraint assessment_fk_patientId
        foreign key (patient_id) references patient (id)
);

create table assess_record
(
    record_time      datetime             not null,
    id               int auto_increment
        primary key,
    assessment_id    int                  not null,
    recorder_id      int                  not null,
    is_original      tinyint(1) default 1 not null,
    change_reason    text                 null,
    feature1positive tinyint(1) default 0 not null,
    feature2positive tinyint(1) default 0 not null,
    feature3positive tinyint(1) default 0 not null,
    feature4positive tinyint(1) default 0 not null,
    constraint assessRecord_id_uindex
        unique (id),
    constraint assessRecord_fk_assessmentId
        foreign key (assessment_id) references assessment (id),
    constraint assessrecord_fk_recorderId
        foreign key (recorder_id) references user (id)
);

create table doctor_project
(
    id                       int auto_increment
        primary key,
    is_leader                tinyint(1) default 0 not null,
    hospital_name_in_project varchar(20)          null,
    hospital_id_in_project   varchar(10)          not null,
    doctor_id                int                  not null,
    project_id               varchar(20)          not null,
    constraint doctorProject_id_uindex
        unique (id),
    constraint doctor_project_fk_doctor_id
        foreign key (doctor_id) references user (id),
    constraint doctorproject_fk_project_id
        foreign key (project_id) references project (project_id)
);

create table record_question
(
    id               int auto_increment
        primary key,
    record_id        int                     not null,
    question_no      varchar(4)              not null,
    question_content text                    not null,
    answer_correct   tinyint(1)  default 1   not null,
    answer_judgement varchar(20) default '0' not null,
    answer_content   text                    null,
    constraint recordQuestion_id_uindex
        unique (id),
    constraint recordQuestion_fk_recordId
        foreign key (record_id) references assess_record (id)
);


