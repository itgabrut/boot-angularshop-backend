create table shema1.Bucket(
quantity int not null,
client_id int(11) not null,
item_id int(11) not null,
primary key(client_id,item_id),
foreign key(client_id) references users(id),
foreign key(item_id) references Items(id)
);