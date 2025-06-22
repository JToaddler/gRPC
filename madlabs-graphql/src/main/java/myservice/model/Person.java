package myservice.model;

public class Person {

	private String name;
	private Integer age;
	private Address address;

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Person() {
	}

	public Person(String name, Integer age, Address address) {
		super();
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public Person(String name, Address address) {
		this.name = name;
		this.address = address;
	}

	public Person(String name) {
		this(name, null);
	}

}
