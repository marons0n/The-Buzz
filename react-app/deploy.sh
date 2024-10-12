#!/bin/bash

# Build the React app
npm run build

# Copy the build to your deployment branch
git checkout deployment
rm -rf build
cp -r ../build .

# Commit and push the changes
git add .
git commit -m "Deploy web app"
git push origin deployment

# Switch back to the main branch
git checkout main