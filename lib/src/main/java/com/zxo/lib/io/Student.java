package com.zxo.lib.io;

import java.io.Serializable;

public class Student extends Person implements Serializable {

  private transient int studio;
  private Person person;

  public Student(String name, String age, String sex, String num) {
    super(name, age, sex, num);
    studio = 987;
    person = new Person(name,age,sex,num);
  }

  public Student(String name, String age, String sex, String num, int studio, Person person) {
    super(name, age, sex, num);
    this.studio = studio;
    this.person = person;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public String toString() {
    return "Student{" + "studio=" + studio + ", person=" + person + ", name='" + name + '\'' + ", age='" + age + '\'' + ", sex='" + sex + '\'' + '}';
  }
}
