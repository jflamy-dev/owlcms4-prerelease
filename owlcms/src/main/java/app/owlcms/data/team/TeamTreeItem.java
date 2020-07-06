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

public class TeamTreeItem {

    @SuppressWarnings("unused")
    private final static Logger logger = (Logger) LoggerFactory.getLogger(TeamTreeItem.class);

    public static Comparator<TeamTreeItem> pointComparator = ((a, b) -> {
        int compare = 0;
        compare = ObjectUtils.compare(a.getGender(), b.getGender(), true);
        if (compare != 0) {
            return compare;
        }
        compare = -ObjectUtils.compare(a.getPoints(), b.getPoints(), true);
        return compare;
    });

    public static Comparator<TeamTreeItem> scoreComparator = ((a, b) -> {
        int compare = 0;
        compare = ObjectUtils.compare(a.getGender(), b.getGender(), true);
        if (compare != 0) {
            return compare;
        }
        compare = -ObjectUtils.compare(a.getScore(), b.getScore(), true);
        return compare;
    });

    private Athlete athlete;
    private boolean done;
    private TeamTreeItem parent;
    private Team team;

    private List<TeamTreeItem> teamMembers;

    public TeamTreeItem(String curTeamName, Gender gender, Athlete teamMember, boolean done) {
        this.athlete = teamMember;
        this.setDone(done);
        if (this.athlete == null) {
            // we are a team
            this.setTeam(new Team(curTeamName, gender));
            this.teamMembers = new ArrayList<>();
        }
    }

    public void addTreeItemChild(Athlete a, boolean done) {
        TeamTreeItem child = new TeamTreeItem(null, a.getGender(), a, done);
        child.setParent(this);
        teamMembers.add(child);
    }

    public String formatName() {
        if (athlete == null) {
            return Translator.translate("TeamResults.TeamNameFormat", getTeam().getName(), getTeam().getGender());
        } else {
            return athlete.getFullName();
        }
    }

    public String formatProgress() {
        if (athlete != null) {
            return isDone() ? Translator.translate("Done") : "";
        } else {
            return getTeam().getCounted() + "/" + getTeam().getSize();
        }
    }

    public String formatScore() {
        Integer pts = getPoints();
        return (pts == null ? "" : Integer.toString(pts));
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public Integer getCleanJerkPoints() {
        return athlete.getCleanJerkPoints();
    }

    public Integer getCombinedPoints() {
        return athlete.getCombinedPoints();
    }

    public Integer getCounted() {
        return team != null ? team.getCounted() : null;
    }

    public Integer getCustomPoints() {
        return athlete.getCustomPoints();
    }

    public Gender getGender() {
        return team != null ? team.getGender() : athlete.getGender();
    }

    public String getName() {
        if (athlete == null) {
            return getTeam().getName();
        } else {
            return athlete.getFullName();
        }
    }

    public TeamTreeItem getParent() {
        return parent;
    }

    public Integer getPoints() {
        Integer pts;
        if (athlete == null) {
            pts = getTeam().getPoints();
        } else {
            pts = isDone() ? getTotalPoints() : null;
        }
        return pts;
    }

    public Double getScore() {
        return (team != null ? team.getScore() : athlete.getSinclairForDelta());
    }

    public long getSize() {
        return team != null ? team.getSize() : null;
    }

    public Integer getSnatchPoints() {
        return athlete.getSnatchPoints();
    }

    public List<TeamTreeItem> getSortedTeamMembers() {
        if (teamMembers == null) {
            return Collections.emptyList();
        }
        teamMembers.sort(Comparator.comparing(TeamTreeItem::getPoints, (a, b) -> ObjectUtils.compare(a, b, true)));
        return teamMembers;
    }

    public Team getTeam() {
        return team;
    }

    public List<TeamTreeItem> getTeamMembers() {
        if (teamMembers == null) {
            return Collections.emptyList();
        }
        return teamMembers;
    }

    public String getTeamName() {
        return athlete.getTeam();
    }

    public Integer getTotalPoints() {
        return (athlete != null ? athlete.getTotalPoints() : null);
    }

    public void setParent(TeamTreeItem parent) {
        this.parent = parent;
    }

    public void setTeamMembers(List<TeamTreeItem> teamMembers) {
        this.teamMembers = teamMembers;
    }

    private boolean isDone() {
        return done;
    }

    private void setDone(boolean done) {
        this.done = done;
    }

    private void setTeam(Team team) {
        this.team = team;
    }
}
