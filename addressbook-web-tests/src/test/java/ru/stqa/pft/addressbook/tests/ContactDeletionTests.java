package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;


public class ContactDeletionTests extends TestBase {
  @BeforeMethod
  public void ensurePrecondicions() {
    if(app.db().contacts().size() == 0)
    {
      app.goTo().gotoHomePage();
      app.contact().create(new ContactData().withFirstName(app.properties().getProperty("contactFirstName"))
              .withLastName(app.properties().getProperty("contactLastName"))
              .withNickname(app.properties().getProperty("contactNickname"))
              .withAddress(app.properties().getProperty("contactAddress"))
              .withPhoto(new File(app.properties().getProperty("photoPath")))
              .withMailAdress1(app.properties().getProperty("contactMailAddress1"))
              .withMailAdress2(app.properties().getProperty("contactMailAddress2"))
              .withMailAdress3(app.properties().getProperty("contactMailAddress3"))
              .withHomePhone(app.properties().getProperty("contactHomePhone"))
              .withMobilePhone(app.properties().getProperty("contactMobilePhone"))
              .withWorkPhone(app.properties().getProperty("contactWorkPhone"))
              .inGroup(new GroupData().withName(app.properties().getProperty("contactGroup"))));
    }
  }

  @Test
  public void testContactDeletion() {
    Contacts before = app.db().contacts();
    ContactData deletedContact = before.iterator().next();
    app.contact().delete(deletedContact);
    Contacts after = app.db().contacts();
    assertEquals(after.size(), before.size() - 1);
    assertThat(after, equalTo(before.without(deletedContact)));
  }
}