package org.sjtugo.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@Entity
@ApiModel(value = "日程信息")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "日程ID", example = "123")
    private Integer scheduleID;

    @ApiModelProperty(value = "用户ID", example = "123")
    private Integer userID;

    @ApiModelProperty(value = "年月", example="2020-5")
    private String yearMonh;

    @ApiModelProperty(value = "日", example="14")
    private String selectDay;

    @ApiModelProperty(value = "小时", example="12")
    private String timeHour;

    @ApiModelProperty(value = "分钟", example="30")
    private String timeMinute;

    @ApiModelProperty(value = "日程名称", example="年纪大会")
    private String scheduleName;

    @ApiModelProperty(value = "起点", example="信息楼")
    private String departShow;

    @ApiModelProperty(value = "终点", example="激光楼E楼")
    private String arriveShow;

    @ApiModelProperty(value = "起点编码", example="DT137348")
    private String depart;

    @ApiModelProperty(value = "终点编码", example="DT137224")
    private String arrive;

    public Schedule() {}
}
