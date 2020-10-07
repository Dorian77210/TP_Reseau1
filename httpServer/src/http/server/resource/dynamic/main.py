import sys
def main():
    argv = sys.argv
    if (len(argv) != 5):
        print("Usage: python3 main.py <headers> <params> <protocol> <body>");
        sys.exit(1);
    
    print("Headers = {}\n parameters = {}\n protocol = {}\n body = {}\n".format(argv[1], argv[2], argv[3], argv[4]));

main();
