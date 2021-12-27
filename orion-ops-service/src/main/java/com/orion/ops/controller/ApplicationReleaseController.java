package com.orion.ops.controller;

import com.orion.lang.wrapper.DataGrid;
import com.orion.lang.wrapper.HttpWrapper;
import com.orion.ops.annotation.RestWrapper;
import com.orion.ops.consts.AuditStatus;
import com.orion.ops.entity.request.ApplicationReleaseAuditRequest;
import com.orion.ops.entity.request.ApplicationReleaseRequest;
import com.orion.ops.entity.vo.ApplicationReleaseDetailVO;
import com.orion.ops.entity.vo.ApplicationReleaseListVO;
import com.orion.ops.entity.vo.ApplicationReleaseMachineVO;
import com.orion.ops.entity.vo.ApplicationReleaseStatusVO;
import com.orion.ops.service.api.ApplicationReleaseService;
import com.orion.ops.utils.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应用发布api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/11 22:15
 */
@RestController
@RestWrapper
@RequestMapping("/orion/api/app-release")
public class ApplicationReleaseController {

    @Resource
    private ApplicationReleaseService applicationReleaseService;

    /**
     * 发布列表
     */
    @RequestMapping("/list")
    public DataGrid<ApplicationReleaseListVO> getReleaseList(@RequestBody ApplicationReleaseRequest request) {
        return applicationReleaseService.getReleaseList(request);
    }

    /**
     * 发布详情
     */
    @RequestMapping("/detail")
    public ApplicationReleaseDetailVO getReleaseDetail(@RequestBody ApplicationReleaseRequest request) {
        Long id = Valid.notNull(request.getId());
        return applicationReleaseService.getReleaseDetail(id);
    }

    /**
     * 发布机器详情
     */
    @RequestMapping("/machine/detail")
    public ApplicationReleaseMachineVO getReleaseMachineDetail(@RequestBody ApplicationReleaseRequest request) {
        Long releaseMachineId = Valid.notNull(request.getReleaseMachineId());
        return applicationReleaseService.getReleaseMachineDetail(releaseMachineId);
    }

    /**
     * 提交发布
     */
    @RequestMapping("/submit")
    public Long submitAppRelease(@RequestBody ApplicationReleaseRequest request) {
        Valid.notBlank(request.getTitle());
        Valid.notNull(request.getAppId());
        Valid.notNull(request.getProfileId());
        Valid.notNull(request.getBuildId());
        Valid.notEmpty(request.getMachineIdList());
        return applicationReleaseService.submitAppRelease(request);
    }

    /**
     * 发布审核
     */
    @RequestMapping("/audit")
    public Integer auditAppRelease(@RequestBody ApplicationReleaseAuditRequest request) {
        Valid.notNull(request.getId());
        AuditStatus status = Valid.notNull(AuditStatus.of(request.getStatus()));
        if (AuditStatus.REJECT.equals(status)) {
            Valid.notBlank(request.getReason());
        }
        return applicationReleaseService.auditAppRelease(request);
    }

    /**
     * 执行发布
     */
    @RequestMapping("/runnable")
    public HttpWrapper<?> runnableAppRelease(@RequestBody ApplicationReleaseRequest request) {
        applicationReleaseService.runnableAppRelease(request);
        return HttpWrapper.ok();
    }

    /**
     * 回滚发布
     */
    @RequestMapping("/rollback")
    public Long rollbackAppRelease(@RequestBody ApplicationReleaseRequest request) {
        Long id = Valid.notNull(request.getId());
        return applicationReleaseService.rollbackAppRelease(id);
    }

    /**
     * 发布状态列表
     */
    @RequestMapping("/list/status")
    public List<ApplicationReleaseStatusVO> getReleaseStatusList(@RequestBody ApplicationReleaseRequest request) {
        List<Long> idList = Valid.notEmpty(request.getIdList());
        return applicationReleaseService.getReleaseStatusList(idList);
    }

    /**
     * 发布状态
     */
    @RequestMapping("/status")
    public ApplicationReleaseStatusVO getReleaseStatus(@RequestBody ApplicationReleaseRequest request) {
        Long id = Valid.notNull(request.getId());
        return applicationReleaseService.getReleaseStatus(id);
    }

}
