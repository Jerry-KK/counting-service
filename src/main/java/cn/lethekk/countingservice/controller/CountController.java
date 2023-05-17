package cn.lethekk.countingservice.controller;

import cn.lethekk.countingservice.enums.EventType;
import cn.lethekk.countingservice.service.CountMsgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lethe
 * @date 2023/5/15 20:13
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/count")
public class CountController {

    static String SUCCESS = "success";

    private final CountMsgService countMsgService;


    /**
     * 记录观看数
     * @param videoId   视频ID
     */
    @GetMapping("/view")
    public String countViewEvent(@RequestParam("videoId") Long videoId) {
        countMsgService.countEvent(videoId, EventType.VIEW.getCode());
        return SUCCESS;
    }

    /**
     * 对视频的某个事件进行计数
     * @param videoId   视频ID
     * @param eventTypeCode  事件类型，可以是“观看”、”喜欢“和”分享“
     */
    @GetMapping("/event")
    public String countEvent(@RequestParam("videoId")Long videoId,
                             @RequestParam("eventTypeCode") Integer eventTypeCode) {    //eventType: view like share
        countMsgService.countEvent(videoId, eventTypeCode);
        return SUCCESS;
    }



}
