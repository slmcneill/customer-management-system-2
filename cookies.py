from selenium import webdriver
from selenium.common.exceptions import WebDriverException

KEEP_BROWSER_OPEN = True  # Set this to True to keep the browser open after script execution
USE_OPTIONS = False  # Set this to True to enable mamximise
try:
    if USE_OPTIONS:
        options = webdriver.ChromeOptions()
        options.add_argument("--start-maximized")
        # Initialize the Chrome driver with options
        driver = webdriver.Chrome(options=options)
    else:
        # Initialize the Chrome driver with no options
        driver = webdriver.Chrome()

    # Attempt to open the localhost:3000 page
    driver.get("http://localhost:3000")

    # Adds the cookie into current browser context
    driver.add_cookie({"name": "mytest", "value": "my test value"})
    
    # If KEEP_BROWSER_OPEN is True, keep the browser open for inspection
    if KEEP_BROWSER_OPEN:
        input("Press Enter to close the browser...")

    # Close the browser
    driver.quit()
    print("Script executed successfully!")

except WebDriverException as e:
    print(f"WebDriverException: {e}")
    if "ERR_CONNECTION_REFUSED" in str(e):
        print("Connection refused: Ensure the server at 'http://localhost:3000' is running.")
    else:
        print("Other WebDriverException occurred. See details above.")
    try:
        driver.quit()  # Ensure browser is closed in case of exception
    except:
        pass  # Handle exception if driver.quit() fails
