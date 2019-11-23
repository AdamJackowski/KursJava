package ru.stqa.pft.mantis.tests;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.openqa.selenium.By.cssSelector;

public class ResetPassword extends TestBase {
  @BeforeMethod
  public void startMailServer()
  {
    app.mail().start();
  }
  @Test
  public void testRegistration() throws IOException, MessagingException {
    String user = app.getProperty("web.adminLogin");
    String password = app.getProperty("web.adminPassword");
    String emailToChange;
    String userToChange;
    String newPassword = password +"1";
    app.session().login(user, password);
    app.session().click(By.linkText("Manage Users"));
    // I choose always the second one form a table
    app.session().click(By.xpath("//tr[4]/td/a"));
    emailToChange  = app.getDriver().findElement(By.name("email")).getAttribute("value");
    userToChange = app.getDriver().findElement(By.name("username")).getAttribute("value");
    app.session().click(cssSelector("input[value='Reset Password']"));
    List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
    String confirmationLink = findConfirmationLink(mailMessages, emailToChange);
    app.reset().changePassword(confirmationLink, newPassword);
    app.httpSession().login(userToChange, newPassword);
    app.httpSession().isLoggedInAs(userToChange);


  }
  private String findConfirmationLink(List<MailMessage> mailMessages, String email)
  {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);
  }


  @AfterMethod(alwaysRun = true)
  public void stopMailServer ()
  {
    app.mail().stop();
  }
}

