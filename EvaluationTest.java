package com.recommendations;

import static org.junit.Assert.*;

import org.junit.Test;

public class EvaluationTest {

	@Test
	public void testGetPredictedRating() {
		Evaluation e = new Evaluation();
		float arr[] = {(float) 4.1666665,(float) 4.0,
				(float) 4.0,0,(float) 3.5625,(float) 4.0,
				0,(float) 4.076923,(float) 4.1666665,(float) 4.0,
				0,(float) 3.0,(float) 4.5,(float) 4.2,0,(float) 4.5,
				(float) 4.0833335,(float) 3.5};
		String g[] = {"Action","Adventure","Romance","Thriller"};
		assertEquals(1.1, e.getPredictedRating(arr, g),0.01);
	}

}
