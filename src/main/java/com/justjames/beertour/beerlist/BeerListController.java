package com.justjames.beertour.beerlist;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="/beerlists")
public class BeerListController {
	
	private Log log = LogFactory.getLog(BeerListController.class);
	
	@Autowired
	BeerListSvc listSvc;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public BeerListResponse getAllLists() {
		Collection<BeerList> beerLists = listSvc.findAll();
		BeerListResponse response = new BeerListResponse(beerLists);
		if (beerLists == null) {
			response.setErrorMsg("No lists found");
		}
		return response;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public BeerListResponse getBeerList(@PathVariable Integer id) {
		BeerList beerList = listSvc.getBeerList(id);
		log.debug(beerList);
		BeerListResponse response = new BeerListResponse(beerList);
		if (beerList == null) {
			response.setErrorMsg("No list found");
		}
		return response;
	}

	@RequestMapping(value="getMyBeerList", method=RequestMethod.GET)
	public @ResponseBody MyBeerListResponse getMyBeerList() {
		BeerList myBeerList = listSvc.getMyBeerList();
		MyBeerListResponse response = new MyBeerListResponse(myBeerList); 
		return response; 
	}
	
	@RequestMapping(value="/{listId}/beers/{beerOnListId}/crossoff", method=RequestMethod.POST)
	public MyBeerListResponse crossOffBeer(@PathVariable Integer listId,@PathVariable Integer beerOnListId) {
		listSvc.crossOffMyBeer(listId, beerOnListId);
		BeerList myBeerList = listSvc.getBeerList(listId);
		MyBeerListResponse response = new MyBeerListResponse(myBeerList); 
		return response;
	}

	@RequestMapping(value="/{listId}/beers/{beerOnListId}/complete", method=RequestMethod.POST)
	public MyBeerListResponse completeBeer(@PathVariable Integer listId,@PathVariable Integer beerOnListId) {
		listSvc.completeBeer(listId, beerOnListId);
		BeerList myBeerList = listSvc.getBeerList(listId);
		MyBeerListResponse response = new MyBeerListResponse(myBeerList); 
		return response;
	}

	@RequestMapping(value="/{listId}/beers/{beerOnListId}/reject", method=RequestMethod.POST)
	public MyBeerListResponse rejectBeer(@PathVariable Integer listId,@PathVariable Integer beerOnListId) {
		listSvc.rejectBeer(listId, beerOnListId);
		BeerList myBeerList = listSvc.getBeerList(listId);
		MyBeerListResponse response = new MyBeerListResponse(myBeerList); 
		return response;
	}
	
	@RequestMapping(value="/getListToComplete", method=RequestMethod.GET)
	public CompleteListResponse getCompleteList() {
		Collection<BeerToComplete> beers = listSvc.getBeersToComplete(); 
		return new CompleteListResponse(beers);
	}
	
	@RequestMapping(value="completeAll", method=RequestMethod.POST)
	public ResponseEntity<String> completeAllOutstanding() {
		listSvc.completeAllOutstanding();
		return ResponseEntity.ok().body("{ \"message\" : \"OK\"}");
	}


}
