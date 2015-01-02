-- selects the sum of a range of values over a time period
-- PARTITION BY clause sets the range of records that will be used for each "GROUP" within the OVER clause.

select distinct(trunc(date_walked, 'DDD')), sum(steps) over (
	partition by trunc(date_walked, 'DDD') order by date_walked RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) s
	from wfh_logs where date_walked >= (sysdate - 7) and steps <> 0 and username='tester2';

-- mysql dump and restore
--mysqldump -u -p database_one table_name > /var/www/backups/table_name.sql

-- Restoring the table into another database
--mysql -u -p database_two < /var/www/backups/table_name.sql
