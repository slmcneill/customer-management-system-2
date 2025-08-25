from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import WebDriverException, TimeoutException
import time

KEEP_BROWSER_OPEN = True
WAIT_TIME = 20  # max seconds for waits

# Helper pause to slow down execution
def pause(seconds=2):
    time.sleep(seconds)

try:
    driver = webdriver.Chrome()
    driver.get("http://localhost:3000/login")
    driver.implicitly_wait(5)

    # ---------- LOGIN AS ADMIN ----------
    username_field = WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = driver.find_element(By.NAME, "password")
    login_button = driver.find_element(By.CSS_SELECTOR, "button")

    username_field.send_keys("benny")
    password_field.send_keys("pass456")
    login_button.click()
    pause(2)

    WebDriverWait(driver, WAIT_TIME).until(EC.url_contains("/app"))
    print("✅ Logged in as admin")

    # ---------- ADD CUSTOMER ----------
    name_input = WebDriverWait(driver, WAIT_TIME).until(
        EC.visibility_of_element_located((By.NAME, "name"))
    )
    email_input = driver.find_element(By.NAME, "email")
    password_input = driver.find_element(By.NAME, "password")

    name_input.send_keys("Lucy Heart")
    email_input.send_keys("lucy@example.com")
    password_input.send_keys("pass123")

    save_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//input[@type='button' and @value='Save']"))
    )
    save_button.click()
    pause(3)

    WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.XPATH, "//tr[td[contains(text(), 'Lucy Heart')]]"))
    )
    print("✅ Customer added: Lucy Heart")

    # ---------- UPDATE CUSTOMER ----------
    customer_row = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//tr[td[contains(text(), 'Lucy Heart')]]"))
    )
    customer_row.click()
    pause(1)

    name_input = WebDriverWait(driver, WAIT_TIME).until(
        EC.visibility_of_element_located((By.NAME, "name"))
    )
    email_input = driver.find_element(By.NAME, "email")
    password_input = driver.find_element(By.NAME, "password")

    name_input.clear()
    name_input.send_keys("Lucy Star")
    email_input.clear()
    email_input.send_keys("lucy.star@example.com")
    password_input.clear()
    password_input.send_keys("newpass456")

    save_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//input[@type='button' and @value='Save']"))
    )
    save_button.click()
    pause(3)

    WebDriverWait(driver, WAIT_TIME).until_not(
        EC.presence_of_element_located((By.XPATH, "//tr[td[contains(text(), 'Lucy Heart')]]"))
    )
    WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.XPATH, "//tr[td[contains(text(), 'Lucy Star')]]"))
    )
    print("✅ Customer updated: Lucy Star")

    # ---------- LOGOUT AS ADMIN ----------
    current_username = "benny"
    logout_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, f"//button[contains(text(),'{current_username}')]"))
    )
    logout_button.click()

    alert = WebDriverWait(driver, WAIT_TIME).until(EC.alert_is_present())
    alert.accept()
    pause(2)
    print("✅ Logged out as admin")

    # ---------- LOGIN AS CUSTOMER ----------
    username_field = WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = driver.find_element(By.NAME, "password")
    login_button = driver.find_element(By.CSS_SELECTOR, "button")

    username_field.send_keys("Lucy Star")
    password_field.send_keys("newpass456")
    login_button.click()
    pause(2)

    WebDriverWait(driver, WAIT_TIME).until(EC.url_contains("/app"))
    print("✅ Logged in as updated customer")

    # ---------- LOGOUT AS CUSTOMER ----------
    current_username = "Lucy Star"
    logout_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, f"//button[contains(text(),'{current_username}')]"))
    )
    logout_button.click()

    alert = WebDriverWait(driver, WAIT_TIME).until(EC.alert_is_present())
    alert.accept()
    pause(2)
    print("✅ Logged out as customer")

    # ---------- LOGIN AGAIN AS ADMIN ----------
    username_field = WebDriverWait(driver, WAIT_TIME).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = driver.find_element(By.NAME, "password")
    login_button = driver.find_element(By.CSS_SELECTOR, "button")

    username_field.send_keys("benny")
    password_field.send_keys("pass456")
    login_button.click()
    pause(2)

    WebDriverWait(driver, WAIT_TIME).until(EC.url_contains("/app"))
    print("✅ Logged back in as admin")

    # ---------- DELETE CUSTOMER ----------
    customer_row = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//tr[td[contains(text(), 'Lucy Star')]]"))
    )
    customer_row.click()
    pause(1)

    delete_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, "//input[@type='button' and @value='Delete']"))
    )
    delete_button.click()
    pause(3)

    WebDriverWait(driver, WAIT_TIME).until_not(
        EC.presence_of_element_located((By.XPATH, "//tr[td[contains(text(), 'Lucy Star')]]"))
    )
    print("✅ Customer deleted: Lucy Star")

    # ---------- FINAL LOGOUT ----------
    current_username = "benny"
    logout_button = WebDriverWait(driver, WAIT_TIME).until(
        EC.element_to_be_clickable((By.XPATH, f"//button[contains(text(),'{current_username}')]"))
    )
    logout_button.click()

    alert = WebDriverWait(driver, WAIT_TIME).until(EC.alert_is_present())
    alert.accept()
    pause(2)
    print("✅ Logged out as admin (final)")

    # ---------- CLEANUP ----------
    if KEEP_BROWSER_OPEN:
        input("Press Enter to close the browser...")

    driver.quit()
    print("🎉 Script executed successfully!")

except (WebDriverException, TimeoutException) as e:
    print(f"Selenium Exception: {e}")
    driver.quit()
