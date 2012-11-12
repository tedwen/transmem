import sys
import struct

gbunicode = {}

def loadtable(tablefile):
	f = open(tablefile)
	while True:
		line = f.readline()
		if line=='':break
		if line.startswith('0x'):
			s = line.split('\t')
			gb = int(s[0],16) + 0x8080
			un = int(s[1],16)
			gbunicode[gb] = un
	f.close()

if len(sys.argv)<3:
	print 'python gb2unicode.py <property_file> <output_file> [<html or char>]'
else:
	tablefile = 'gb-unicode.table'
	if sys.argv[0].find('\\')>0:
		n = sys.argv[0].rfind('\\')
		path = sys.argv[0][:n+1]
		tablefile = path + tablefile
	loadtable(tablefile)
	fname = sys.argv[1]
	foname = sys.argv[2]
	if len(sys.argv)>3:
		output = sys.argv[3]
	else:
		output = 'html'
	fi = open(fname)
	fo = open(foname,'wb')
	while True:
		c = fi.read(1)
		if c=='':break
		d1 = ord(c)
		if d1 < 0x80:
			fo.write(c)
		else:
			c2 = fi.read(1)
			if c2=='':break
			d2 = ord(c2)
			if d2 < 0x80:
				fo.write(c)
			else:
				d = d1 << 8 | d2
				ucode = gbunicode[d]
				if output=='html':
					fo.write('&#%d;'%ucode)
				else:
					try:
						fo.write(struct.pack('BB',(ucode >> 8) & 0xff, ucode & 0xff))
					except Exception:
						print ucode
	fi.close()
	fo.close()

def unicode2utf8(d):
	if d < 0x800:
		dc1 = 0xc0 | (d >> 6)
		dc2 = 0x80 | (d & 0x3f)
		rs = struct.pack('bb',dc1, dc2)
	elif d < 0x10000:
		dc1 = 0xe0 | (d>>12)
		dc2 = 0x80 | (d>>6 & 0x3f)
		dc3 = 0x80 | d & 0x3f
		rs = struct.pack('bbb',dc1, dc2, dc3)
	elif d < 0x200000:
		dc1 = 0xf0 | d>>18
		dc2 = 0x80 | d>>12 & 0x3f
		dc3 = 0x80 | d>>6 & 0x3f
		dc4 = 0x80 | d & 0x3f
		rs = struct.pack('bbbb',dc1, dc2, dc3, dc4)
	return rs
