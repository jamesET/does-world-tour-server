package com.justjames.beertour.activity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ActivityTypeConverter implements AttributeConverter<ActivityType,String> {

	@Override
	public String convertToDatabaseColumn(ActivityType attribute) {
		if (attribute == null) {
			return "B";
		}
		switch (attribute) {
		case Beer:
			return "B";
		case News:
			return "N";
		case FinishBeerList:
			return "FB";
		default:
			throw new IllegalArgumentException("Unknown attribute " + attribute);
		}
	}

	@Override
	public ActivityType convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return ActivityType.Beer;
		}
		switch (dbData) {
		case "B":
			return ActivityType.Beer;
		case "N":
			return ActivityType.News;
		case "FB":
			return ActivityType.FinishBeerList;
		default:
			throw new IllegalArgumentException("Unknown attribute " + dbData);
		}
	}

}
