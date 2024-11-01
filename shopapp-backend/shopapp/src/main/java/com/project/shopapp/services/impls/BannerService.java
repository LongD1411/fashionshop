package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.request.BannerDTO;
import com.project.shopapp.entities.Banner;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.repositories.BannerRespository;
import com.project.shopapp.respone.BannerResponse;
import com.project.shopapp.services.IBannerService;
import com.project.shopapp.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class BannerService implements IBannerService {
    private final BannerRespository bannerRespository;
    private final ModelMapper modelMapper;
    @Override
    public List<Banner> getAllBanner() {
        List<Banner> bannerEntities = bannerRespository.findAll();
        List<BannerResponse> bannerResponses = new ArrayList<>();
        modelMapper.map(bannerEntities,bannerResponses);
        return  bannerEntities;
    }

    @Override
    public Banner createBanner(BannerDTO bannerDTO) {
        Banner banner = Banner.builder()
                .description(bannerDTO.getDescription())
                .thumbnail(bannerDTO.getThumbnail())
                .title(bannerDTO.getTitle())
                .build();
        return bannerRespository.save(banner);
    }

    @Override
    public Banner getBanner(Long id) {
        return bannerRespository.findById(id).orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));
    }

    @Override
    public void deleteBanner(Long id) throws Exception {
        Banner banner = bannerRespository.findById(id).orElseThrow(()->  new AppException(ErrorCode.DATA_NOT_EXISTED));
        if(banner!=null ){
            bannerRespository.deleteById(id);
            ImageUtil.deleteImage(banner.getThumbnail());
        }
    }

    @Override
    @Transactional
    public Banner updateBanner(BannerDTO bannerDTO) throws Exception {
        Banner banner = bannerRespository.findById(bannerDTO.getId()).orElseThrow(()->  new AppException(ErrorCode.DATA_NOT_EXISTED));
        banner.setDescription(bannerDTO.getDescription());
        banner.setTitle(bannerDTO.getTitle());
        String nameImageDelete = banner.getThumbnail();
        if(bannerDTO.getThumbnail()!= null) {
            banner.setThumbnail(bannerDTO.getThumbnail());
            bannerRespository.save(banner);
            ImageUtil.deleteImage(nameImageDelete);
            return banner;
        }
        return  bannerRespository.save(banner);
    }
}
