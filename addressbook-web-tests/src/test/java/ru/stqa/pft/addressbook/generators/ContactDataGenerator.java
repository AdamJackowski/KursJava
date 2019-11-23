package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ContactDataGenerator {
  @Parameter(names = "-c", description = "Contact count")
  public int count;

  @Parameter(names = "-f", description = "Target file")
  public String file;

  @Parameter(names = "-d", description = "Data format")
  public String format;

//  @Parameter(names = "-p", description = "Photo path")
//  public String path;

  static Properties properties;

  public static void main(String[] args) throws IOException {
    String target = System.getProperty("target", "local");
    properties = new Properties();
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
    ContactDataGenerator generator = new ContactDataGenerator();
    JCommander jCommander = new JCommander(generator);
    try {
      jCommander.parse(args);
    } catch (ParameterException ex) {
      jCommander.usage();
      return;
    }

    generator.run();
  }

  private void run() throws IOException {
    List<ContactData> contacts = generateContacts(count);
    if (format.equals("csv")) {
      saveAsCsv(contacts, new File(file));
    } else if (format.equals("xml")) {
      saveAsXml(contacts, new File(file));
    } else if (format.equals("json")) {
      saveAsJson(contacts, new File(file));
    } else {
      System.out.println("Unrecognized format" + format);
    }
  }

  private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE).create();
    String json = gson.toJson(contacts);

    try(Writer writer = new FileWriter(file)) {
      writer.write(json);
    }
  }

  private void saveAsXml(List<ContactData> contacts, File file) throws IOException {
    XStream xStream = new XStream();
    xStream.processAnnotations(ContactData.class);
    String xml = xStream.toXML(contacts);
    try(Writer writer = new FileWriter(file)) {
      writer.write(xml);
    }
  }

  private void saveAsCsv(List<ContactData> contacts, File file) throws IOException {
    try(Writer writer = new FileWriter(file)) {
      for (ContactData contact : contacts) {
        writer.write(String.format("%s; %s; %s; %s; %s; %s; %s %s; %s; %s\n", contact.getFirstName(), contact.getLastName(), contact.getNickname(), contact.getAddress(), contact.getPhoto().getPath(), contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getMailAdress1(), contact.getMailAdress2(), contact.getMailAdress3()));
      }
    }
  }

  private List<ContactData> generateContacts(int count) {
    List<ContactData> contacts = new ArrayList<ContactData>();
    for (int i = 0; i < count; i++) {
      contacts.add(new ContactData().withFirstName(properties.getProperty("contactFirstName") + i)
              .withLastName(properties.getProperty("contactLastName") + i)
              .withNickname(properties.getProperty("contactNickname") + i)
              .withAddress(properties.getProperty("contactAddress") + i)
              .withPhoto(new File(properties.getProperty("photoPath")))
              .withMailAdress1(properties.getProperty("contactMailAddress1") + i)
              .withMailAdress2(properties.getProperty("contactMailAddress2") + i)
              .withMailAdress3(properties.getProperty("contactMailAddress3") + i)
              .withHomePhone(properties.getProperty("contactHomePhone") + i)
              .withMobilePhone(properties.getProperty("contactMobilePhone") + i)
              .withWorkPhone(properties.getProperty("contactWorkPhone") + i)
              .inGroup(new GroupData().withName(properties.getProperty("contactGroup") + i)));
    }
    return contacts;

  }
}
