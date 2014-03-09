# PasswordBox project specific
find . -name spoon-output -type d 

# Mac specific
find . -name .DS_Store 

# Generic Android Ignores
find . -name \*.apk 
find . -name gen -type d 
find . -name bin -type d 
find . -name classes -type d 
find . -name target -type d 
find . -name build -type d 
find . -name proguard_logs -type d 
find . -name .gradle -type d 

# IDEA Ignores
find . -name \*.iml 
find . -name \*.ipr 
find . -name \*.iws 
find . -name .idea -type d 
find . -name local.properties 

# Eclipse 
find . -name .project 

# Others / automatically added
