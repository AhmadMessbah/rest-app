@echo off
echo Stopping all running containers...
powershell -Command "docker ps -a -q | Select-Object -Unique | ForEach-Object { docker stop $_ }"

echo Removing all containers...
powershell -Command "docker ps -a -q | Select-Object -Unique | ForEach-Object { docker rm $_ }"

echo Removing all images...
powershell -Command "docker images -q | Select-Object -Unique | ForEach-Object { docker rmi -f $_ }"

echo Removing all volumes...
powershell -Command "docker volume ls -q | Select-Object -Unique | ForEach-Object { docker volume rm $_ }"

echo Removing build cache and build history...
powershell -Command "docker builder prune -a -f"

echo Cleaning up unused Docker objects...
powershell -Command "docker system prune -a -f --volumes"

echo Docker cleanup completed.
pause