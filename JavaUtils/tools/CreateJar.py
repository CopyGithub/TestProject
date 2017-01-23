import os
import fnmatch
import shutil
 
def iterfindfiles(path, fnexp):
    for root, dirs, files in os.walk(path):
        for filename in fnmatch.filter(files, fnexp):
            yield os.path.join(root, filename)

output = open('1.txt','w+')
for filename in iterfindfiles(r"../src", "*.java"):
    output.write(filename+"\n")
output.close()
os.system('mkdir target')
os.system('javac -encoding UTF-8 -d target @1.txt')
os.system('jar cf javaUtils.jar -C target/ .')
os.remove('1.txt')
shutil.rmtree('target')
