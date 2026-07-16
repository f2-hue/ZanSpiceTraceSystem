package chain.trace.zanzibarspice.service;

import chain.trace.zanzibarspice.entity.Farm;
import chain.trace.zanzibarspice.entity.User;
import chain.trace.zanzibarspice.repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FarmService {

    private final FarmRepository farmRepository;

    public Farm registerFarm(User farmer, String farmName, String district,
                             String region, Double landSizeAcres, String spiceTypes) {
        Farm farm = new Farm();
        farm.setFarmer(farmer);
        farm.setFarmName(farmName);
        farm.setDistrict(district);
        farm.setRegion(region);
        farm.setLandSizeAcres(landSizeAcres);
        farm.setSpiceTypes(spiceTypes);
        return farmRepository.save(farm);
    }

    public List<Farm> getFarmsByFarmer(User farmer) {
        return farmRepository.findByFarmer(farmer);
    }

    public List<Farm> getAllFarms() {
        return farmRepository.findAll();
    }

    public Optional<Farm> findById(Long id) {
        return farmRepository.findById(id);
    }
}