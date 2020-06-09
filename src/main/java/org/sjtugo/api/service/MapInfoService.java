package org.sjtugo.api.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.DAO.Entity.CarInfo;
import org.sjtugo.api.DAO.Entity.Destination;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.Entity.HelloBikeInfo;
import org.sjtugo.api.controller.ResponseEntity.MapVertexResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MapInfoService {
    private final MapVertexInfoRepository mapVertexInfoRepository;
    private final DestinationRepository destinationRepository;
    private final HelloBikeRepository helloBikeRepository;
    private final CarInfoRepository carInfoRepository;

    public MapInfoService(MapVertexInfoRepository mapVertexInfoRepository,
                          DestinationRepository destinationRepository,
                          HelloBikeRepository helloBikeRepository, CarInfoRepository carInfoRepository){
        this.mapVertexInfoRepository = mapVertexInfoRepository;
        this.destinationRepository = destinationRepository;
        this.helloBikeRepository = helloBikeRepository;
        this.carInfoRepository = carInfoRepository;
    }

    public List<MapVertexInfo> searchParkingSimple(String keyword) {
        return mapVertexInfoRepository.findByVertexNameLike("%"+keyword+"%");
    }

    public List<MapVertexResponse> searchParking(String keyword) {
        return mapVertexInfoRepository.findByVertexNameLike("%"+keyword+"%")
                .stream()
                .map(this::dataToResponse)
                .collect(Collectors.toList());
    }

    public List<Destination> searchPlace(String keyword) {
        return destinationRepository.findByPlaceNameLike("%"+keyword+"%");
    }

    public int getCurrentBike(int parkID){
        return helloBikeRepository.findBikeCounts(String.valueOf(parkID));
    }

    public int getCurrentMotor(int parkID){
        return 0; // TODO
    }

    public List<HelloBikeInfo> nearbyBikes (double lng,double lat){
        return helloBikeRepository.findNearbyBikes(lng,lat);
        //        return helloBikeRepository.findAllByLngBetweenAndLatBetweenAndTimeAfter
//                (lng-0.002, lng+0.002,
//                lat -0.002, lat+0.002, LocalDateTime.now().minusMinutes(2));
    }

    public List<CarInfo> nearbyCars (){
        return carInfoRepository.findCurrentCars();
        //        return helloBikeRepository.findAllByLngBetweenAndLatBetweenAndTimeAfter
//                (lng-0.002, lng+0.002,
//                lat -0.002, lat+0.002, LocalDateTime.now().minusMinutes(2));
    }




    public List<MapVertexResponse> nearbyParking(Point a){
        Polygon window = nearbyWindow(a);
        return mapVertexInfoRepository.findNearbyPoint(window)
                .stream()
                .map(this::dataToResponse)
                .collect(Collectors.toList());
    }




    /**
     * 根据数据库查询结果，查询实时单车数
     * @param info 传入数据库对象
     */
    public MapVertexResponse dataToResponse (MapVertexInfo info) {
        MapVertexResponse result = new MapVertexResponse();
        result.setVertexInfo (info);
        result.setBikeCount(getCurrentBike(info.getVertexID()));
        result.setMotorCount(getCurrentMotor(info.getVertexID()));
        return result;
    }


    private Polygon nearbyWindow(Point t){
        double lng_shift = 0.01;
        double lat_shift = 0.01;
        double x = t.getCoordinate().x;
        double y = t.getCoordinate().y;
        return new GeometryFactory().createPolygon(
                new Coordinate[]{
                        new Coordinate(x - lng_shift, y - lat_shift),
                        new Coordinate(x-lng_shift,y+lat_shift),
                        new Coordinate(x+lng_shift,y+lat_shift),
                        new Coordinate(x+lng_shift,y-lat_shift),
                        new Coordinate(x - lng_shift, y - lat_shift)
                });
    }
}
