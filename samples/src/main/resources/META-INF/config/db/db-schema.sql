create table users (
	user_name		varchar(25)		not null,
	full_name		varchar(250)	not null,
	email_address	varchar(80)		not null
);
alter table users add constraint pk_users primary key (user_name);

create table posts (
	id				identity		not null,
	user_name		varchar(25)		not null,
	submit_date		timestamp		not null default now(),
	content			varchar(4000)
);
alter table posts add constraint fk_posts_users foreign key (user_name) references users (user_name);
create index idx_posts_user_name on posts (user_name);
