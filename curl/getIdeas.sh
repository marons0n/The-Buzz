## check if token is set
if [ -z "$TOKEN" ]; then
echo "Please set your token"
exit 1
fi

curl -X GET https://team-untitled-23.dokku.cse.lehigh.edu/ideas \
-H "Content-Type: application/json" \
-H "Authorization: Bearer $TOKEN"
