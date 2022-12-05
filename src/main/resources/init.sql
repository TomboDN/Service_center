CREATE TABLE IF NOT EXISTS users
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL DEFAULT 'customer'
);

CREATE TABLE IF NOT EXISTS customers
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    phone   VARCHAR(255) UNIQUE,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    customer_id   INT          NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE,
    total_cost    INT,
    item          VARCHAR(255) NOT NULL,
    reason        VARCHAR(255) NOT NULL,
    finished      BOOLEAN DEFAULT FALSE,
    creation_date DATETIME,
    paid          BOOLEAN DEFAULT FALSE,
    finished_date DATETIME
);

CREATE TABLE IF NOT EXISTS workers
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    salary  INT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS services
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL,
    cost INT                 NOT NULL
);

CREATE TABLE IF NOT EXISTS spare_parts
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(255) NOT NULL,
    amount INT          NOT NULL,
    cost   INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS orders_services
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    order_id       INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    service_id     INT NOT NULL,
    FOREIGN KEY (service_id) REFERENCES services (id) ON DELETE CASCADE,
    worker_id      INT NOT NULL,
    FOREIGN KEY (worker_id) REFERENCES workers (id) ON DELETE CASCADE,
    spare_parts_id INT,
    FOREIGN KEY (spare_parts_id) REFERENCES spare_parts (id) ON DELETE CASCADE,
    finished       BOOLEAN DEFAULT FALSE
);

INSERT INTO customers (name, surname, phone)
VALUES ('Иван', 'Иванов', '88005553535'),
       ('Петр', 'Петров', '89234440003'),
       ('Александр', 'Александров', '89450230002'),
       ('Денис', 'Маркин', '89450230342'),
       ('Роман', 'Александров', '89450557402');

INSERT INTO orders (customer_id, total_cost, item, reason, finished, creation_date, paid, finished_date)
VALUES ('2', '4000', 'HP Omen 15 2020', 'Не работает клавиатура', '1', '2022-07-12 13:00:00', '1',
        '2022-07-16 10:00:00'),
       ('3', '1500', 'Lenovo Legion 3', 'Случайные выключения', '1', '2022-08-15 15:30:06', '1', '2022-08-17 09:36:36'),
       ('1', '8000', 'Asus Laptop', 'Артефакты дисплея', '0', '2022-09-01 07:50:59', '0', null),
       ('5', '2500', 'Lenovo Legion 5', 'Утечки памяти', '1', '2022-08-17 15:30:06', '1', '2022-08-20 09:36:36'),
       ('4', '2500', 'Lenovo Legion 7', 'Случайные выключения', '1', '2022-08-16 15:30:06', '1', '2022-08-17 09:36:36'),
       ('4', '7500', 'Asus Vivo', 'Не работает экран', '0', '2022-09-01 13:30:06', '0', null),
       ('3', '1750', 'HP Envy', 'Не работает', '1', '2022-08-27 15:30:06', '1', '2022-08-28 15:20:36'),
       ('2', '5500', 'Huawei MateBook', 'Не работает клавиатура', '1', '2022-08-09 15:30:06', '1',
        '2022-08-15 13:33:33'),
       ('1', '2500', 'HP Omen 17 2021', 'Случайные выключения', '0', '2022-09-02 18:30:06', '0', null),
       ('5', '8000', 'Dell 23', 'Артефакты дисплея', '1', '2022-08-25 10:24:06', '0', '2022-08-29 19:46:36');

INSERT INTO workers (name, surname, salary)
VALUES ('Кошкин', 'Лев', '45000'),
       ('Рощин', 'Павел', '20000');

INSERT INTO services (name, cost)
VALUES ('Диагностика', '1000'),
       ('Замена термопасты', '750'),
       ('Замена клавиатуры', '1000'),
       ('Замена видеочипа', '1500'),
       ('Замена экрана', '1500'),
       ('Исправление операционной системы', '1500');

INSERT INTO spare_parts (name, amount, cost)
VALUES ('Видеочип Nvidia RTX 2060', '5', '7000'),
       ('Термопаста Arctic Cooling', '100', '500'),
       ('Клавиатура HP Omen 15', '3', '3000'),
       ('Процессор AMD FX-8300', '2', '6000'),
       ('Экран Asus Vivo', '7', '5000'),
       ('Клавиатура Huawei MateBook', '7', '3500');

INSERT INTO orders_services (order_id, service_id, worker_id, spare_parts_id, finished)
VALUES ('2', '1', '1', null, '1'),
       ('3', '1', '1', null, '1'),
       ('1', '1', '2', null, '1'),
       ('1', '3', '2', '3', '1'),
       ('2', '2', '1', '2', '1'),
       ('3', '4', '1', '1', '0'),
       ('4', '1', '1', null, '1'),
       ('5', '1', '1', null, '1'),
       ('6', '1', '2', null, '1'),
       ('7', '1', '2', null, '1'),
       ('8', '1', '2', null, '1'),
       ('9', '1', '1', null, '1'),
       ('10', '1', '1', null, '1'),
       ('4', '6', '1', null, '1'),
       ('5', '6', '1', null, '1'),
       ('6', '5', '2', '5', '0'),
       ('7', '2', '2', '2', '1'),
       ('8', '3', '2', '6', '1'),
       ('9', '6', '1', null, '0'),
       ('10', '4', '2', '1', '1');


create procedure service.finish_order(o_id int)
begin
update orders set finished=1 and finished_date = current_timestamp() where id = o_id;
update orders_services set finished=1 where order_id=o_id;
end;

CREATE function service.calculate_cost(o_id int) returns int
    reads sql data
BEGIN
    declare services_cost, parts_cost int;
select sum(cost)
into services_cost
from orders_services
         join service.services s on s.id = orders_services.service_id
where order_id = o_id
  and cost is not null;
select sum(cost)
into parts_cost
from orders_services
         join service.spare_parts sp on sp.id = orders_services.spare_parts_id
where order_id = o_id
  and orders_services.spare_parts_id is not null;
return services_cost + parts_cost;
end;

create function service.customer_exists(c_id int) returns int
    reads sql data
begin
return exists(select * from customers where id = c_id);
end;

create function service.service_exists(s_id int) returns int
    reads sql data
begin
return exists(select * from services where id = s_id);
end;

create function service.order_exists(o_id int) returns int
    reads sql data
begin
return exists(select * from orders where id = o_id);
end;

create function service.worker_exists(w_id int) returns int
    reads sql data
begin
return exists(select * from workers where id = w_id);
end;

create function service.spare_part_exists(sp_id int) returns int
    reads sql data
begin
return exists(select * from spare_parts where id = sp_id);
end;

create function service.calculate_salary(w_id int)
    returns int
    reads sql data
begin
    declare services_cost, parts_cost, salary_counted int;
select sum(cost)
into services_cost
from orders_services
         join service.services s on s.id = orders_services.service_id
where orders_services.worker_id = w_id
  and cost is not null;

select sum(cost)
into parts_cost
from orders_services
         join service.spare_parts sp on sp.id = orders_services.spare_parts_id
where worker_id = w_id
  and orders_services.spare_parts_id is not null;

return ((services_cost + parts_cost) / 1.7);
end;

create trigger service.trigger_new_order_before
    before insert
    on orders
    for each row
begin
    if customer_exists(new.customer_id)
    then
        set new.creation_date = current_timestamp();
    else
        signal sqlstate '45000' set message_text = 'Customer does not exist';
    end if;
    if new.total_cost is null then
        set new.total_cost = 0;
    end if;
end;

create trigger service.trigger_new_orders_services_before
    before insert
    on orders_services
    for each row
begin
    if not order_exists(new.order_id)
    then signal sqlstate '45000' set message_text = 'Order does not exist';
    end if;
    if not service_exists(new.service_id)
    then signal sqlstate '45000' set message_text = 'Service does not exist';
    end if;
    if not (new.spare_parts_id is not null and spare_part_exists(new.spare_parts_id))
    then
        signal sqlstate '45000' set message_text = 'Spare part does not exist';
    end if;
    if not worker_exists(new.worker_id)
    then
        signal sqlstate '45000' set message_text = 'Worker does not exist';
    end if;
end;