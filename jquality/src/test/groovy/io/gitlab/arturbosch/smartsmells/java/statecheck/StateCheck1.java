package io.gitlab.arturbosch.smartsmells.java.statecheck;

/**
 * @author Artur Bosch
 */
@SuppressWarnings("ALL")
public class StateCheck1 {

	private static final int MATCH = 0;
	private static final int BIGGER = 1;
	private static final int SMALLER = -1;

	public int replaceWithPolymorphism(Staty staty) {
		if (staty instanceof BigStaty) {
			return BIGGER;
		} else if (staty instanceof SmallStaty) {
			return SMALLER;
		} else if (staty instanceof StuffStaty) {
			return MATCH;
		}
		throw new IllegalStateException();
	}

	class Staty {
	}

	class BigStaty extends Staty {
	}

	class SmallStaty extends Staty {
	}

	class StuffStaty extends Staty {
	}
}
