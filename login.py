from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import WebDriverException, TimeoutException

KEEP_BROWSER_OPEN = True  # Set this to True to keep the browser open after script execution

try:
    # Initialize the Chrome driver
    driver = webdriver.Chrome()

    # Attempt to open the localhost:3000/login page
    driver.get("http://localhost:3000/login")

    # Wait for username and password input fields to be present
    username_field = WebDriverWait(driver, 3000).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = WebDriverWait(driver, 3000).until(
        EC.presence_of_element_located((By.NAME, "password"))
    )
    login_button = WebDriverWait(driver, 3000).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, "button"))
    )

    # Enter username and password
    username_field.send_keys("benny")
    password_field.send_keys("pass456")

    # Submit the form
    login_button.click()

    # Wait for navigation or URL change after login
    WebDriverWait(driver, 10).until(
        EC.url_contains("/app")
    )

    # Get the current URL after login
    current_url = driver.current_url
    print(f"Current URL after login: {current_url}")

    # Assert that we are now in the profile page
    assert "/app" in current_url, f"Expected '/app' in URL but got: {current_url}"

    # If KEEP_BROWSER_OPEN is True, keep the browser open for inspection
    if KEEP_BROWSER_OPEN:
        input("Press Enter to close the browser...")

    # Close the browser
    driver.quit()
    print("Script executed successfully!")

except (WebDriverException, TimeoutException) as e:
    print(f"Selenium Exception: {e}")
    if "ERR_CONNECTION_REFUSED" in str(e):
        print("Connection refused: Ensure the server at 'http://localhost:3000' is running.")
    else:
        print("Other WebDriverException occurred. See details above.")
    
    # Close the browser in case of exception
    driver.quit()
