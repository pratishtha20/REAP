package com.ttnd.reap.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ttnd.reap.pojo.BadgeTransaction;
import com.ttnd.reap.pojo.EmployeeDetails;
import com.ttnd.reap.pojo.ReceivedBadges;
import com.ttnd.reap.service.IService;

@Controller
// @RequestMapping("/")
public class HomeController {

	@Autowired
	private IService service;

	// @Autowired
	// private HttpSession httpSession;

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView home(HttpSession httpSession) {
		EmployeeDetails employeeDetails = (EmployeeDetails) httpSession.getAttribute("employeeDetails");
		ModelAndView modelAndView = new ModelAndView();
		if (employeeDetails == null) {
			modelAndView.setViewName("login");
			return modelAndView;
		}
		modelAndView.addObject("badgeTransaction", new BadgeTransaction());
		modelAndView.addObject("receivedBadges", service.getReceivedBadgesOfEmployee(employeeDetails));
		modelAndView.addObject("remainingBadges", service.getRemainingBadgesOfEmployee(employeeDetails));
		modelAndView.setViewName("redirect:reap_home");
		return modelAndView;
	}

	/*
	 * @RequestMapping(value = "/login", method = RequestMethod.GET) public
	 * String login(HttpSession session) { return "login"; }
	 */

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("email_id") String email_id, @RequestParam("password") String password,
			HttpSession httpSession) {
		EmployeeDetails employeeDetails;
		ModelAndView modelAndView = new ModelAndView();
		try {
			int id = Integer.parseInt(email_id);
			employeeDetails = service.findEmployeeById(id, password);
		} catch (NumberFormatException numberFormatException) {
			employeeDetails = service.findEmployeeByEmail(email_id, password);
		}
		if (employeeDetails == null) {
			modelAndView.setViewName("login");
			modelAndView.addObject("msg", "Invalid Credentials!");
			return modelAndView;
		}
		httpSession.setAttribute("employeeDetails", employeeDetails);
		modelAndView.addObject("badgeTransaction", new BadgeTransaction());
		modelAndView.addObject("receivedBadges", service.getReceivedBadgesOfEmployee(employeeDetails));
		modelAndView.addObject("remainingBadges", service.getRemainingBadgesOfEmployee(employeeDetails));
		modelAndView.setViewName("reap_home");
		return modelAndView;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register(HttpSession httpSession) {
		EmployeeDetails employeeDetails = (EmployeeDetails) httpSession.getAttribute("employeeDetails");
		ModelAndView modelAndView = new ModelAndView();
		if (employeeDetails == null) {
			modelAndView.addObject("employeeDetails", new EmployeeDetails());
			modelAndView.setViewName("register");
			return modelAndView;
		}
		modelAndView.addObject("badgeTransaction", new BadgeTransaction());
		modelAndView.addObject("receivedBadges", service.getReceivedBadgesOfEmployee(employeeDetails));
		modelAndView.addObject("remainingBadges", service.getRemainingBadgesOfEmployee(employeeDetails));
		modelAndView.setViewName("reap_home");
		return modelAndView;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(@ModelAttribute EmployeeDetails employeeDetails, HttpSession httpSession) {
		ModelAndView modelAndView = new ModelAndView();
		int success = service.save(employeeDetails);
		if (success == 1) {
			httpSession.setAttribute("employeeDetails", employeeDetails);

			modelAndView.addObject("badgeTransaction", new BadgeTransaction());
			modelAndView.addObject("receivedBadges", service.getReceivedBadgesOfEmployee(employeeDetails));
			modelAndView.addObject("remainingBadges", service.getRemainingBadgesOfEmployee(employeeDetails));
			modelAndView.setViewName("reap_home");
			return modelAndView;
		} else {
			if (success == -1)
				modelAndView.addObject("msg", "This email already exists!! Please choose another one");
			else
				modelAndView.addObject("msg", "Something went wrong! Please try again.");

			modelAndView.setViewName("register");
			return modelAndView;
		}

	}

	@RequestMapping(value = "/badges", method = RequestMethod.GET)
	public ModelAndView badges(HttpSession httpSession) {
		EmployeeDetails employeeDetails = (EmployeeDetails) httpSession.getAttribute("employeeDetails");
		ModelAndView modelAndView = new ModelAndView();
		if (employeeDetails == null) {
			modelAndView.setViewName("login");
			modelAndView.addObject("msg", "Please login first!!!!");
			return modelAndView;
		}
		modelAndView.setViewName("myBadges");
		ReceivedBadges receivedBadges = service.getReceivedBadgesOfEmployee(employeeDetails);
		modelAndView.addObject("receivedBadges", receivedBadges);
		return modelAndView;
	}

	public ModelAndView badges(@ModelAttribute BadgeTransaction badgeTransaction, HttpSession httpSession) {
		EmployeeDetails employeeDetails = (EmployeeDetails) httpSession.getAttribute("employeeDetails");
		ModelAndView modelAndView = new ModelAndView();
		badgeTransaction.setSender(employeeDetails);
		badgeTransaction.setDate(new Date());
		modelAndView.setViewName("myBadges");
		ReceivedBadges receivedBadges = service.getReceivedBadgesOfEmployee(employeeDetails);
		modelAndView.addObject("receivedBadges", receivedBadges);
		return modelAndView;
	}
}
