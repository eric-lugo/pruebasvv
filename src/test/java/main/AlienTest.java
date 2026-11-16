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

	@ParameterizedTest(name = "{0}: ({1}, {2}) -> ({3}, {4})")
	@CsvSource(delimiter = ';', value = {
			"CP01,179,175,179,175",
			"CP02,-1,175,0,175",
			"CP03,0,175,0,175",
			"CP04,1,175,1,175",
			"CP05,357,175,357,175",
			"CP06,358,175,358,175",
			"CP07,359,175,358,175",
			"CP08,179,-1,179,0",
			"CP09,179,0,179,0",
			"CP10,179,1,179,1",
			"CP11,179,349,179,349",
			"CP12,179,350,179,350",
			"CP13,179,351,179,350"
	})

	void initAlien_test_robust(String idCaso, int xEntrada, int yEntrada, int xEsperada, int yEsperada) {
		Alien alien = new Alien(xEntrada, yEntrada);

		assertEquals(xEsperada, alien.getX(), "Falló la coordenada X en " + idCaso);
		assertEquals(yEsperada, alien.getY(), "Falló la coordenada Y en " + idCaso);
	}



}