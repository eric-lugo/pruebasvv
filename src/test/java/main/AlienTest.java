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
			"CP-IA-01;179;175;179;175",
			"CP-IA-02;4;175;5;175",
			"CP-IA-03;5;175;5;175",
			"CP-IA-04;6;175;6;175",
			"CP-IA-05;327;175;327;175",
			"CP-IA-06;328;175;328;175",
			"CP-IA-07;329;175;328;175",
			"CP-IA-08;179;-1;179;0",
			"CP-IA-09;179;0;179;0",
			"CP-IA-10;179;1;179;1",
			"CP-IA-11;179;289;179;289",
			"CP-IA-12;179;290;179;290",
			"CP-IA-13;179;291;179;290"

	})

	void initAlien_test(String idCaso, int xEntrada, int yEntrada, int xEsperada, int yEsperada) {
		Alien alien = new Alien(xEntrada, yEntrada);

		assertEquals(xEsperada, alien.getX(), "Falló la coordenada X en " + idCaso);
		assertEquals(yEsperada, alien.getY(), "Falló la coordenada Y en " + idCaso);
	}

	@ParameterizedTest(name = "{0}: x={1}, direction={2} -> xEsperada={3}")
	@CsvSource({
			"CP-ACT01,5,-1,5",
			"CP-ACT02,5,0,5",
			"CP-ACT03,5,1,6",
			"CP-ACT04,166,-1,165",
			"CP-ACT05,166,0,166",
			"CP-ACT06,166,1,167",
			"CP-ACT07,328,-1,327",
			"CP-ACT08,328,0,328",
			"CP-ACT09,328,1,328"
	})
	void testAct(String id, int xEntrada, int direction, int xEsperada) {
		Alien alien = new Alien(xEntrada, 175);
		alien.act(direction);
		assertEquals(xEsperada, alien.getX(), () -> id + " fallido");
	}

}