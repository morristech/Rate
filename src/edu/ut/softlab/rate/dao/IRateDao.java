package edu.ut.softlab.rate.dao;

import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 16-4-11.
 */
public interface IRateDao extends IOperations<Rate>{
    List<Rate> getLatestUpdateEntity();
    ChartData getChartData(String start, String end, Currency inCurrency, Currency outCurrency);
    Rate getLatestCurrencyRate(Currency currency);
    ChartData getSpecificRate(String start, String end, Currency currency);
}