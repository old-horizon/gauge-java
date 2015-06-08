// Copyright 2015 ThoughtWorks, Inc.

// This file is part of Gauge-Java.

// This program is free software.
//
// It is dual-licensed under:
// 1) the GNU General Public License as published by the Free Software Foundation,
// either version 3 of the License, or (at your option) any later version;
// or
// 2) the Eclipse Public License v1.0.
//
// You can redistribute it and/or modify it under the terms of either license.
// We would then provide copied of each license in a separate .txt file with the name of the license as the title of the file.

package com.thoughtworks.gauge.processor;


import com.thoughtworks.gauge.StepValue;
import com.thoughtworks.gauge.refactor.JavaRefactoring;
import com.thoughtworks.gauge.refactor.RefactoringResult;
import gauge.messages.Messages;

public class RefactorRequestProcessor implements IMessageProcessor {

    public Messages.Message process(Messages.Message message) {
        Messages.RefactorRequest refactorRequest = message.getRefactorRequest();
        RefactoringResult result = new JavaRefactoring(StepValue.from(refactorRequest.getOldStepValue()),
                StepValue.from(refactorRequest.getNewStepValue()),
                refactorRequest.getParamPositionsList()).performRefactoring();

        return createRefactorResponse(message, result);
    }

    private Messages.Message createRefactorResponse(Messages.Message message, RefactoringResult result) {
        return Messages.Message.newBuilder()
                .setMessageId(message.getMessageId())
                .setMessageType(Messages.Message.MessageType.RefactorResponse)
                .setRefactorResponse(Messages.RefactorResponse.newBuilder().setSuccess(result.passed()).setError(result.errorMessage()).addFilesChanged(result.fileChanged()).build())
                .build();
    }

}
