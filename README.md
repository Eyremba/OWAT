# OWAT
"Obfuscation With A Twist", a protocol for obfuscating and un-obfuscating data inspired by the Rubik's Cube.

Essentially, the idea is simple. The data to be obfuscated is placed into a matrix, with each bit placed into a point on that matrix. Then, the matrix is grown my adding rows and columns of dummy data in order to increase the amount of possible entropy. Once the data is set up, the data is scrambled, and the steps to scramble the data are recorded. The scrambled data is outputted to a ".obfd" (obfuscated data) file, and 'key' (list of scrambling moves, other bookkeeping data) is outputted to a ".obfdk" (obfuscated data key) file. In order to unscramble, the program reads in the scrambled data, recreates the scrambled matrix, and uses the key file to unscramble the data. The unscrambled data is then outputted back to the user.

### The goal of this process is not to be efficient in memory or processing time. The goal is to effectively obscure and hide data.
