package org.proteinevolution.models.spec;

public final class HHR {

	private HHR() {
		
		throw new AssertionError();
	}

	// Contains the indices of the records within a line of an HHR file

	// Number
	public static final int NO_START = 0;
	public static final int NO_END = 3;

	// Hit
	public static final int HIT_START = 3;
	public static final int HIT_END = 34;

	// Prob
	public static final int PROB_START = 34;
	public static final int PROB_END = 40;

	// E-value
	public static final int EVAL_START = 40;
	public static final int EVAL_END = 48;

	// P-value
	public static final int PVAL_START = 48;
	public static final int PVAL_END = 56;

	// Score
	public static final int SCORE_START = 56;
	public static final int SCORE_END = 63;

	// SS
	public static final int SS_START = 63;
	public static final int SS_END = 69;

	// Cols
	public static final int COLS_START = 69;
	public static final int COLS_END = 74;

	// Query Start
	public static final int QUERY_START_START = 74;
	public static final int QUERY_START_END = 79;

	// Query End
	public static final int QUERY_END_START = 80; // Skip the '-'
	public static final int QUERY_END_END = 85;

	// Template Start
	public static final int TEMPLATE_START_START = 85;
	public static final int TEMPLATE_START_END = 89;

	// Template End
	public static final int TEMPLATE_END_START = 90; // Skip the '-'
	public static final int TEMPLATE_END_END = 94;

	// Ref
	public static final int REF_START = 94; // Skip the '-'
}
