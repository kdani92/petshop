package hu.bme.xj4vjg.petshop.repository;

import android.content.Context;

import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import hu.bme.xj4vjg.petshop.model.Pet;
import hu.bme.xj4vjg.petshop.model.Species;

public class SugarOrmRepository implements Repository {
	@Override
	public void open(Context context) {
		SugarContext.init(context);
	}

	@Override
	public void close() {
		SugarContext.terminate();
	}

	@Override
	public List<Species> getSpecies() {
		if (speciesTableExists()) {
			return SugarRecord.listAll(Species.class);
		}
		return new ArrayList<>();
	}

	@Override
	public List<Pet> getPets() {
		if (petTableExists()) {
			return SugarRecord.listAll(Pet.class);
		}
		return new ArrayList<>();
	}

	@Override
	public void updateSpecies(List<Species> speciesList) {
		List<Species> speciesListToSave = new ArrayList<>();
		if (speciesTableExists()) {
			List<Species> storedSpeciesList = SugarRecord.listAll(Species.class);
			for (Species species : speciesList) {
				boolean speciesUpdated = false;
				for (Species storedSpecies : storedSpeciesList) {
					if (species.equals(storedSpecies)) {
						updateSpecies(storedSpecies, species);
						speciesListToSave.add(storedSpecies);
						speciesUpdated = true;
						break;
					}
				}
				if (!speciesUpdated) {
					speciesListToSave.add(species);
				}
			}
		} else {
			speciesListToSave.addAll(speciesList);
		}

		SugarRecord.saveInTx(speciesListToSave);
	}

	@Override
	public void updatePets(List<Pet> pets) {
		List<Pet> petsToSave = new ArrayList<>();
		if (petTableExists()) {
			List<Pet> storedPets = SugarRecord.listAll(Pet.class);
			for (Pet pet : pets) {
				boolean petUpdated = false;
				for (Pet storedPet : storedPets) {
					if (pet.equals(storedPet)) {
						updatePet(storedPet, pet);
						petsToSave.add(storedPet);
						petUpdated = true;
						break;
					}
				}
				if (!petUpdated) {
					petsToSave.add(pet);
				}
			}
		} else {
			petsToSave.addAll(pets);
		}

		SugarRecord.saveInTx(petsToSave);
	}

	@Override
	public Pet getPet(String petId) {
		if (petTableExists()) {
			List<Pet> pets = SugarRecord.listAll(Pet.class);
			for (Pet pet : pets) {
				if (pet.getPetId().equals(petId)) {
					return pet;
				}
			}
		}
		return null;
	}

	@Override
	public void savePet(Pet pet) {
		if (petTableExists()) {
			Pet storedPet = getPet(pet.getPetId());
			if (storedPet != null) {
				updatePet(storedPet, pet);
				SugarRecord.saveInTx(storedPet);
			} else {
				SugarRecord.saveInTx(pet);
			}
		} else {
			SugarRecord.saveInTx(pet);
		}
	}

	@Override
	public void removePet(String petId) {
		if (petTableExists()) {
			Pet pet = getPet(petId);
			if (pet != null) {
				SugarRecord.deleteInTx(pet);
			}
		}
	}

	@Override
	public boolean containsPet(String petId) {
		if (petTableExists()) {
			List<Pet> pets = SugarRecord.listAll(Pet.class);
			for (Pet pet : pets) {
				if (pet.getPetId().equals(petId)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean speciesTableExists() {
		try {
			return SugarRecord.count(Species.class) >= 0;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean petTableExists() {
		try {
			return SugarRecord.count(Pet.class) >= 0;
		} catch (Exception e) {
			return false;
		}
	}

	private void updateSpecies(Species oldSpecies, Species newSpecies) {
		oldSpecies.setName(newSpecies.getName());
	}

	private void updatePet(Pet oldPet, Pet newPet) {
		oldPet.setSpecies(newPet.getSpecies());
		oldPet.setColor(newPet.getColor());
		oldPet.setTimeOfBirth(newPet.getTimeOfBirth());
		oldPet.setPrice(newPet.getPrice());
		oldPet.setImageUrl(newPet.getImageUrl());
	}
}
