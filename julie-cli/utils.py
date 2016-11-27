import datetime

class Utils:

    def isValidDate(self, date):
        """Function to validate whether a passed date string is a valid date"""
        try:
            datetime.datetime.strptime(date, '%Y-%m-%d')
            return True
        except Exception:
            return False
