create table if not exists Users (
  id int not null,
  name varchar(40) not null
);
create table if not exists Phonebook (
  id int not null,
  owner_id int not null,
  number varchar(40) not null
);
alter table Phonebook
add foreign key(owner_id) references Users(id) ON DELETE CASCADE;