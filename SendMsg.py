import pywhatkit
import sys

number=sys.argv[1]
msg=sys.argv[2]
hour =int(sys.argv[3])
minute =int(sys.argv[4])
print(minute)
pywhatkit.sendwhatmsg(number,msg ,hour ,minute,25)