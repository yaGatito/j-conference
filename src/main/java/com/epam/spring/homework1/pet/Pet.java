package com.epam.spring.homework1.pet;

import com.epam.spring.homework1.pet.api.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Pet {

    private List<Animal> animals;

    @Autowired
    public Pet(List<Animal> animals) {
        this.animals = animals;
    }

    public void printPets(){
        for (Animal animal : animals) {
            System.out.println(animal.getClass().getSimpleName());
        }
    }
}
