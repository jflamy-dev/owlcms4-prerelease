/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 * 
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)  
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.data.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.LoggerFactory;

import app.owlcms.data.athlete.Athlete;
import app.owlcms.data.athlete.Gender;
import app.owlcms.i18n.Translator;
import ch.qos.logback.classic.Logger;

public class TeamTreeItem extends Team {
    
    @SuppressWarnings("unused")
    private final Logger logger = (Logger) LoggerFactory.getLogger(TeamTreeItem.class);

    private Athlete athlete;

    private TeamTreeItem parent;

    private List<TeamTreeItem> teamMembers;

    private boolean done;

    public TeamTreeItem(String curTeamName, Gender gender, Athlete teamMember, boolean done) {
        super(curTeamName, gender);
        this.athlete = teamMember;
        this.setDone(done);
        if (this.athlete == null) {
            // we are a team
            this.teamMembers = new ArrayList<>();
        }
    }

    public Integer getCleanJerkPoints() {
        return toInteger(athlete.getCleanJerkPoints());
    }

    public Integer getCombinedPoints() {
        return toInteger(athlete.getCombinedPoints());
    }

    public Integer getCustomPoints() {
        return toInteger(athlete.getCustomPoints());
    }

    @Override
    public String getName() {
        if (athlete == null) {
            return super.getName();
        } else {
            return athlete.getFullName();
        }
    }
    
    public String formatName() {
        if (athlete == null) {
            return Translator.translate("TeamResults.TeamNameFormat",super.getName(),getGender());
        } else {
            return athlete.getFullName();
        }
    }

    public TeamTreeItem getParent() {
        return parent;
    }

    public Integer getSnatchPoints() {
        return toInteger(athlete.getSnatchPoints());
    }

    public String getTeam() {
        return athlete.getTeam();
    }

    public List<TeamTreeItem> getTeamMembers() {
        if (teamMembers == null) {
            return Collections.emptyList();
        }
        return teamMembers;
    }
    
    public List<TeamTreeItem> getSortedTeamMembers() {
        if (teamMembers == null) {
            return Collections.emptyList();
        }
        teamMembers.sort(Comparator.comparing(TeamTreeItem::getPoints, (a,b) -> ObjectUtils.compare(a, b, true)));
        return teamMembers;
    }

    public Integer getTotalPoints() {
        return toInteger(athlete != null ? athlete.getTotalPoints() : null);
    }

    public void setParent(TeamTreeItem parent) {
        this.parent = parent;
    }

    public void setTeamMembers(List<TeamTreeItem> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public void addTreeItemChild(Athlete a, boolean done) {
        TeamTreeItem child = new TeamTreeItem(null, a.getGender(), a, done);
        child.setParent(this);
        teamMembers.add(child);
    }

    private Integer toInteger(Float f) {
        return f == null ? null : Math.round(f);
    }

    public Athlete getAthlete() {
        return athlete;
    }
    
    public String formatPoints() {
        Integer pts = getPoints();
        return (pts == null ? "" : pts.toString());    
    }

    private Integer getPoints() {
        Integer pts;
        if (athlete == null) {
            pts = getScore();
        } else {
            pts = isDone() ? getTotalPoints() : null;
        }
        return pts;
    }
    
    public String formatProgress() {
        if (athlete != null) {
            return isDone() ? Translator.translate("Done") : "";
        } else {
            return getCounted() + "/" + getSize();
        }
    }

    private boolean isDone() {
        return done;
    }

    private void setDone(boolean done) {
        this.done = done;
    }

}