#!/bin/bash
set -e

echo '1.启动mysql....'
#启动mysql
service mysql start
sleep 3
echo `service mysql status`

echo '2.开始导入数据....'
#导入数据
mysql < /mysql/sakila-schema.sql
mysql < /mysql/sakila-data.sql
echo '3.导入数据完毕....'

sleep 3
echo `service mysql status`
echo `mysql容器启动完毕,且数据导入成功`

tail -f /dev/null