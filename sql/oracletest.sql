create table member(
mno number(5) not null,
mname nvarchar2(10) not null,
id nvarchar2(10) primary key, --pk 선언
pw nvarchar2(10) not null,
regidate date default sysdate not null
)
create sequence member_seq increment by 1 start with 1 nocycle nocache
-- 시퀀스 생성(1부터 시작해서 끝까지 가면 반복하지 않고 종료)

insert into member (mno,mname,id,pw)
values (member_seq.nextval,'전민기','jmg','1234')
insert into member (mno,mname,id,pw)
values (member_seq.nextval,'김진우','kjw','1234')
insert into member (mno,mname,id,pw)
values (member_seq.nextval,'김보령','kbr','1234')
insert into member (mno,mname,id,pw)
values (member_seq.nextval,'이하늘','lhn','1234')
insert into member (mno,mname,id,pw)
values (member_seq.nextval,'배혜민','bhm','1234')
insert into member (mno,mname,id,pw)
values (member_seq.nextval,'김샛별','ksb','1234')
insert into member (mno,mname,id,pw)
values (member_seq.nextval,'전혜진','jhj','1234')

select * from member -- 테이블 확인용 
drop table member -- 테이블 삭제용
delete from member -- 테이블 데이터 삭제용
------------------------------------member table 끝-----------------------------------------------------------
alter table board add constraint board_member_fk foreign key (bwriter) references member(id)
create table board (
bno number(5) primary key,
btitle nvarchar2(30) not null,
bcontent nvarchar2(1000) not null,
bwriter nvarchar2(10) not null, -- 외래키
bdate date not null
)
insert into board (bno, btitle, bcontent, bwriter, bdate)
values (member_seq.nextval, '사과', '맛있어요','jmg',sysdate)
insert into board (bno, btitle, bcontent, bwriter, bdate)
values (member_seq.nextval, '배', '달아요','kjw',sysdate)
insert into board (bno, btitle, bcontent, bwriter, bdate)
values (member_seq.nextval, '레몬', '셔요','kbr',sysdate)
insert into board (bno, btitle, bcontent, bwriter, bdate)
values (member_seq.nextval, '자몽', '써요','lhn',sysdate)
insert into board (bno, btitle, bcontent, bwriter, bdate)
values (member_seq.nextval, '바나나', '길어요','bhm',sysdate)
insert into board (bno, btitle, bcontent, bwriter, bdate)
values (member_seq.nextval, '수박', '빨개요','ksb',sysdate)
insert into board (bno, btitle, bcontent, bwriter, bdate)
values (member_seq.nextval, '파인애플', '달고셔요','jhj',sysdate)
select * from board -- 테이블 확인용 
drop table board -- 테이블 삭제용
delete from board -- 테이블 데이터 삭제용
-------------------------------------------------------------------------------------------------
select b.*, m.mname from member m inner join board b on m.id = b.bwriter where m.id='jmg'
-- 회원 테이블과 게시판 테이블 id=bwriter 조건으로 연결해서 jmg인 회원이 작성한 내용 조회하는 쿼리문.
select id from member
select bwriter from board