create table users
(
    user_id   SERIAL primary key,
    user_name varchar not null default '',
    login     varchar not null,
    is_admin  bool    not null default false,
    passwd    varchar not null,
    balance   bigint  not null default 0
);

create table admins
(
    admin_id     SERIAL primary key,
    user_id      int references users (user_id),
    common_rate  float   not null default -1,
    review_count int     not null default 0,
    phone_number varchar not null default ''
);

create table customers
(
    customer_id SERIAL primary key,
    user_id     int references users (user_id),
    add_contact varchar
);

create table rating
(
    rating_id   SERIAL primary key,
    customer_id int references customers (customer_id),
    admin_id    int references admins (admin_id),
    rate        float not null default -1
);

create table orders
(
    order_id         SERIAL primary key,
    user_id          int references users (user_id),
    status           varchar not null default '',
    order_number     varchar not null,
    delivery_date    date,
    items_amount     int     not null default 1,
    total_cost       int     not null,
    delivery_address varchar
);

create table categories
(
    category_id SERIAL primary key,
    name        varchar not null,
    uri         varchar not null default '/'
);

create table items
(
    item_id SERIAL primary key,
    price int not null,
    category_id int references categories(category_id),
    description varchar not null default '',
    admin_id int references admins(admin_id)
);

create table photos
(
    photo_id SERIAL primary key,
    uri varchar not null default '/',
    item_id int references items(item_id),
    is_main bool not null default false
);
