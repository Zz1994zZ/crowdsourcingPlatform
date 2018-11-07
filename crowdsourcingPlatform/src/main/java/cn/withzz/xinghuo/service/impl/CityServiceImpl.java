package cn.withzz.xinghuo.service.impl;

import cn.withzz.xinghuo.dao.CityDao;
import cn.withzz.xinghuo.domain.City;
import cn.withzz.xinghuo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市业务逻辑实现类
 *
 * Created by bysocket on 07/02/2017.
 */
@Service
public class CityServiceImpl implements CityService {

//    @Autowired
    private CityDao cityDao;

    public List<City> findAllCity(){
        List citys = new ArrayList();
        for (int i = 0; i <5 ; i++) {
            City city = new City();
            city.setCityName("city"+i);
            city.setId((long)i);
            city.setProvinceId(33L);
            city.setDescription("hoho,this is city:"+i);
            citys.add(city);
        }

//        return cityDao.findAllCity();
        return citys;

    }

    public City findCityById(Long id) {
        return cityDao.findById(id);
    }

    @Override
    public Long saveCity(City city) {
        return cityDao.saveCity(city);
    }

    @Override
    public Long updateCity(City city) {
        return cityDao.updateCity(city);
    }

    @Override
    public Long deleteCity(Long id) {
        return cityDao.deleteCity(id);
    }

}
