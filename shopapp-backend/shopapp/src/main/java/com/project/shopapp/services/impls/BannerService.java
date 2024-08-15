package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.BannerDTO;
import com.project.shopapp.entities.Banner;
import com.project.shopapp.repositories.BannerRespository;
import com.project.shopapp.respone.BannerResponse;
import com.project.shopapp.services.IBannerService;
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
}
