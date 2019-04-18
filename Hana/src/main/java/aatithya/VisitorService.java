package aatithya;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/uri", produces = "application/json")
public class VisitorService {

	
	EnquiryDao enquiry=new EnquiryDao();
	// posting into
	
	@RequestMapping(value = "/addDetails", method = RequestMethod.POST, consumes = "application/json")
	public String addDetails(@RequestBody EnquiryDetails details) {

		String res = enquiry.addDetails(details);
		return res;
	}
	
	@RequestMapping(value = "/addInfo", method = RequestMethod.POST, consumes = "application/json")
	public String addInfo(@RequestBody EnquiryInformation info) {

		String res = enquiry.addInfo(info);
		return res;
	}
	
	@RequestMapping(value = "/addVehicle", method = RequestMethod.POST, consumes = "application/json")
	public String addVehichle(@RequestBody VehicleInformation vehichle) {

		String res = enquiry.addVehichle(vehichle);
		return res;
	}
	


}
