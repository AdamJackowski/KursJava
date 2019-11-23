package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

  private final Properties properties;
  private WebDriver wd;
  private String browser;
  private RegistationHelper registrationHelper;
  private FtpHelper ftp;
  private MailHelper mailHelper;
  private ResetHelper resetHelper;
  private SessionHelper sessionHelper;
  private HttpSession httpSession;


  public ApplicationManager(String browser) {

    this.browser = browser;
    properties = new Properties();
  }

  public void init() throws IOException {
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));

    }

  public void stop() {
    if (wd != null) {
      wd.quit();
    }
  }

  public HttpSession newSession()
  {
    return new HttpSession(this);
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public RegistationHelper registration() {
    if (registrationHelper == null) {
      registrationHelper = new RegistationHelper(this);
    }
    return registrationHelper;
  }

  public FtpHelper ftp()
  {
    if(ftp == null) {
      ftp = new FtpHelper(this);
    }
    return ftp;
  }
  public WebDriver getDriver() {
    if (wd == null) {
      if (browser.equals(BrowserType.FIREFOX)) {
        wd = new FirefoxDriver();
      } else if (browser.equals(BrowserType.CHROME)) {
        wd = new ChromeDriver();
      } else if (browser.equals(BrowserType.EDGE)) {
        wd = new EdgeDriver();
      }
      wd.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
      wd.get(properties.getProperty("web.baseUrl"));
    }
    return wd;
  }

  public MailHelper mail()
  {
    if (mailHelper == null)
    {
      mailHelper = new MailHelper(this);
    }
    return mailHelper;
  }
  public ResetHelper reset() {
    if (resetHelper == null) {
      resetHelper = new ResetHelper(this);
    }
    return resetHelper;
  }

  public SessionHelper session() {
    if (sessionHelper == null) {
      sessionHelper = new SessionHelper(this);
    }
    return sessionHelper;
  }
  public HttpSession httpSession() {
    if (httpSession == null) {
      httpSession = new HttpSession(this);
    }
    return httpSession;
  }
}
