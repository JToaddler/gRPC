package myservice.model;

public class Pet {

	private String name;
	private String color;
	private PetKind kind;
	private Person owner;

	public Pet(String name, String color, PetKind kind, Person owner) {
		this.name = name;
		this.color = color;
		this.kind = kind;
		this.owner = owner;
	}

	public Pet(String name, String color, PetKind kind) {
		this(name, color, kind, null);
	}

	public Pet(String name, String color) {
		this(name, color, null, null);
	}

	public Pet(String name) {
		this(name, null, null, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public PetKind getKind() {
		return kind;
	}

	public void setKind(PetKind kind) {
		this.kind = kind;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}
}
