set -v
screen
node index.js
autostart=true
autorestart=true

# Application should now be running under supervisor