package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GroupDataGenerator extends TestBase {

  @Parameter(names = "-c", description = "Group count")
  public int count;

  @Parameter(names = "-f",description = "Target file")
  public String file;

  @Parameter(names = "-d",description = "Data format")
  public String format;

  static Properties properties;

  public static void main(String[] args) throws IOException {
    String target = System.getProperty("target", "local");
    properties = new Properties();
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));

    GroupDataGenerator generator = new GroupDataGenerator();
    JCommander jCommander = new JCommander(generator);
   try {
     jCommander.parse(args);
   }
    catch (ParameterException ex)
    {
      jCommander.usage();
      return;
    }

    generator.run();
  }

  private void run() throws IOException {
    List<GroupData> groups = generateGroups(count);
    if(format.equals("csv")) {
      saveAsCsv(groups, new File(file));
    }
    else if (format.equals("xml"))
    {
      saveAsXml(groups, new File(file));
    }
    else if (format.equals("json"))
    {
      saveAsJson(groups, new File(file));
    }
    else
    {
      System.out.println("Unrecognized format" + format);
    }
  }

  private void saveAsJson(List<GroupData> groups, File file) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    String json = gson.toJson(groups);
    try(Writer writer = new FileWriter(file)) {
      writer.write(json);
    }
  }

  private void saveAsXml(List<GroupData> groups, File file) throws IOException {
    XStream xStream = new XStream();
    xStream.processAnnotations(GroupData.class);
    String xml = xStream.toXML(groups);
    try(Writer writer = new FileWriter(file)) {
      writer.write(xml);
    }

  }

  private void saveAsCsv(List<GroupData> groups, File file) throws IOException {
    try(Writer writer = new FileWriter(file)) {
      for (GroupData group : groups) {
        writer.write(String.format("%s;%s;%s\n", group.getName(), group.getHeader(), group.getFooter()));
      }
    }
  }

  private List<GroupData> generateGroups(int count) {
    List<GroupData> groups = new ArrayList<GroupData>();
    for (int i = 0; i < count; i++) {
      groups.add(new GroupData().withName(properties.getProperty("groupName") + i)
              .withHeader(properties.getProperty("groupHeader") + i)
              .withFooter(properties.getProperty("groupFooter") + i));
    }
    return groups;

  }
}
