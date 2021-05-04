create sequence user_seq
start with 1
increment by 1
minvalue 0;

create sequence content_seq
start with 1
increment by 1
minvalue 0;

drop table board_info_table purge;
create table board_info_table(
  board_info_idx   number  constraint board_info_pk primary key,
  board_info_name  varchar2(500) not null
);

insert into board_info_table values(1, '자유게시판');
insert into board_info_table(board_info_idx, board_info_name) values(2, '유모게시판');
insert into board_info_table(board_info_idx, board_info_name) values(3, '정치게시판');
insert into board_info_table(board_info_idx, board_info_name) values(4, '스포츠게시판');

select * from board_info_table;

commit;

drop table user_table purge;
create table user_table(
  user_idx   number  constraint user_pk primary key,
  user_name  varchar2(50) not null,
  user_id    varchar2(100) unique not null,
  user_pw    varchar2(100) not null
);

select * from user_table;


drop table content_table purge;
create table content_table(
  content_idx        number constraint content_pk primary key,
  content_subject    varchar2(500) not null,
  content_text       long not null,
  content_file       varchar2(500),
  content_writer_idx number constraint content_fk1 references user_table(user_idx),
  content_board_idx  number constraint content_fk2 references board_info_table(Board_info_idx),
  content_date       date not null 
);

select * from content_table;


/*
board_info_table 테이블에서
board_info_idx, board_info_name을
board_info_idx 오름차순으로 가져오기
*/

select board_info_idx, board_info_name
from board_info_table
order by board_info_idx;


-- 아이디 중복 확인하기

select user_name 
from user_table
where user_id = 'tjoeun';