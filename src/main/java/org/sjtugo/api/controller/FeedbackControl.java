package org.sjtugo.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.sjtugo.api.DAO.FeedbackRepository;
import org.sjtugo.api.entity.Feedback;
import org.sjtugo.api.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value="Feedback System")
@RestController
@RequestMapping("/feedback")
public class FeedbackControl {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @ApiOperation(value = "用户添加行程反馈")
    @PostMapping("/addfeedback")
    public ResponseEntity<?> addFeedback(@RequestBody FeedbackRequest feedbackRequest){
        FeedbackService feedbackser = new FeedbackService(feedbackRepository);
        return feedbackser.addFeedback(feedbackRequest.getUserID(),
                feedbackRequest.getTripID(),
                feedbackRequest.getPickupFB(),
                feedbackRequest.getTrafficFB(),
                feedbackRequest.getParkFB(),
                feedbackRequest.getServiceFB(),
                feedbackRequest.getContents());
    }

    @Data
    static class FeedbackRequest{
        @ApiModelProperty(value = "用户ID", example = "123")
        private Integer userID;

        @ApiModelProperty(value = "行程ID", example = "123")
        private Integer tripID;

        @ApiModelProperty(value = "取车反馈", example="5")
        private Integer pickupFB;

        @ApiModelProperty(value = "交通反馈", example="5")
        private Integer trafficFB;

        @ApiModelProperty(value = "停车反馈", example="5")
        private Integer parkFB;

        @ApiModelProperty(value = "服务反馈", example="5")
        private Integer serviceFB;

        @ApiModelProperty(value = "文字反馈内容")
        private String contents;
    }

}


