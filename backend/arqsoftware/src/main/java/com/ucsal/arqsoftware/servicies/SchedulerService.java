package com.ucsal.arqsoftware.servicies;

import com.ucsal.arqsoftware.entities.ApprovalHistory;
import com.ucsal.arqsoftware.job.MarkAvailableJob;
import com.ucsal.arqsoftware.job.MarkUnavailableJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class SchedulerService {

    @Autowired
    private Scheduler scheduler;

    public void scheduleApprovalHistoryJobs(ApprovalHistory entity) throws SchedulerException {
        scheduleStartJob(entity);
        scheduleEndJob(entity);
    }

    private void scheduleStartJob(ApprovalHistory entity) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(MarkUnavailableJob.class)
                .withIdentity("markUnavailableJob_" + entity.getRequest().getId())
                .usingJobData("approvalHistoryId", entity.getRequest().getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(entity.getRequest().getDateTimeStart().toInstant()))
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    private void scheduleEndJob(ApprovalHistory entity) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(MarkAvailableJob.class)
                .withIdentity("markAvailableJob_" + entity.getId())
                .usingJobData("approvalHistoryId", entity.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(entity.getRequest().getDateTimeEnd().toInstant()))
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}