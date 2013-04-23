package util;

import constants.Sizes;

public abstract class DataGenerator {

	// price generator
	public static Integer getPrice(String serviceName, String provider) {
		int price = (serviceName + provider).hashCode() % (Sizes.maxPrice - Sizes.minPrice);
		price += Sizes.minPrice;

		return new Integer(price);
	}
}
