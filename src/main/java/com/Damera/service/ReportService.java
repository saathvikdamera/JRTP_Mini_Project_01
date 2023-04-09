package com.Damera.service;

import java.util.List;

import com.Damera.entities.CitizenPlans;
import com.Damera.request.SearchRequest;

public interface ReportService {
	
	List<CitizenPlans> fetchPlans(SearchRequest request);
	
	List<String> fetchPlanNames();
	
	List<String> fetchPlanStatus();
	
	boolean exportEmail();
	
	boolean exportPDF();

}
