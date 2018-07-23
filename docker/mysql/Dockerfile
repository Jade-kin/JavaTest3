FROM registry.saas.hand-china.com/tools/mysql:5.6

#设置免密登录
ENV MYSQL_ALLOW_EMPTY_PASSWORD yes

#将所需文件放到容器中
COPY ../mysql/setup.sh /mysql/setup.sh
COPY ../mysql/sakila-schema.sql /mysql/schema.sql
COPY ../mysql/sakila-data.sql /mysql/privileges.sql

#设置容器启动时执行的命令
CMD ["sh", "/mysql/setup.sh"]