package com.example.xcf.entity;

/**
 * Created by pjy on 2017/6/2.
 */

public class Employee {
    private int id;
    private String name;
    private double salary;
    private int age;
    private String gender;

    public Employee() {
    }

    public Employee(int id, String name, double salary, int age, String gender) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
