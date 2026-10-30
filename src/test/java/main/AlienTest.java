package main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;

import static org.junit.jupiter.api.Assertions.*;

class AlienTest {

	@Test
	void createAlien_test() {
		Alien alien = new Alien(0, 0);
		assertNotNull(alien, "Alien object should not be null");
	}


}