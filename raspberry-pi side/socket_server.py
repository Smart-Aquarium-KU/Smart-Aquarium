import socket
import sys
from thread import *
from Crypto.Cipher import DES
from datetime import datetime, timedelta
import re
import base64

def parseToken(str,hash):
  cipher = DES.new(hash,DES.MODE_CFB, hash)
  resolved=cipher.decrypt(base64.b64decode(str))
  return resolved
 
HOST = ''   # Symbolic name meaning all available interfaces
PORT = 1234 # Arbitrary non-privileged port

fo=open("aquarium.txt","r+")
string=fo.read(12);
fo.close()
tokens=string.split(",")
hashWord=tokens[2]

 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print 'Socket created'
 
#Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
     
print 'Socket bind complete'
 
#Start listening on socket
s.listen(10)
print 'Socket now listening'
 
#Function for handling connections. This will be used to create threads
def clientthread(conn):
    #Sending message to connected client
    #conn.send('Welcome to the server. Type something and hit enter\n') #send only takes string
     
    #infinite loop so that function do not terminate and thread do not end.
    while True :
         
        #Receiving from client
        data = conn.recv(1024)
        if not data: 
            break
         #send back reply to client
       # conn.sendall(reply)
        print(parseToken(data,hashWord));
     
    #came out of loop
    conn.close()
 
#now keep talking with the client
while 1:
    #wait to accept a connection - blocking call
    conn, addr = s.accept()
    print 'Connected with ' + addr[0] + ':' + str(addr[1])
     
    #start new thread takes 1st argument as a function name to be run, second is the tuple of arguments to the function.
    start_new_thread(clientthread ,(conn,))
 
s.close()
