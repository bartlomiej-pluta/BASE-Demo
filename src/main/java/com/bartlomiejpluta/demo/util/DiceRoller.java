package com.bartlomiejpluta.demo.util;

import lombok.*;
import java.util.*;
import java.util.regex.*;

@Data
@AllArgsConstructor
public class DiceRoller {
	private static final Pattern CODE_PATTERN = Pattern.compile("(\\d+)d(\\d+)([+-]\\d+)?");
	private final Random random = new Random();
	private int rolls;
	private int dice;
	private int modifier;

	public int roll() {
		var sum = modifier;

		for(int i=0; i<rolls; ++i) {
			sum += random.nextInt(dice) + 1;
		}

		return Math.max(0, sum);
	}

	public static DiceRoller of(String code) {
		var matcher = CODE_PATTERN.matcher(code);
		matcher.matches();
		return new DiceRoller(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(3)));
	}
}