import requests
import random
from lxml import etree

def checkName(name):
    if name.strip() == "":
        print("Sorry, you did not enter your name")
        return 0
    else:
        print("\n--------------------------------------\n")
        return 1


def hangman():
    # Random choices INSIDE function
    alphabetChoice = random.choice(list("abcdefghi"))
    wordChoice = random.randint(1, 9)

    url = f"https://randomword.com/words/{alphabetChoice}.html"
    xpath = f"/html/body/div[1]/div[2]/div/div/div[1]/div[2]/ul[1]/li[{wordChoice}]"

    try:
        response = requests.get(url, timeout=5)
        tree = etree.HTML(response.content)
        elements = tree.xpath(xpath)

        if not elements:
            print("Failed to fetch word. Try again.")
            return

        word = elements[0].text.strip().lower()

    except:
        print("Error fetching word. Check internet connection.")
        return

    alphabets = "abcdefghijklmnopqrstuvwxyz"
    chance = 10
    guess_made = ""

    while True:
        main = ""
        for letter in word:
            if letter in guess_made:
                main += letter
            else:
                main += "_ "

        if "_" not in main:
            print("\nWord is:", main)
            print("You win!")
            break

        print(f"\nGuess the word: {main}")
        guess = input("Enter a letter: ").lower()

        if guess not in alphabets or len(guess) != 1:
            print("Invalid input, enter a single letter")
            continue

        if guess in guess_made:
            print("Already guessed")
            continue

        guess_made += guess

        if guess not in word:
            chance -= 1
            print("Wrong guess!")

            if chance == 9:
                print("9 turns left")

            elif chance == 8:
                print("8 turns left")
                print("      O")

            elif chance == 7:
                print("7 turns left")
                print("      O")
                print("      |")

            elif chance == 6:
                print("6 turns left")
                print("      O")
                print("      |")
                print("     /")

            elif chance == 5:
                print("5 turns left")
                print("      O")
                print("      |")
                print("     / \\")

            elif chance == 4:
                print("4 turns left")
                print("     \\O")
                print("      |")
                print("     / \\")

            elif chance == 3:
                print("3 turns left")
                print("     \\O/")
                print("      |")
                print("     / \\")

            elif chance == 2:
                print("2 turns left")
                print("     \\O/  |")
                print("      |")
                print("     / \\")

            elif chance == 1:
                print("1 turn left - last chance!")
                print("     \\O/ __|")
                print("      |")
                print("     / \\")

            elif chance == 0:
                print(f"\nYou lost! The word was: {word}")
                print("Game Over")
                print("      O____|")
                print("     /|\\")
                print("     / \\")
                break


# MAIN
name = input("Hey there! What is your name: ")

if checkName(name):
    print(name + ", let's play a game")
    hangman()