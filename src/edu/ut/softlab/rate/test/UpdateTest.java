package edu.ut.softlab.rate.test;



import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.component.UpdateData;
import edu.ut.softlab.rate.controller.RateController;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.ICurrencyService;
import edu.ut.softlab.rate.service.IRateService;
import edu.ut.softlab.rate.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by alex on 16-4-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/mvc-dispatcher-servlet.xml", "classpath:/spring-hibernate.xml"})
@Transactional
public class UpdateTest {
    @Autowired
    UpdateData updateData;

    @Autowired
    RateController controller;

    @Autowired
    IRateService rateService;

    @Autowired
    ICurrencyDao currencyDao;

    @Autowired
    IRateDao rateDao;

    @Autowired
    ICurrencyService currencyService;

    @Autowired
    IUserService userService;

    @Value("#{account_information}")
    private Properties account_information;


    @Value("#{supplement}")
    private Properties supplement;

    @Resource(name = "messageSource")
    private MessageSource ms;


    private static final Logger logger =
            LoggerFactory.getLogger(UpdateTest.class);

    @Test
    @Rollback(false)
    @Transactional
    public void rateTest(){
        try{
//            org.springframework.core.io.Resource resource = new ClassPathResource(account_information.getProperty("PUSH_CA"));
//            String certificate = resource.getFile().getPath();
            Utility.send("alexlai@softlab.cs.tsukuba.ac.jp", "test", "test mail");
        }catch (Exception ex){
            System.out.println("error:"+ex.toString());
        }

    }
}
