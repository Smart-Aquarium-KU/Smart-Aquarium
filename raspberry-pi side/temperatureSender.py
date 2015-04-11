import socket
import sys
import time
from Crypto.Cipher import DES
import base64

def cyrpth(str,salt):
  cipher = DES.new(salt,DES.MODE_CFB, salt)
  encrypted=base64.b64encode(cipher.encrypt(str))
  return encrypted

try:
    temp=32
    while True:
        # Create a TCP/IP socket
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        # Connect the socket to the port where the server is listening
        server_address = ('localhost', 1235)
        #print >>sys.stderr, 'connecting to %s port %s' % server_address
        sock.connect(server_address)
        # Send data
	fo=open("aquarium.txt","r+")
	string=fo.read(12);
	fo.close()
	tokens=string.split(",")
	
        message = tokens[1]+','+tokens[2]+','+str(temp)
	
	print >>sys.stderr, 'sending "%s"' % message
        
	encryptedMessage=cyrpth(message,tokens[2])
	finalMessage=encryptedMessage[:10]+tokens[2]+encryptedMessage[-10:]
        print >>sys.stderr, 'sending "%s"' % finalMessage
        sock.sendall(finalMessage)
	sock.close()
        time.sleep(1)
        temp=temp+1
        

finally:
    print >>sys.stderr, 'closing socket'
    #sock.close()
