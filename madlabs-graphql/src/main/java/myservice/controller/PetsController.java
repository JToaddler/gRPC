package myservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;
import myservice.model.Address;
import myservice.model.Person;
import myservice.model.Pet;
import myservice.model.PetFilter;
import myservice.model.PetInput;
import myservice.model.PetKind;

@Controller
public class PetsController {

	private Map<String, Pet> petMap = new HashMap<>();
	private final Map<String, Person> personMap = new HashMap<>();

	public PetsController() {

		Address address = new Address("Main Street", "New York", "NY", "10001");
		Person bob = new Person("Bob", address);
		Person alice = new Person("Alice");

		personMap.put(bob.getName(), bob);
		personMap.put(alice.getName(), alice);

		Pet luna = new Pet("Luna", "coffe", PetKind.CAT, alice);
		Pet skipper = new Pet("Skipper", "black", PetKind.DOG, bob);

		petMap.put(luna.getName(), luna);
		petMap.put(skipper.getName(), skipper);
		petMap.put("Skipper", skipper);

	}

	@QueryMapping()
	public List<Pet> getPet(@Argument String name) {
		if (name != null && !name.isEmpty()) {
			return List.of(this.petMap.get(name));
		} else
			return this.petMap.values().stream().collect(Collectors.toList());
	}

	@QueryMapping()
	public List<Pet> searchPet(@Argument PetFilter filter) {
		if (filter == null) {
			return this.petMap.values().stream().collect(Collectors.toList());
		}
		return this.petMap.values().stream().filter(pet -> {
			return (filter.name() == null ? true : StringUtils.equalsIgnoreCase(pet.getName(), filter.name()));
		}).filter(pet -> {
			return filter.color() == null ? true : StringUtils.equalsIgnoreCase(pet.getColor(), filter.color());
		}).filter(pet -> {
			return filter.kind() == null ? true : filter.kind().equals(pet.getKind());
		}).collect(Collectors.toList());
	}

	@QueryMapping
	public List<Person> searchOwner(DataFetchingEnvironment env) {
		Map<String, Object> inputMap = env.getArgument("ownerFilter");
		inputMap.entrySet().stream().forEach(entry -> {
			System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
		});
		return this.personMap.values().stream().collect(Collectors.toList());
	}

	@MutationMapping()
	public List<Pet> addPet(@Argument PetInput petInput) {
		Pet pet = new Pet(petInput.name(), petInput.color(), petInput.kind());
		this.petMap.put(pet.getName(), pet);
		return this.petMap.values().stream().collect(Collectors.toList());
	}

	@MutationMapping()
	public List<Person> addOwner(@Argument Person owner) {
		personMap.put(owner.getName(), owner);
		return this.personMap.values().stream().collect(Collectors.toList());
	}

	@MutationMapping()
	public Pet updateOwner(@Argument String petName, @Argument String ownerName) {
		Pet pet = this.petMap.get(petName);

		if (pet == null) {
			return null;
		}
		Person owner = this.personMap.get(ownerName);
		if (owner == null) {
			return null;
		}
		pet.setOwner(owner);
		return pet;
	}

}
