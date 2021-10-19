@echo off
set PATH=..\..\..\env\node-v14.17.0-win-x64;%PATH%
if not exist node_modules (
    npm install
)
npm start
