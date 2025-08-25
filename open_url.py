from selenium import webdriver
import time

# Initialize the Chrome driver
driver = webdriver.Chrome()

# Open the Selenium website
driver.get("https://www.selenium.dev/")

# Wait for 10 seconds
time.sleep(10)

# Prompt user to continue
#input("Press OK to close the browser and quit the program...")

# Close the browser
driver.quit()
