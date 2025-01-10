package com.example.playgroundcommon.jooqLike;

class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this; // возвращаем текущий объект для цепочки вызовов
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this; // возвращаем текущий объект
    }

    public Person setAge(int age) {
        this.age = age;
        return this; // возвращаем текущий объект
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}

public class FluentApiExample {
    public static void main(String[] args) {
        Person person = new Person()
                .setFirstName("John")
                .setLastName("Doe")
                .setAge(30);

        System.out.println(person);

        String s = new Person()
                .setFirstName("John")
                .setLastName("Doe")
                .setAge(30).toString();
    }
}