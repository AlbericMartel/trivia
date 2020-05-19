package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.GoldenGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

public class SomeTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(originalOut);
	}

	@Test
	public void logGolden() {
		System.setOut(new PrintStream(outContent));

		long seed = 1L;

		String outGolden = goldenGame(seed);
		outContent.reset();

		String out = newGame(seed);

		assertThat(out).isEqualTo(outGolden);
	}

	private String goldenGame(long seed) {
		boolean notAWinner;

		GoldenGame goldenGame = new GoldenGame();

		goldenGame.add("Chet");
		goldenGame.add("Pat");
		goldenGame.add("Sue");

		Random goldenRand = new Random(seed);

		do {
			goldenGame.roll(goldenRand.nextInt(5) + 1);

			if (goldenRand.nextInt(9) == 7) {
				notAWinner = goldenGame.wrongAnswer();
			} else {
				notAWinner = goldenGame.wasCorrectlyAnswered();
			}
		} while (notAWinner);

		return outContent.toString();
	}

	private String newGame(Long seed) {
		boolean notAWinner;
		Game aGame = new Game();

		aGame.add("Chet");
		aGame.add("Pat");
		aGame.add("Sue");

		Random rand = new Random(seed);

		do {
			aGame.roll(rand.nextInt(5) + 1);

			if (rand.nextInt(9) == 7) {
				notAWinner = aGame.wrongAnswer();
			} else {
				notAWinner = aGame.wasCorrectlyAnswered();
			}

			aGame.moveToNextPlayer();
		} while (notAWinner);

		return outContent.toString();
	}
}
