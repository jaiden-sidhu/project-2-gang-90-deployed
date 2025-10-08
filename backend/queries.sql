--SPECIAL QUERY: select count of orders grouped by week
SELECT
    FLOOR(EXTRACT(EPOCH FROM (transaction_time - TIMESTAMP '2024-01-01')) / (7 * 24 * 60 * 60)) + 1 AS week_number,
    COUNT(*) AS order_count
FROM transactions
GROUP BY week_number
ORDER BY week_number;

--SPECIAL QUERY: select count of orders, sum of order total grouped by hour
SELECT
    EXTRACT(HOUR FROM transaction_time) AS hour_of_day,
    COUNT(*) AS order_count,
    SUM(total_price) AS total_sales
FROM transactions
GROUP BY hour_of_day
ORDER BY hour_of_day;

-- SSPECIAL QUERY: select top 10 sums of order total grouped by day in descending order by order total
SELECT
    CAST(transaction_time AS DATE) AS sale_day,
    SUM(total_price) AS daily_sales
FROM transactions
GROUP BY sale_day
ORDER BY daily_sales DESC
LIMIT 10;

--SPECIAL QUERY: select count of inventory items from inventory and menu grouped by menu item
SELECT 
    m.item_name,
    COUNT(i.ingredient_id) AS ingredient_count
FROM ingredients_map im
JOIN ingredients i 
    ON im.ingredient_id = i.ingredient_id
JOIN menu m
    ON im.item_id = m.item_id
GROUP BY m.item_name
ORDER BY m.item_name;

--show all cashiers
SELECT employee_id, name, pay
FROM personnel
WHERE role = 'cashier';

--show the average pay by role
SELECT role, AVG(pay) AS average_salary
FROM personnel
GROUP BY role;

--show menu items that cost less than $5.99
SELECT item_name, price
FROM menu
WHERE price < 5.99;

--Top 5 most popular menu items
SELECT item_name, item_popularity
FROM menu
ORDER BY item_popularity DESC
LIMIT 5;

--Average price of all menu items
SELECT AVG(price) AS average_item_price
FROM menu;


--Total sales over all time
SELECT SUM(total_price) As profit 
From transactions;

--Total number of sales 
SELECT COUNT(*) As total_transations
From transactions;

--Select all transations given customer_name
DECLARE @name AS VARCHAR(100)='Test cutomer'
SELECT * From transactions
WHERE customer_name = @name;

--Bottom 5 popular menu items
SELECT item_name, item_popularity
From menu
ORDER BY item_popularity ASC 
LIMIT 5; 

--Order by porfit of each item
SELECT item_name, price * item_popularity
From menu
ORDER BY item_popularity DESC;

--Get sales per weekday
SELECT
    TO_CHAR(transaction_time, 'Day') AS day_of_week,
    COUNT(*) AS order_count,
    SUM(total_price) AS total_sales
FROM transactions
GROUP BY day_of_week
ORDER BY total_sales DESC;


