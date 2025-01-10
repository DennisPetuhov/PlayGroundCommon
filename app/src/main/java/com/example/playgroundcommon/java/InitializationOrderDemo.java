package com.example.playgroundcommon.java;

class Parent {
    // Static field
    static String staticParentField = initializeStaticParentField();

    // Instance field
    String instanceParentField = initializeInstanceParentField();

    // Static initialization block
    static {
        System.out.println("Static block of parent");
    }

    // Instance initialization block
    {
        System.out.println("Instance block of parent");
    }

    // Constructor of the parent class
    public Parent() {
        System.out.println("Constructor of parent");
    }

    // Static method to initialize static field
    static String initializeStaticParentField() {
        System.out.println("Initializing static field of parent");
        return "staticParentField";
    }

    // Method to initialize instance field
    String initializeInstanceParentField() {
        System.out.println("Initializing instance field of parent");
        return "instanceParentField";
    }
}

class Child extends Parent {
    // Static field
    static String staticChildField = initializeStaticChildField();

    // Instance field
    String instanceChildField = initializeInstanceChildField();

    // Static initialization block
    static {
        System.out.println("Static block of child");
    }

    // Instance initialization block
    {
        System.out.println("Instance block of child");
    }

    // Constructor of the child class
    public Child() {
        System.out.println("Constructor of child");
    }

    // Static method to initialize static field
    static String initializeStaticChildField() {
        System.out.println("Initializing static field of child");
        return "staticChildField";
    }

    // Method to initialize instance field
    String initializeInstanceChildField() {
        System.out.println("Initializing instance field of child");
        return "instanceChildField";
    }
}

public class InitializationOrderDemo {
    public static void main(String[] args) {
        System.out.println("Creating child object:");
        Child child = new Child();
    }
}