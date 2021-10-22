@echo off
if not exist pgsql\_data (
	mkdir pgsql\_data
	pgsql\bin\initdb -D pgsql\_data -U postgres
)
pgsql\bin\pg_ctl -D pgsql\_data start

SET PGCLIENTENCODING=utf-8
pgsql\bin\psql -U postgres -c "drop database if exists formation_spring"
pgsql\bin\psql -U postgres -c "create database formation_spring"
pgsql\bin\psql -U postgres -d formation_spring -f ..\workspaces\myWorkspace\pg-data.sql
