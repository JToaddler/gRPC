package myservice.model;

public enum PetKind {

	CAT, DOG, BIRD, FISH, REPTILE;

	public static PetKind fromString(String kind) {
		if (kind == null || kind.isEmpty()) {
			return null;
		}
		try {
			return PetKind.valueOf(kind.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null; // or throw an exception if preferred
		}
	}
}
