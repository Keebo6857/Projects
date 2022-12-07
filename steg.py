import sys
#Byte Method
#storage

#grabs store or recieve
sr = sys.argv[1]

#grabs byte or bit
bB = sys.argv[2]

#grabs wrapper file
wFile = sys.argv[5]
wFile = wFile[2:len(wFile)]

#grabs offset
off = sys.argv[3]
off = off[2:len(off)]
off = int(off)


#flag for storing or recieving 
sflag = True
 
#checking for store or recieve
if(sr == "-s"):
    sflag = True
else:
    sflag = False

#grabs interval
interval = sys.argv[4]
interval = interval[2:len(interval)]
interval = int(interval)

#sentinal
sent = bytearray(b'\x00\xFF\x00\x00\xFF\x00')
sent2 = []
for i in range(0, len(sent)):
    sent2.append(sent[i])
    
sentarr = [0, 255, 0, 0, 255, 0]


w = ""
h = ""
    
    
f = open(wFile, 'rb')
w = bytearray(f.read())
f.close


#start of logic
if(bB == "-B"):
    #if storing
    if(sflag):
        i = 0
        while i < len(h):
            w[off] = h[i]
            off += interval
            i += 1

        j = 0
        while j < len(sent):
            w[off]= sent[j]
            off += interval
            j += 1

    #if recieving    
    else:
        while(off < len(w)):
            h = bytearray()
            b = bytearray()
            b = w[off]
            #print(b)
            flag = False
            for x in range(0, len(sent)):
                if(w[off + interval*x] != sent[x]):
                    flag = True
                    break
            if(flag):
                h.append(b)
            else:
                break
            off += interval
        strh = h    
        sys.stdout.buffer.write(strh)

else:
    if(sflag):
        pass
    else:
        harr = bytearray()
        found = False
        barr = []
        while(off < len(w)):
            b = 0b00000000
            for j in range(8):
                b |= (w[off] & 0b00000001)
                if(j < 7):
                    b = ((b << 1) & (2 ** 8 - 1))
                    off += interval
            off += interval
            barr.append(b) 
            # print(barr)
            if(barr == [0,255,0,0,255,0]):
                found = True
                break
            else:
                harr.append(barr[0])
                temp = barr
                for i in range(0, len(temp)-1):
                    temp[i] = barr[i+1]
                if(len(temp) == 6):
                    temp.pop(len(temp) - 1)
                barr = temp

                
                
            
        
        if(found):
            sys.stdout.buffer.write(harr)
        else:
            sys.stdout.buffer.write("no worky")
           
