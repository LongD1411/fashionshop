package com.project.shopapp.services;

import com.project.shopapp.dtos.BannerDTO;
import com.project.shopapp.entities.Banner;
import com.project.shopapp.respone.BannerResponse;
import org.springframework.stereotype.Service;

import java.util.List;
public interface IBannerService {
   List<Banner> getAllBanner();
   Banner createBanner(BannerDTO bannerDTO);
   Banner getBanner(Long id) throws  Exception;
   void deleteBanner(Long id)throws Exception;
   Banner updateBanner(BannerDTO bannerDTO) throws  Exception;
}
