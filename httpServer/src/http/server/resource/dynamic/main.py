import sys
def main():
    argv = sys.argv
    print("Bien executé")
    print("Salut")
    print("dd")
    with open('toto.txt', 'a') as file:
        file.write("salut");
main()

