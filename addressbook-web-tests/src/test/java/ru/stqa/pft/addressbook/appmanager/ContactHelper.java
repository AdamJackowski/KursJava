package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

import static org.testng.Assert.assertFalse;


public class ContactHelper extends HelperBase {

  private Contacts contactCache = null;

  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void submitContactCreation() {
    click(By.name("submit"));
  }

  public void submitContactModification() {
    click(By.name("update"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFirstName());
    type(By.name("middlename"), contactData.getMiddleName());
    type(By.name("lastname"), contactData.getLastName());
    attach(By.name("photo"), contactData.getPhoto());
    type(By.name("nickname"), contactData.getNickname());
    type(By.name("title"), contactData.getTitle());
    type(By.name("company"), contactData.getCompany());
    type(By.name("address"), contactData.getAddress());
    type(By.name("home"), contactData.getHomePhone());
    type(By.name("mobile"), contactData.getMobilePhone());
    type(By.name("work"), contactData.getWorkPhone());
    type(By.name("fax"), contactData.getFaxNumber());
    type(By.name("email"), contactData.getMailAdress1());
    type(By.name("email2"), contactData.getMailAdress2());
    type(By.name("email3"), contactData.getMailAdress3());

    if (creation) {
      if (contactData.getGroups().size() > 0) {
        Assert.assertTrue(contactData.getGroups().size() == 1);
        new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroups().iterator().next().getName());
      } else {
        assertFalse(isElementPresent(By.name("new_group")));
      }
    }
  }

  public void returnToHomePage() {
    click(By.linkText("home page"));
  }

  public void initContactCreation() {
    click(By.linkText("add new"));
  }

  public void initContactModification() {
    click(By.xpath("//img[@alt='Edit']"));
  }

  public void initContactModificationById(int id) {

    getRowCellsById(id).get(7).findElement(By.tagName("a")).click();
  }

  public boolean isThereAContact() {
    return isElementPresent(By.xpath("//img[@alt='Edit']"));
  }

  public void deleteSelectedContacts() {
    click(By.xpath("//input[@value='Delete']"));
    closeAlert();
  }

  public void selectContactById(int id) {
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }

  public void create(ContactData contact) {
    initContactCreation();
    fillContactForm(contact, true);
    submitContactCreation();
    contactCache = null;
    returnToHomePage();
  }

  public void modify(ContactData contact) {
    initContactModificationById(contact.getId());
    fillContactForm(contact, false);
    submitContactModification();
    contactCache = null;
    returnToHomePage();
  }

  public void delete(ContactData contact) {
    selectContactById(contact.getId());
    click(By.xpath("//input[@value='Delete']"));
    closeAlert();
    contactCache = null;
  }
  public void showDetailsById(int id)
  {
    getRowCellsById(id).get(6).findElement(By.tagName("a")).click();

  }

  private List<WebElement> getRowCellsById(int id) {
    WebElement checkbox = wd.findElement(By.cssSelector(String.format("input[value='" + id + "']")));
    WebElement row = checkbox.findElement(By.xpath("./../.."));
    return row.findElements(By.tagName("td"));
  }

  public Contacts all() {

    if(contactCache != null)
    {
      return new Contacts(contactCache);
    }
    contactCache = new Contacts();
    List<WebElement> elements = wd.findElements(By.name("entry"));
    for (WebElement element : elements) {
      int id = Integer.parseInt(element.findElement(By.name("selected[]")).getAttribute("value"));

      List<WebElement> cells = element.findElements(By.tagName("td"));
      String firstname = cells.get(2).getText();
      String lastname = cells.get(1).getText();
      String adress = cells.get(3).getText();
      String allMails = cells.get(4).getText();
      String allPhones = cells.get(5).getText();
      contactCache.add(new ContactData().withId(id)
              .withFirstName(firstname)
              .withLastName(lastname)
              .withAddress(adress)
              .withAllMails(allMails)
              .withAllPhones(allPhones)
              .inGroup(new GroupData().withName("test1")));
    }
    return contactCache;
  }

  private void closeAlert() {
    wd.switchTo().alert().accept();
    wd.findElement(By.cssSelector("div.msgbox"));
  }

  public ContactData infoFromEditForm(ContactData contact) {
    initContactModificationById(contact.getId());
    String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
    String nickname = wd.findElement(By.name("nickname")).getAttribute("value");
    String address = wd.findElement(By.name("address")).getAttribute("value");
    String mailadress1 = wd.findElement(By.name("email")).getAttribute("value");
    String mailadress2 = wd.findElement(By.name("email2")).getAttribute("value");
    String mailadress3 = wd.findElement(By.name("email3")).getAttribute("value");
    String home = wd.findElement(By.name("home")).getAttribute("value");
    String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String work = wd.findElement(By.name("work")).getAttribute("value");
    return new ContactData().withId(contact.getId())
            .withFirstName(firstname)
            .withLastName(lastname)
            .withNickname(nickname)
            .withAddress(address)
            .withMailAdress1(mailadress1)
            .withMailAdress2(mailadress2)
            .withMailAdress3(mailadress3)
            .withHomePhone(home)
            .withMobilePhone(mobile)
            .withWorkPhone(work);
  }
  public ContactData contactInfo () {
    WebElement content = wd.findElement(By.id("content"));
    String allInfo= content.getText();
    return new ContactData().withAllInfo(allInfo);
  }

  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }
}
