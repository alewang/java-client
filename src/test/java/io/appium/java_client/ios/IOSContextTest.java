/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.appium.java_client.ios;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.NoSuchContextException;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.junit.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Test context-related features
 */
public class IOSContextTest {

  private static AppiumDriver<?> driver;
  private static AppiumDriverLocalService service;

  @BeforeClass
  public static void beforeClass() throws Exception{
     service = AppiumDriverLocalService.buildDefaultService();
     service.start();

    if (service == null || !service.isRunning())
      throw new RuntimeException("An appium server node is not started!");

    File appDir = new File("src/test/java/io/appium/java_client");
    File app = new File(appDir, "WebViewApp.app.zip");
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
    capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "8.4");
    capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone Simulator");
    capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
    driver = new IOSDriver<WebElement>(service.getUrl(), capabilities);
  }

  @AfterClass
  public static void tearDown() throws Exception {
    if (driver != null) {
      driver.quit();
    }

    if (service.isRunning()) {
      service.stop();
    }
  }

  @Test
  public void testGetContext() {
    assertEquals("NATIVE_APP", driver.getContext());
  }

  @Test
  public void testGetContextHandles() {
    assertEquals(driver.getContextHandles().size(), 2);
  }

  @Test
  public void testSwitchContext() {
    driver.getContextHandles();
    driver.context("WEBVIEW_1");
    assertEquals(driver.getContext(), "WEBVIEW_1");
    driver.context("NATIVE_APP");
  }

  @Test(expected = NoSuchContextException.class)
  public void testContextError() {
    driver.context("Planet of the Ape-ium");
  }
}
