# Function to implement the pause command
function pause(){
 read -s -n 1 -p "Press any key to continue . . ."
 echo ""
}

#@echo "Adding files on repository..."
git add .

#@echo "adding url of distant repository"
git remote add origin https://ledjizalfra@github.com/ledjizalfra/strage.git

#@echo "Our first commit..."
git commit -m "UPDATE"

#@echo "pushing..."
git push -u origin master

echo ""
echo ""
pause
sleep 15