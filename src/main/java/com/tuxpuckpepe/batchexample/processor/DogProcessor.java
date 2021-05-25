package com.tuxpuckpepe.batchexample.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.tuxpuckpepe.batchexample.model.Dog;

/**
 * 
 * @author pepe
 *
 */
public class DogProcessor implements ItemProcessor<Dog, Dog>{
	
	private static final String NO_BREED = "mongrel";
	private static final String NO_BREED_STANDARD =	"mixed-breed";
	
	private static final Logger LOG = LoggerFactory.getLogger(DogProcessor.class);
	
	@Override
	public Dog process(Dog dog) throws Exception {
		LOG.info(String.format("processing dog: %s", dog));
		String breed  = dog.getBreed().toLowerCase().equals(NO_BREED) ? 
						NO_BREED_STANDARD : dog.getBreed().toLowerCase();
		Dog newDog = new Dog(dog.getName(), breed, dog.getAge());
		
		return  newDog;
	}

}
