package academy.devdojo.springboot2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import academy.devdojo.springboot2.entities.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long>{
}
