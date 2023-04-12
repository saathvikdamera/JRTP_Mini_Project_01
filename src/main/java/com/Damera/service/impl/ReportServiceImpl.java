package com.Damera.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.Damera.entities.CitizenPlans;
import com.Damera.repositories.CitizenPlanRepository;
import com.Damera.request.SearchRequest;
import com.Damera.service.ReportService;
import com.Damera.utils.EmailUtils;
import com.Damera.utils.ExcelGenerator;
import com.Damera.utils.PDFGenrator;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private CitizenPlanRepository repo;

	@Autowired
	private ExcelGenerator egenerator;

	@Autowired
	private EmailUtils utils;

	@Autowired
	private PDFGenrator pgenerator;

	@Override
	public List<CitizenPlans> fetchPlans(SearchRequest request) {

		CitizenPlans c1 = new CitizenPlans();

		if(null != request.getPlanName() && !"".equals(request.getPlanName())) {
			c1.setPlanName(request.getPlanName());
		}

		if(null != request.getPlanStatus() && !"".equals(request.getPlanStatus())) {
			c1.setPlanStatus(request.getPlanStatus());
		}

		if(null != request.getGender() && !"".equals(request.getGender())) {
			c1.setCitizenGender(request.getGender());
		}

		if(null != request.getStartDate() && !"".equals(request.getStartDate())) {
			String date = request.getStartDate();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(date,dateTimeFormatter);
			c1.setPlanStartDate(localDate);
		}

		if(null != request.getEndDate() && !"".equals(request.getEndDate())) {
			String date = request.getEndDate();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(date,dateTimeFormatter);
			c1.setPlanEndDate(localDate);
		}

		Example<CitizenPlans> exmp = Example.of(c1);
		List<CitizenPlans> plans = repo.findAll(exmp);
		return plans;
	}

	@Override
	public List<String> fetchPlanNames() {
		List<String> planNames = repo.getPlanNames();
		return planNames;
	}

	@Override
	public List<String> fetchPlanStatus() {
		List<String> statusNames = repo.getStatusNames();
		return statusNames;
	}

	@Override
	public boolean exportEmail() {
		File f = new File("Plans.xls");
		//String filePath = "D:\\19-JFSD\\Java RealTime Project\\Mini project Workspace\\PlansData.xls";
		try {

			List<CitizenPlans> plans = repo.findAll();
			FileOutputStream fos = new FileOutputStream(f);
			egenerator.generateExcel(fos, plans);
			utils.sendEmailWithAttachment("saathvikdamera@gmail.com",
					"Citizen Plans Report","Here is the excelsheet of plans.", f);
			
			fos.close();
			f.delete();

		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean exportPDF() {
		File f = new File("Plans.pdf");
	//	String filePath = "D:\\19-JFSD\\Java RealTime Project\\Mini project Workspace\\PlansData.pdf";
		try {
			List<CitizenPlans> plans = repo.findAll();
			FileOutputStream fos = new FileOutputStream(f );
			pgenerator.generatePDF(plans, fos);
			utils.sendEmailWithAttachment("saathvikdamera@gmail.com",
					"Citizen Plans Report","Here is the pdf of plans.", f);
			fos.close();
			f.delete();
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
