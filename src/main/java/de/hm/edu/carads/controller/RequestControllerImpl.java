package de.hm.edu.carads.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.comm.Fellow;
import de.hm.edu.carads.models.comm.OfferInformation;
import de.hm.edu.carads.models.util.FellowState;

public class RequestControllerImpl implements RequestController {

	private DriverController dc;
	private AdvertiserController ac;

	public RequestControllerImpl(DriverController dc, AdvertiserController ac) {
		this.dc = dc;
		this.ac = ac;
	}

	@Override
	public Collection<OfferInformation> getOfferInformation(String driverId)
			throws Exception {
		Collection<OfferInformation> offers = new ArrayList<OfferInformation>();

		Iterator<Car> carIterator = dc.getCars(driverId).iterator();

		while (carIterator.hasNext()) {
			Car car = carIterator.next();
			// Collection<Campaign> requestingCampaigns = new
			// ArrayList<Campaign>();
			Iterator<Campaign> requestingCampaignsIterator = ac
					.getCarRequestingCampaigns(car.getId()).iterator();
			while (requestingCampaignsIterator.hasNext()) {
				Campaign reqCamp = requestingCampaignsIterator.next();
				FellowState state = getStateFromCampaign(car.getId(), reqCamp);
				Advertiser advertiser = ac.getAdvertiserFromCampaign(reqCamp
						.getId());

				// Loesche unnoetige Informationen
				reqCamp.setFellows(null);
				advertiser.removeAllCampaigns();

				offers.add(new OfferInformation(car, advertiser, reqCamp, state));
			}
		}
		return offers;
	}

	@Override
	public void respondToOffer(String carId, String advertiserId,
			String campaignId, String respond) throws Exception {
		Campaign campaign = ac.getCampaign(advertiserId, campaignId);
		Iterator<Fellow> fellowIterator = campaign.getFellows().iterator();
		Collection<Fellow> updatedFellows = new ArrayList<Fellow>();
		while (fellowIterator.hasNext()) {
			Fellow fellow = fellowIterator.next();
			if (fellow.getCarId().equals(carId)) {
				fellow.setState(getFellowState(respond));
			}
			updatedFellows.add(fellow);
		}
		campaign.setFellows(updatedFellows);
		ac.updateCampaign(advertiserId, campaignId, campaign);
	}

	private FellowState getFellowState(String state) {
		if (state.equals(FellowState.ACCEPTED.toString())) {
			return FellowState.ACCEPTED;
		} else if (state.equals(FellowState.ASKED.toString())) {
			return FellowState.ASKED;
		} else if (state.equals(FellowState.REJECTED.toString()))
			;
		return FellowState.REJECTED;
	}

	private FellowState getStateFromCampaign(String carid, Campaign campaign) {

		Iterator<Fellow> fellowIterator = campaign.getFellows().iterator();
		while (fellowIterator.hasNext()) {
			Fellow fellow = fellowIterator.next();
			if (fellow.getCarId().equals(carid)) {
				return fellow.getState();
			}
		}
		return null;
	}

	@Override
	public Collection<Car> getAvailableCars(String advertiserId,
			String campaignId) throws Exception {
		Collection<Car> availableCars = new ArrayList<Car>();

		Campaign campaign = ac.getEntity(advertiserId).getCampaign(campaignId);

		Iterator<Car> allReducedCars = reduceAlreadyAskedCars(campaign,
				dc.getAllCars()).iterator();
		while (allReducedCars.hasNext()) {
			Car car = allReducedCars.next();
			if (!ac.isCarOccupiedInTime(car.getId(), campaign.getStartDate(),
					campaign.getEndDate()))
				availableCars.add(car);
		}

		return availableCars;
	}

	/**
	 * Diese Methode nimmt eine Kampagne und eine Liste an Fahrzeugen entgegen.
	 * Die List wird um alle Fahrtzeuge reduziert, welche bereits fue die
	 * Kampagne angefragt wurden. Dabei spielt es keine Rolle ob die Fahrzeuge
	 * bereits geantwortet haben.
	 * 
	 * @param campaign
	 * @param cars
	 * @return reducedCarList
	 */
	private Collection<Car> reduceAlreadyAskedCars(Campaign campaign,
			Collection<Car> cars) {
		Collection<Car> reduced = new ArrayList<Car>();
		Collection<Fellow> fellows = campaign.getFellows();
		Iterator<Car> carIterator = cars.iterator();
		boolean isAFellow;
		while (carIterator.hasNext()) {
			isAFellow = false;
			Car car = carIterator.next();
			Iterator<Fellow> fellowIterator = fellows.iterator();
			while (fellowIterator.hasNext()) {
				Fellow fellow = fellowIterator.next();
				if (car.getId().equals(fellow.getCarId())) {
					isAFellow = true;
					break;
				}

			}
			if (!isAFellow)
				reduced.add(car);
		}
		return reduced;
	}
}
