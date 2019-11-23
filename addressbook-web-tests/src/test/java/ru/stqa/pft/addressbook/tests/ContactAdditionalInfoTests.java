package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAdditionalInfoTests extends TestBase{

  public static String cleaned(String field) {
    return field.replaceAll("\\s", "")
            .replaceAll("\\n", "");
  }

  @Test
  public void testAdditionalInfo() {
    app.goTo().gotoHomePage();
    ContactData contact = app.contact().all().iterator().next();
    ContactData contactToCompare = app.contact().infoFromEditForm(contact);
    app.goTo().gotoHomePage();
    app.contact().showDetailsById(contact.getId());
    ContactData contactInfo = app.contact().contactInfo();
    assertThat(cleaned(contactInfo.getAllInfo()), equalTo(mergeAllData(contactToCompare)));
  }

  private String mergeAllData(ContactData contact){
    String  homePhone = "";
    String mobilePhone = "";
    String workPhone = "";
    String groupMember = "Member of: test 1";
    if(!contact.getHomePhone().equals(""))
      homePhone = "H: " + contact.getHomePhone();
    if(!contact.getMobilePhone().equals(""))
      mobilePhone = "M: "+ contact.getMobilePhone();
    if(!contact.getWorkPhone().equals(""))
      workPhone = "W:" + contact.getWorkPhone();

    return Arrays.asList(contact.getFirstName()+ contact.getLastName(),contact.getNickname() ,contact.getAddress(), homePhone, mobilePhone, workPhone, contact.getMailAdress1(),contact.getMailAdress2(), contact.getMailAdress3(), groupMember)
            .stream().filter((s) -> !s.equals(""))
            .map(ContactAdditionalInfoTests::cleaned)
            .collect(Collectors.joining(""));
  }
}
