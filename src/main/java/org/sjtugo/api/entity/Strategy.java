package org.sjtugo.api.entity;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.sjtugo.api.service.NavigateService.NavigatePlace;

import javax.validation.constraints.AssertTrue;
import java.time.Duration;
import java.util.List;

@Data
@ApiModel("返回方案详情")
public class Strategy {
    @ApiModelProperty(value = "方案类型", example = "哈罗单车")
    private String type;
    @ApiModelProperty(value = "出发地点名称", example = "上院215")
    private String depart;
    @ApiModelProperty(value = "到达地点名称", example = "121.3,31.2附近的位置")
    private String arrive;
    @ApiModelProperty(value = "途径地点名称",
            example = "[121.3;31.2附近的位置, 电院4号楼]")
    private List<String> pass;

    @ApiModelProperty(value = "方案总用时，单位为秒", dataType = "int",
            example = "423")
    private Duration travelTime;

    @ApiModelProperty(value = "方案总距离，单位为米", example = "589")
    private int distance;

    @ApiModelProperty(value = "方案总花费，单位为分", example = "150")
    private int cost;

    @ApiModelProperty(value = "用户查询时提交的个性化选项", example = "[避开拥堵, 允许禁停区]")
    private List<String> preference;

    @ApiModelProperty(value = "路线方案详情")
    private List<Route> routeplan;

    @ApiModelProperty(value = "起始点详情")
    private NavigatePlace beginDetail;

    @ApiModelProperty(value = "终点详情")
    private NavigatePlace endDetail;

    @ApiModelProperty(value = "途径点详情")
    private List<NavigatePlace> passDetail;

    /**
     * 拼接另一个Strategy到当前Strategy
     * @param nextStrategy 另一个Strategy，要求该Strategy不存在途经点，且出发点与当前实例的到达点一致，允许包含多条Route。
     */
    public void merge(Strategy nextStrategy){
        // pre-condition: self.arrive == nextStrategy.depart, nextStrategy.pass = {}
//        System.out.println(nextStrategy);
//        System.out.println(this);
        pass.add(this.arrive);
        passDetail.add(this.endDetail);
        arrive = nextStrategy.getArrive();
        endDetail = nextStrategy.endDetail;
        travelTime = travelTime.plus(nextStrategy.travelTime);
        preference = nextStrategy.preference;
        distance += nextStrategy.distance;
        cost += nextStrategy.cost;
        routeplan.addAll(nextStrategy.routeplan);

    }


}
