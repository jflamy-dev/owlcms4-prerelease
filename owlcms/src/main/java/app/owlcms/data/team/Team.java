/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.data.team;

import java.util.Comparator;

import org.apache.commons.lang3.ObjectUtils;

import app.owlcms.data.athlete.Gender;

/**
 * A non-persistent class to assist in creating reports and team results
 *
 * @author Jean-François Lamy
 */
public class Team {

    public static Comparator<Team> scoreComparator = ((a, b) -> -ObjectUtils.compare(a.score, b.score, true));

    public static Comparator<Team> pointsComparator = ((a,
            b) -> -ObjectUtils.compare(a.getPoints(), b.getPoints(), true));

    private String name;

    private double score = 0.0D;

    private int points = 0;

    private int counted;

    private long size;

    private Gender gender;

    public Team(String curTeamName, Gender gender) {
        name = curTeamName;
        this.gender = gender;
    }

    public int getCounted() {
        return counted;
    }

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public double getScore() {
        return score;
    }

    public long getSize() {
        return size;
    }

    public void setCounted(int counted) {
        this.counted = counted;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setScore(double d) {
        this.score = d;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
