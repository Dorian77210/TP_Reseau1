import sys
import json

argv = sys.argv
if (len(argv) != 5):
    print("Usage: python3 main.py <headers> <params> <protocol> <body>");
    sys.exit(1);

# print("{}".format(argv[2].split("\n")));
elements = argv[2].split("\n");
result = 0;
for element in elements:
	x = int(element.split('=')[1]);
	result += x;

j = {
	'result': result
};
print("{}".format(json.dumps(j, separators=(',', ':'))));
