#!/bin/bash

#@echo off
#mvn clean package && ant

# 1. 获取最近一次提交的commit ID
last_commit=$(git rev-parse HEAD)
echo "当前构建版本：$last_commit"
echo "上一次构建版本：$(cat .last_commit)"
# 2. 判断当前构建版本与上一次构建版本是否一致
if [ -f .last_commit ] && [ "$(cat .last_commit)" = "$last_commit" ]; then
  echo "Git仓库没有新的提交，跳过编译打包操作"
  #mvn package
else
  echo "Git仓库有新的提交，执行编译打包操作"
  mvn package
  # 保存当前构建版本到文件
  echo "$last_commit" > .last_commit
fi