# poalim
poalim
The code contains 2 projects :

1. searchClient - responsible for loading accountId and user name to indexing db
2. uploder - contains GUI and using AWS OCR to fetch text from image then calling to searchClient looking for name.

both projects use spring boot on AWS 

Amazon Rekognition was chossen since all OCR JDK's do requier loading a file from local FS which is not possible 
in cloud environment.



