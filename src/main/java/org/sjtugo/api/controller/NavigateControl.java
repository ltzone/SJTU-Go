package org.sjtugo.api.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.entity.Strategy;
import io.swagger.annotations.*;
import org.sjtugo.api.service.planner.BusPlanner;
import org.sjtugo.api.service.planner.WalkPlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api(value="Navigate System")
@RestController
@RequestMapping("/navigate")
public class NavigateControl {
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;
    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BusTimeRepository busTimeRepository;
    @Autowired
    private BusStopRepository busStopRepository;


    @ApiOperation(value = "Walk Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回步行方案")
    @PostMapping("/walk")
    public Strategy navigateWalk(@RequestBody NavigateRequest navigateRequest) {
        WalkPlanner planner = new WalkPlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate);
        String[] passPlaces =new String[navigateRequest.getPassPlaces().size()];
        return planner.planAll(navigateRequest.getBeginPlace(),
                navigateRequest.getPassPlaces().toArray(passPlaces),
                navigateRequest.getArrivePlace(),
                navigateRequest.getDepartTime());
    }

    @ApiOperation(value = "Bus Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回校园巴士出行方案")
    @PostMapping("/bus")
    public Strategy navigateBus(@RequestBody NavigateRequest navigateRequest) {
        BusPlanner planner = new BusPlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate, busTimeRepository,busStopRepository);
        String[] passPlaces =new String[navigateRequest.getPassPlaces().size()];
        return planner.planAll(navigateRequest.getBeginPlace(),
                navigateRequest.getPassPlaces().toArray(passPlaces),
                navigateRequest.getArrivePlace(),
                navigateRequest.getDepartTime());
    }

    @ApiOperation(value = "Parse Place",
            notes = "给定校园内地点ID或经纬度，返回地点信息。要求地点的格式：“VT+NUMID”,”DT+NUMID“," +
                    "”POINT(经度 纬度)“，若不满足以上格式，将会被当做搜索关键词，通过腾讯地图API在交大校园" +
                    "内搜索相关地点，匹配地名、经纬度。")
    @PostMapping(value = "/parsePlace", produces="text/plain;charset=UTF-8")
    public String parsePlace(@RequestBody String place) {
        BusPlanner planner = new BusPlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate, busTimeRepository,busStopRepository);
        return planner.parsePlace(place).toString();
    }

//    @PostMapping(path="/bus/addRecord")
//    public @ResponseBody String addNewBusStop (@RequestParam String stopName
//            , @RequestParam String stopLoc) throws ParseException {
//        BusStop n = new BusStop();
//        n.setStopName(stopName);
//        n.setLocation((Point) new WKTReader().read(stopLoc));
//        busStopRepository.save(n);
//        return "Saved";
//    }

    @ApiModel(value = "导航输入数据")
    @Data
    static class NavigateRequest {
        @ApiModelProperty(value = "出发点ID或经纬度", required = true,
                example = "图书馆")
        private String beginPlace;
        @ApiModelProperty(value = "到达点ID或经纬度", required = true,
                example = "POINT (121.435505 31.026303)")
        private String arrivePlace;
        @ApiModelProperty(value = "途径点ID或经纬度",
                example = "[\"学生服务中心\"]")
        private List<String> passPlaces = Collections.emptyList();


        @ApiModelProperty(value = "出发时间（必须严格按照格式，不足位用0补齐），可不设该字段，默认为当前时间",
                example = "2020/05/11 12:05:12")
        @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime departTime = LocalDateTime.now();
    }
}