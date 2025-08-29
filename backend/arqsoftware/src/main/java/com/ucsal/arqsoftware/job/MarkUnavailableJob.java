package com.ucsal.arqsoftware.job;

import com.ucsal.arqsoftware.entities.PhysicalSpace;
import com.ucsal.arqsoftware.repositories.ApprovalHistoryRepository;
import com.ucsal.arqsoftware.repositories.PhysicalSpaceRepository;
import com.ucsal.arqsoftware.repositories.RequestRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class MarkUnavailableJob implements Job {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PhysicalSpaceRepository physicalSpaceRepository;

    @Autowired
    private ApprovalHistoryRepository approvalHistoryRepository;

    @Override
    public void execute(JobExecutionContext context) {
        Long approvalHistoryId = context.getJobDetail().getJobDataMap().getLong("approvalHistoryId");

        PhysicalSpace physicalSpace = approvalHistoryRepository.findById(approvalHistoryId).get().getRequest().getPhysicalSpace();

        physicalSpace.setAvailability(false);
        physicalSpaceRepository.save(physicalSpace);

        System.out.println("Physical Space " + physicalSpace.getId() + " mark to UNAVAILABLE at " + LocalDateTime.now());

    }
}
