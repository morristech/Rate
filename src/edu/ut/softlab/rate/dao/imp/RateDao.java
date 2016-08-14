package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.model.Currency;
import org.directwebremoting.annotations.RemoteMethod;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by alex on 16-4-11.
 */
@Repository("rateDao")
public class RateDao extends AbstractHibernateDao<Rate> implements IRateDao{
    public RateDao(){
        super();
        setClass(Rate.class);
    }


    @Override
    public List<Rate> getLatestRates() {
        DetachedCriteria maxDate = DetachedCriteria.forClass(Rate.class)
                .setProjection(Projections.max("date"));
        Criteria criteria = getCurrentSesstion().createCriteria(Rate.class)
                .add(Property.forName("date").eq(maxDate));
        return criteria.list();
    }

    @Override
    public Rate getLatestCurrencyRate(Currency currency) {
        DetachedCriteria maxDate = DetachedCriteria.forClass(Rate.class)
                .setProjection(Projections.max("date"));
        Criteria criteria = getCurrentSesstion().createCriteria(Rate.class);
        criteria.add(Restrictions.eq("currency", currency))
                .add(Property.forName("date").eq(maxDate));
        return (Rate)criteria.uniqueResult();
    }

    @Override
    public ChartData getSpecificRate(String start, String end, Currency currency) {
        String hql = "from Rate where date >= :start and date <= :endDate and cid = :cid";
        List<Rate> currencyRateList = getCurrentSesstion().createQuery(hql).setString("start", start)
                .setString("endDate", end)
                .setString("cid", currency.getCid())
                .list();
        ChartData chartData = new ChartData();
        try{
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            chartData.setTime(startDate.getTime()+32400000);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
        chartData.setInCurrency(currency.getCode());
        for(int i=0; i<currencyRateList.size(); i++){
            chartData.getData().add(currencyRateList.get(i).getValue());
        }
        return chartData;
    }



    @Override
    public List<Rate> getSpecificRateList(long startDate, long endDate, Currency currency) {
        Criteria crit = getCurrentSesstion().createCriteria(Rate.class)
                .add(Restrictions.eq("currency", currency))
                .add(Restrictions.between("date", new Date(startDate), new Date(endDate)))
                .addOrder(Order.asc("date"));
        return crit.list();
    }
}
