package de.hm.edu.carads.controller;

import java.util.Collection;
import java.util.Iterator;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import com.mongodb.BasicDBObject;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.MetaInformation;

public class AdvertiserControllerImpl extends AbstractEntityControllerImpl<Advertiser> implements AdvertiserController{

	public AdvertiserControllerImpl(DatabaseController database) {
		super(Advertiser.class, database);
	}
	
	@Override
	public Advertiser changeEntity(String id, Advertiser entityData) throws Exception {
		if(!EntityValidator.isEntityValid((entityData)))
			throw new InvalidAttributesException();
		
		Advertiser d = getAdvertiserByEmail(entityData.getEmail());
		if(d!=null)
			if (!d.getId().equals(id))
				throw new AlreadyExistsException();

		return super.changeEntity(id, entityData);
	}
	
	@Override
	public Advertiser addEntity(Advertiser entity) throws Exception {
		if(existAdvertiserByEmail(entity.getEmail()))
			throw new AlreadyExistsException();
		return super.addEntity(entity);
	}
	
	private boolean existAdvertiserByEmail(String email) {
		if(dbController.existEntityByKeyValue(Advertiser.class, "email", email))
			return true;
		return false;
	}	
	
	private Advertiser getAdvertiserByEmail(String email) {
		return this.makeEntityFromBasicDBObject(dbController
				.getEntityByKeyValue(Advertiser.class, "email", email));
	}

	@Override
	public Campaign addCampaign(String advertiserId, Campaign campaign)
			throws Exception {
		Advertiser ad = getEntity(advertiserId);
		
		campaign.setId(dbController.getNewId());
		campaign.renewMetaInformation();
		ad.addCampaign(campaign);
		ad.getMetaInformation().update();
		
		dbController.updateEntity(Advertiser.class, ad.getId(), BasicDBObject.parse(gson.toJson(ad)));
		
		return campaign;
	}

	@Override
	public Collection<Campaign> getCampaigns(String advertiserId) throws Exception {
		Advertiser advertiser = getEntity(advertiserId);
		
		return advertiser.getCampaigns();
	}

	@Override
	public Campaign getCampaign(String advertiserId, String campaignId) throws Exception {
		Advertiser advertiser = getEntity(advertiserId);
		Campaign c = advertiser.getCampaign(campaignId);
		if(c==null)
			throw new NoContentException(campaignId + " not found");
		
		return c;
	}

	@Override
	public void deleteCampaign(String advertiserId, String campaignId)
			throws Exception {
		Advertiser advertiser = getEntity(advertiserId);
		if(!advertiser.removeCampaign(campaignId))
			throw new NoContentException(campaignId + " not found");
		advertiser.getMetaInformation().update();
		
		dbController.updateEntity(Advertiser.class, advertiserId, BasicDBObject.parse(gson.toJson(advertiser)));
	}

	@Override
	public Campaign updateCampaign(String advertiserId, String campaignId, Campaign campaign) throws Exception {		
		if(!EntityValidator.isEntityValid(campaign))
			throw new InvalidAttributesException();
		
		Advertiser advertiser = getEntity(advertiserId);
		Campaign oldC = advertiser.getCampaign(campaignId);
		if(oldC == null)
			throw new NoContentException(campaignId + " not found");
		MetaInformation oldMeta = oldC.getMetaInformation();
		advertiser.removeCampaign(campaignId);
		campaign.update(oldMeta);
		campaign.setId(campaignId);
		advertiser.addCampaign(campaign);
		
		dbController.updateEntity(Advertiser.class, advertiserId, BasicDBObject.parse(gson.toJson(advertiser)));
		return campaign;
	}
}
