/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.uievents;

public class DecisionEvent {

    private Boolean decisionLight1;
    private Boolean decisionLight2;
    private Boolean decisionLight3;
    private boolean down;
    private boolean decisionLightsVisible;
    private String fopName;
    private boolean isBreak;
    private DecisionEventType eventType;
    private boolean done;
    private String groupName;

    public DecisionEvent() {
    }

    public Boolean getDecisionLight1() {
        return decisionLight1;
    }

    public Boolean getDecisionLight2() {
        return decisionLight2;
    }

    public Boolean getDecisionLight3() {
        return decisionLight3;
    }

    public DecisionEventType getEventType() {
        return eventType;
    }

    public String getFopName() {
        return fopName;
    }

    public boolean isBreak() {
        return isBreak;
    }

    public boolean isDecisionLightsVisible() {
        return decisionLightsVisible;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isDown() {
        return down;
    }

    public void setBreak(boolean isBreak) {
        this.isBreak = isBreak;
    }

    public void setDecisionLight1(Boolean decisionLight1) {
        this.decisionLight1 = decisionLight1;
    }

    public void setDecisionLight2(Boolean decisionLight2) {
        this.decisionLight2 = decisionLight2;
    }

    public void setDecisionLight3(Boolean decisionLight3) {
        this.decisionLight3 = decisionLight3;
    }

    public void setDecisionLightsVisible(boolean decisionLightsVisible) {
        this.decisionLightsVisible = decisionLightsVisible;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setEventType(DecisionEventType eventType) {
        this.eventType = eventType;
    }

    public void setFopName(String fopName) {
        this.fopName = fopName;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
