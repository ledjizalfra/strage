# Function to implement the pause command
function pause(){
 read -s -n 1 -p "Press any key to continue . . ."
 echo ""
}
echo "> START MYSQL CONTAINER <"

# This will remove all local volumes not used by at least one container.
docker volume prune -f

docker compose up mysql-db -d --force-recreate 

# Dangling images are not referenced by other images and are safe to delete
# This will remove all dangling images
docker image prune -f

# This will remove all custom networks not used by at least one container.
docker network prune -f

echo ""
echo ""
pause
sleep 30