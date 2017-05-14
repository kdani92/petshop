package hu.bme.xj4vjg.petshop.mock;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import hu.bme.xj4vjg.petshop.interactor.pet.network.event.AddPetNetworkEvent;
import hu.bme.xj4vjg.petshop.interactor.pet.network.event.GetPetDetailNetworkEvent;
import hu.bme.xj4vjg.petshop.interactor.pet.network.event.GetPetsNetworkEvent;
import hu.bme.xj4vjg.petshop.interactor.pet.network.event.GetSpeciesNetworkEvent;
import hu.bme.xj4vjg.petshop.model.Pet;
import hu.bme.xj4vjg.petshop.model.Settings;
import hu.bme.xj4vjg.petshop.model.Species;
import hu.bme.xj4vjg.petshop.repository.MemoryRepository;

import static hu.bme.xj4vjg.petshop.PetShopApplication.injector;

public class MockPetNetworkInteractor {
	@Inject
	EventBus bus;

	@Inject
	Settings settings;

	private static List<Species> speciesList;
	private static List<Pet> pets;

	static {
		speciesList = MemoryRepository.getDummySpeciesList();
		pets = MemoryRepository.getDummyPets();
	}

	public MockPetNetworkInteractor() {
		injector.inject(this);
	}

	public void getSpecies() {
		GetSpeciesNetworkEvent getSpeciesNetworkEvent = new GetSpeciesNetworkEvent();
		getSpeciesNetworkEvent.setContent(speciesList);
		getSpeciesNetworkEvent.setCode(200);

		bus.post(getSpeciesNetworkEvent);
	}

	public void getPets() {
		GetPetsNetworkEvent getPetsNetworkEvent = new GetPetsNetworkEvent();
		getPetsNetworkEvent.setContent(pets);
		getPetsNetworkEvent.setCode(200);

		bus.post(getPetsNetworkEvent);
	}

	public void getPetDetail(String petId) {
		GetPetDetailNetworkEvent getPetDetailNetworkEvent = new GetPetDetailNetworkEvent();
		Pet searchedPet = null;
		for (Pet pet : pets) {
			if (pet.getPetId().equals(petId)) {
				searchedPet = pet;
				break;
			}
		}

		getPetDetailNetworkEvent.setContent(searchedPet);
		if (searchedPet != null) {
			getPetDetailNetworkEvent.setCode(200);
		} else {
			getPetDetailNetworkEvent.setCode(410);
		}

		bus.post(getPetDetailNetworkEvent);
	}

	public void addPet(String species, String color, long timeOfBirth, int price, String imageUrl) {
		AddPetNetworkEvent addPetNetworkEvent = new AddPetNetworkEvent();
		if (!MockAuthInteractor.isUserAdmin(settings.getUsername())) {
			addPetNetworkEvent.setCode(420);
		} else if (species == null || species.isEmpty()) {
			addPetNetworkEvent.setCode(410);
		} else {
			String petId = UUID.randomUUID().toString();
			pets.add(new Pet(petId.toString(), species, color, timeOfBirth, price, imageUrl));
			addPetNetworkEvent.setCode(200);
			addPetNetworkEvent.setContent(petId);
		}

		bus.post(addPetNetworkEvent);
	}
}
