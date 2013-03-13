/*******************************************************************************
 *
 *  Copyright 2011 - Sardegna Ricerche, Distretto ICT, Pula, Italy
 *
 * Licensed under the EUPL, Version 1.1.
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *  http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in  writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * In case of controversy the competent court is the Court of Cagliari (Italy).
 *******************************************************************************/
package service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Guest;
import model.Housed;
import model.questura.HousedType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import persistence.mybatis.mappers.GuestMapper;
import persistence.mybatis.mappers.HousedMapper;
import persistence.mybatis.mappers.HousedTypeMapper;

@Service
public class HousedServiceImpl implements HousedService{
	@Autowired
	private HousedMapper housedMapper;
	@Autowired
	private GuestMapper guestMapper;
	@Autowired
	private HousedTypeMapper housedTypeMapper;
	
	public List<Housed> findHousedByIdBooking(Integer id_booking) {
		List<Housed> ret;
		Guest guest = null;
		HousedType housedType = null;
		
		ret =  this.getHousedMapper().findHousedByIdBooking(id_booking);
		for (Housed each : ret) {
			guest = this.getGuestMapper().findGuestById(each.getId_guest());
			each.setGuest(guest);
			housedType = this.getHousedTypeMapper().findHousedTypeById(each.getId_housedType());
			each.setHousedType(housedType);
		}
		return ret;
	}
	
	
	
	@Override
	public List<Housed> findHousedByIdGuest(Integer id_guest) {
		List<Housed> ret;
		Guest guest = null;
		HousedType housedType = null;
		
		ret =  this.getHousedMapper().findHousedByIdGuest(id_guest);
		/*
		for (Housed each : ret) {
			guest = this.getGuestMapper().findGuestById(each.getId_guest());
			each.setGuest(guest);
			housedType = this.getHousedTypeMapper().findHousedTypeById(each.getId_housedType());
			each.setHousedType(housedType);
		}*/
		return ret;
	}

	

	@Override
	public Housed findMostRecentHousedByIdGuest(Integer id_guest) {
		Housed ret = null;
		List<Housed> housedList = null;
		
		housedList = this.getHousedMapper().findHousedByIdGuest(id_guest);
		Comparator<Housed> comparator = new Comparator<Housed>() {

			@Override
			public int compare(Housed o1, Housed o2) {
				int retComparator = 0;
				
				if(o1.getCheckInDate()!=null && o2.getCheckInDate()!=null){
					retComparator = o1.getCheckInDate().compareTo(o2.getCheckOutDate());
				}				
				return retComparator;
			}
			
		};
		
		Collections.sort(housedList, comparator);
		
		ret = housedList.get(housedList.size() - 1);
		
		return ret;
	}



	@Override
	public List<Housed> findAll() {
		return this.getHousedMapper().findAll();
	}
	
	@Override
	public Housed findHousedById(Integer id) {
		Housed ret = null;
		Guest guest = null;
		HousedType housedType = null;
		
		ret = this.getHousedMapper().findHousedById(id);
		if(ret!=null){
			guest = this.getGuestMapper().findGuestById(ret.getId_guest());
			ret.setGuest(guest);
			housedType = this.getHousedTypeMapper().findHousedTypeById(ret.getId_housedType());
			ret.setHousedType(housedType);			
		}
		
		
		return ret;
	}
	
	@Override
	public Housed findHousedByIdBookingAndIdGuest(Integer id_booking, Integer id_guest) {
		Map map = null;
		Housed ret = null;
		Guest guest = null;
		HousedType housedType = null;
				
		map = new HashMap();
		map.put("id_booking", id_booking);
		map.put("id_guest", id_guest);
		
		ret = this.getHousedMapper().findHousedByIdBookingAndIdGuest(map);
		guest = this.getGuestMapper().findGuestById(ret.getId_guest());
		ret.setGuest(guest);
		housedType = this.getHousedTypeMapper().findHousedTypeById(ret.getId_housedType());
		ret.setHousedType(housedType);
		
		return ret;
	}
	
	
	
	@Override
	public Integer insert(Housed housed) {
		return this.getHousedMapper().insert(housed);
	}
	
	@Override
	public Integer update(Housed housed) {
		return this.getHousedMapper().update(housed);
	}

	@Override
	public Integer delete(Integer id) {
		return this.getHousedMapper().delete(id);
	}

	public HousedMapper getHousedMapper() {
		return housedMapper;
	}
	public void setHousedMapper(HousedMapper housedMapper) {
		this.housedMapper = housedMapper;
	}
	public GuestMapper getGuestMapper() {
		return guestMapper;
	}
	public void setGuestMapper(GuestMapper guestMapper) {
		this.guestMapper = guestMapper;
	}
	public HousedTypeMapper getHousedTypeMapper() {
		return housedTypeMapper;
	}
	public void setHousedTypeMapper(HousedTypeMapper housedTypeMapper) {
		this.housedTypeMapper = housedTypeMapper;
	}
	
}