find . -name \*.apk -exec rm -rf {} \;
find . -name gen -type d -exec rm -rf {} \;
find . -name bin -type d -exec rm -rf {} \;
find . -name classes -type d -exec rm -rf {} \;
find . -name target -type d -exec rm -rf {} \;
find . -name build -type d -exec rm -rf {} \;

