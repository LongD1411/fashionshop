package com.project.shopapp.services;

import com.project.shopapp.dtos.request.BannerDTO;
import com.project.shopapp.entities.Banner;

import java.util.List;
public interface IBannerService {
   List<Banner> getAllBanner();
   Banner createBanner(BannerDTO bannerDTO);
   Banner getBanner(Long id);
   void deleteBanner(Long id)throws Exception;
   Banner updateBanner(BannerDTO bannerDTO) throws  Exception;
}
