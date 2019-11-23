package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class ResetHelper extends HelperBase {
  public ResetHelper(ApplicationManager app) {
    super(app);
  }

  public void changePassword (String confirmationLink, String newPassword)
  {
    wd.get(confirmationLink);
    type(By.name("password"), newPassword);
    type(By.name("password_confirm"), newPassword);
    click(By.cssSelector("input[value='Update User']"));
  }
}

