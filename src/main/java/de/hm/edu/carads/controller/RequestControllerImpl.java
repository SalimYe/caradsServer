package de.hm.edu.carads.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import sun.util.EmptyListResourceBundle;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.comm.Fellow;
import de.hm.edu.carads.models.comm.OfferInformation;
import de.hm.edu.carads.models.util.FellowState;

public class RequestControllerImpl implements RequestController{

	private DriverController dc;
	private AdvertiserController ac;
	
	public RequestControllerImpl(DatabaseController database){
		dc = new DriverControllerImpl(database);
		ac = new AdvertiserControllerImpl(database);
	}
	
	@Override
	public Collection<OfferInformation> getOfferInformation(String driverId) throws Exception {
		Collection<OfferInformation> offers = new ArrayList<OfferInformation>();
		
		Iterator<Car> carIterator = dc.getCars(driverId).iterator();
		
		
		while(carIterator.hasNext()){
			Car car = carIterator.next();
			//Collection<Campaign> requestingCampaigns = new ArrayList<Campaign>();
			Iterator<Campaign> requestingCampaignsIterator = ac.getCarRequestingCampaigns(car.getId()).iterator();
			while(requestingCampaignsIterator.hasNext()){
				Campaign reqCamp = requestingCampaignsIterator.next();
				FellowState state = getStateFromCampaign(car.getId(), reqCamp);
				Advertiser advertiser = ac.getAdvertiserFromCampaign(reqCamp.getId());
				
				//Loesche unnoetige Informationen
				reqCamp.setFellows(null);
				advertiser.removeAllCampaigns();
				
				
				offers.add(new OfferInformation(car, advertiser, reqCamp, state));
			}
		}
		return offers;
	}
	
	@Override
	public OfferInformation respondToOffer(String driverId, String campaignId, String respond) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	private FellowState getStateFromCampaign(String carid, Campaign campaign){
		
		Iterator<Fellow> fellowIterator = campaign.getFellows().iterator();
		while(fellowIterator.hasNext()){
			Fellow fellow = fellowIterator.next();
			if(fellow.getCarId().equals(carid))
				return fellow.getState();
		}
		return null;
	}

	
	
}
