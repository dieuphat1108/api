package com.project.api.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.project.api.config.PaypalPaymentIntent;
import com.project.api.config.PaypalPaymentMethod;
import com.project.api.dao.BasketRepository;
import com.project.api.dao.PaymentRepository;
import com.project.api.dao.UserRepository;
import com.project.api.dto.PaymentDto;
import com.project.api.entity.Basket;
import com.project.api.entity.PaymentEntity;
import com.project.api.entity.User;
import com.project.api.service.PaypalService;
import com.project.api.util.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

//fix pull
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PaymentController {
	int userId;
	Double total;
	public static final String PAYPAL_SUCCESS_URL = "pay/success";
	public static final String PAYPAL_CANCEL_URL = "pay/cancel";

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	private PaypalService paypalService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BasketRepository basketRepository;

	@RequestMapping(method = RequestMethod.POST, value = "pay")
	public String pay(HttpServletRequest request, @RequestBody PaymentDto paymentDto) {
		userId = paymentDto.getUserId();
		total = paymentDto.getTotal();
		String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
		String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
		try {
			Payment payment = paypalService.createPayment(
					paymentDto.getTotal(),
					"USD",
					PaypalPaymentMethod.paypal,
					PaypalPaymentIntent.sale,
					"Thanh toán giỏ hàng",
					cancelUrl,
					successUrl);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return links.getHref();
				}
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}

	@RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
	public String cancelPay() {
		return "cancel";
	}

	@RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
	public RedirectView successPay(@RequestParam("paymentId") String paymentId,
								   @RequestParam("PayerID") String payerId) {
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				Date datePay = new Date();
				PaymentEntity paymentEntity = new PaymentEntity();
				paymentEntity.setUserId(userId);
				paymentEntity.setPaymentDate(datePay);
				paymentEntity.setAmount(total);
				paymentRepository.save(paymentEntity);
				//remove basket
				List<Basket> basket = basketRepository.findByUserId((long)userId);
				basketRepository.delete(basket.get(0));
				RedirectView redirectView = new RedirectView();
				redirectView.setUrl("http://localhost:3000");
				return redirectView;
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:3000");
		return redirectView;
	}

}