<ul>
<li>create table doctor (uuid uuid not null, first_name varchar(255), last_name varchar(255), patronymic varchar(255), primary key (uuid))</li>
<li>create table doctor_specializations (doctors_uuid uuid not null, specializations_uuid uuid not null, primary key (doctors_uuid, specializations_uuid))</li>
<li>create table patient (uuid uuid not null, first_name varchar(255), last_name varchar(255), patronymic varchar(255), primary key (uuid))</li>
<li>create table patient_talons (patient_uuid uuid not null, talons_uuid uuid not null, primary key (patient_uuid, talons_uuid))</li>
<li>create table specialization (uuid uuid not null, duration numeric(21,0), name varchar(255), primary key (uuid))</li>
<li>create table talon (uuid uuid not null, start_date_time timestamp(6), stop_date_time timestamp(6), doctor_uuid uuid, patient_uuid uuid, specialization_uuid uuid, primary key (uuid))</li>
<li>alter table if exists patient_talons drop constraint if exists UK_m4iue0wuftb2d8viqa17soy1b</li>
<li>alter table if exists patient_talons add constraint UK_m4iue0wuftb2d8viqa17soy1b unique (talons_uuid)</li>
<li>alter table if exists doctor_specializations add constraint FKisj28p734bowe6hf7t30l0svv foreign key (specializations_uuid) references specialization</li>
<li>alter table if exists doctor_specializations add constraint FKen46aip3o005385s7ps71xltm foreign key (doctors_uuid) references doctor</li>
<li>alter table if exists patient_talons add constraint FKjtj40bbvu4w4h28bkasmdk68c foreign key (talons_uuid) references talon</li>
<li>alter table if exists patient_talons add constraint FK51sy9kxnvbxog8a5vf6xmc5q5 foreign key (patient_uuid) references patient</li>
<li>alter table if exists talon add constraint FKnm0q1qiy9rbjuidr671y88vp foreign key (doctor_uuid) references doctor</li>
<li>alter table if exists talon add constraint FKautwaol8fovxaq66tojuf06tc foreign key (patient_uuid) references patient</li>
<li>alter table if exists talon add constraint FKmevjxsq0uf0qb2r1kh4h0f5al foreign key (specialization_uuid) references specialization</li>
</ul>
