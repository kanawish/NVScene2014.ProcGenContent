# PasswordBox project specific
find . -name spoon-output -type d -exec rm -rf {} \;

# Mac specific
find . -name .DS_Store -exec rm -rf {} \;

# Generic Android Ignores
find . -name \*.apk -exec rm -rf {} \;
find . -name gen -type d -exec rm -rf {} \;
find . -name bin -type d -exec rm -rf {} \;
find . -name classes -type d -exec rm -rf {} \;
find . -name target -type d -exec rm -rf {} \;
find . -name build -type d -exec rm -rf {} \;
find . -name proguard_logs -type d -exec rm -rf {} \;
find . -name .gradle -type d -exec rm -rf {} \;

# IDEA Ignores
find . -name \*.iml -exec rm -rf {} \;
find . -name \*.ipr -exec rm -rf {} \;
find . -name \*.iws -exec rm -rf {} \;
find . -name .idea -type d -exec rm -rf {} \;
find . -name local.properties -exec rm -rf {} \;

# Eclipse 
find . -name .project -exec rm -rf {} \;

# Others / automatically added
