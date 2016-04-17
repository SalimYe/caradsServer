package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;

public class Campaign extends Model{

	private String name;
	private String description;
	private String campaignBudget;
	private String startDate;
	private String endDate;
	private Collection<Image> images;
}
