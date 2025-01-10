//package com.example.playgroundcommon.generics;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GenericsMy {
//}
//
//class Animal {}
//class Cat extends Animal {}
//
//void feedAnimals(List<Animal> animals) {
//    // Логика кормления животных
//}
//
//List<Cat> cats = new ArrayList<>();
//// feedAnimals(cats);  // Ошибка компиляции: List<Cat> не является List<Animal>
//void feedAnimals(List<? extends Animal> animals) {
//    for (Animal animal : animals) {
//        // Логика кормления
//    }
//}
//
//List<Cat> cats = new ArrayList<>();
//feedAnimals(cats);  // Это работает, так как List<? extends Animal> позволяет передать List<Cat>
//
//
//
//void processAnimals(List<? super Cat> animals) {
//    animals.add(new Cat());
//    // Нельзя безопасно прочитать объект как Cat или Animal, только как Object
//}
//
//List<Animal> animals = new ArrayList<>();
//processAnimals(animals);  // Это работает, так как List<Animal> можно использовать как List<? super Cat>
//List<Animal> animals = new ArrayList<>();
//List<Cat> cats = new ArrayList<>();
//// animals = cats;  // Ошибка компиляции