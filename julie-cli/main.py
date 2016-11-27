import sys
import json
import requests

import configs
from utils import Utils

class WeightHandler:

    def __init__(self):
        self.utils = Utils()
        self.serviceUrl = configs.serviceUrl

    def getWeight(self):
        print "\nGetting weight..."
        startDate = raw_input("Enter a start date: ")
        endDate = raw_input("Enter an end date: ")
        
        if not self.utils.isValidDate(startDate):
            raise Exception("Not a valid start date! " + 
                "Please enter a valid date.")

        # We allow endDate to be an empty string because in this case the
        # service assumes that we want until the present date
        if not self.utils.isValidDate(endDate) and endDate != "":
            raise Exception("Not a valid end date! " + 
                "Please enter a valid date or empty.")

        query = self.serviceUrl + "/health/weight?startDate="
        query = query + startDate

        if endDate != "":
            query = query + "&endDate=" + endDate

        r = requests.get(query)

        if r.status_code != 200:
            raise Exception("Status Code: " + str(r.status_code) + 
                " Service is not happy with our request. Try again.")
            
        response = json.loads(r.text.encode("utf-8"))
        print "\n"

        print "Date\t\tWeight(Lbs)"
        print "==============================="
        for data in response["weights"]:
            print data["date"] + "\t" + str(data["weight"])

class Menu:
    """Base menu class"""

    def __init__(self):
        self.options = []

    def display(self):
        print "Select an option:\n====================="
        for idx, opt in enumerate(self.options):
            print str(idx) + "\t" + opt[0]

    def run(self, option, menuStack):
        raise Exception("run function not implemented")

class MainMenu(Menu):
    """Class to handle main menu related actions""" 

    def __init__(self):
        Menu.__init__(self)

        self.options.append(("Go to Health", HealthMenu()))
        self.options.append(("Exit", None))

    def run(self, option, menuStack):
        if (self.options[option][0] == "Exit"):
            sys.exit()

        menuStack.append(self)

        choice = self.options[option]
        if isinstance(choice[1], Menu):
            menuStack.append(choice[1])
            return 

class HealthMenu(Menu):
    """Class to handle health menu related actions"""

    def __init__(self):
        Menu.__init__(self)

        self.options.append(("Go to Weight", WeightMenu()))
        self.options.append(("Return", None))

    def run(self, option, menuStack):
        
        if (self.options[option][0] == "Return"):
            return

        menuStack.append(self)

        choice = self.options[option]
        if isinstance(choice[1], Menu):
            menuStack.append(choice[1])
            return 

class WeightMenu(Menu):
    """Class to handle weight menu related actions"""
    
    def __init__(self):
        Menu.__init__(self)

        self.weightHandler = WeightHandler()
        self.options.append(("Get weight", None))
        self.options.append(("Return", None))

    def run(self, option, menuStack):
        
        if (self.options[option][0] == "Return"):
            return

        menuStack.append(self)

        choice = self.options[option]
        if isinstance(choice[1], Menu):
            menuStack.append(choice[1])
            return 

        if (choice[0] == "Get weight"):
            self.weightHandler.getWeight()

if __name__ == "__main__":
    """Entry point"""
    
    print ""
    menuStack = []

    while True: 

        if not menuStack:
            menuStack.append(MainMenu())

        currMenu = menuStack.pop()
        currMenu.display()

        userInput = raw_input("=====================\nRun: ")

        try:
            currMenu.run(int(userInput), menuStack)
        except Exception as ex:
            print ex
        print "\n"
