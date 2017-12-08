package com.heizi.pointsuser.block.my;

import com.heizi.mycommon.model.BaseModel;

/**
 * Created by leo on 17/12/6.
 */

public class ModelFeedback extends BaseModel {
    private String id;
    private String issue;
    private String answer;
    private boolean isOpen=true;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
