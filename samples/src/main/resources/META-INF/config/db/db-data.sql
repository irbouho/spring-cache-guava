insert into users (user_name, full_name, email_address) values ('omar',	'Omar Irbouh',	'omar@irbouh.net');
insert into users (user_name, full_name, email_address) values ('taha',	'Taha Irbouh',	'taha@irbouh.net');
insert into users (user_name, full_name, email_address) values ('adam',	'Adam Irbouh',	'adam@irbouh.net');
commit;

insert into posts (user_name, content) values ('omar', 'omar - entry nbr 1');
insert into posts (user_name, content) values ('omar', 'omar - entry nbr 2');
insert into posts (user_name, content) values ('omar', 'omar - entry nbr 3');
insert into posts (user_name, content) values ('taha', 'taha - entry nbr 1');
insert into posts (user_name, content) values ('adam', 'adam - entry nbr 1');
commit;
