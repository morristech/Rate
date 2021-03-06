package edu.ut.softlab.rate.webcontroller;


import edu.ut.softlab.rate.bean.CurrencyBean;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.ICurrencyService;
import edu.ut.softlab.rate.webbean.WebCurrencyBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by alex on 16-8-1.
 */
@Controller
@RequestMapping("/web/currencies")
public class WebCurrencyController {
    @Resource(name = "currencyService")
    private ICurrencyService currencyService;

    @Value("#{currency_country}")
    private Properties currencyCountry;

    @Value("#{country_location}")
    private Properties countryLocation;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getCurrencies(@RequestParam(value="cid", required = false) String cid,
                                                            @RequestParam(value="lan", required = false, defaultValue = "en") String lan,
                                                             @RequestParam(value="rev", required = false, defaultValue = "0") Integer rev){
        List<WebCurrencyBean> currencies = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        if(rev == null){
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            response.put(ResponseField.error_message, "rev is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (cid == null){
            List<Currency> currencyList = currencyService.getUpdatedCurrencies(rev);
            for(Currency currency : currencyList){
                WebCurrencyBean currencyBean = new WebCurrencyBean(currency);
                System.out.println(currency.getCode());
                currencyBean.setIcon(currencyCountry.get(currency.getCode()).toString().toLowerCase());
                currencyBean.setName(java.util.Currency.getInstance(currency.getCode()).getDisplayName(Locale.forLanguageTag(lan)));
                System.out.println(countryLocation.getProperty(currencyCountry.get(currency.getCode()).toString()));
                String[] location = countryLocation.getProperty(currencyCountry.get(currency.getCode()).toString()).split(",");
                currencyBean.setLatitude(Double.parseDouble(location[0]));
                currencyBean.setLongitude(Double.parseDouble(location[1]));
                currencies.add(currencyBean);
            }
            result.put("currencies", currencies);
            result.put("revision", currencyService.getCurrentRevision());
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            Currency currency = currencyService.findOne(cid);
            if(currency == null){
                response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
                response.put(ResponseField.error_message, "No requested currency");
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }else {
                WebCurrencyBean currencyBean = new WebCurrencyBean(currency);
                currencyBean.setIcon(currencyCountry.get(currency.getCode()).toString().toLowerCase());
                currencyBean.setName(java.util.Currency.getInstance(currency.getCode()).getDisplayName(Locale.forLanguageTag(lan)));
                currencies.add(currencyBean);
                response.put(ResponseField.result, currencies);
                response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addCurrency(@RequestParam(value = "code", required = true)String code){
        Map<String, Object> result = new HashMap<>();
        String message = currencyService.addCurrency(code);
        result.put(ResponseField.result, message);
        result.put(ResponseField.HttpStatus, HttpStatus.OK.value());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
