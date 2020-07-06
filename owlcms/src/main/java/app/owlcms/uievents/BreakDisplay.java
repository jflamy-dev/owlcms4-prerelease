/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.uievents;

import org.slf4j.LoggerFactory;

import app.owlcms.data.group.Group;
import app.owlcms.fieldofplay.FieldOfPlay;
import app.owlcms.i18n.Translator;
import app.owlcms.init.OwlcmsSession;
import ch.qos.logback.classic.Logger;

public interface BreakDisplay {

    Logger logger = (Logger) LoggerFactory.getLogger(BreakDisplay.class);

    public void doBreak();

    public default String inferGroupName() {
        FieldOfPlay fop = OwlcmsSession.getFop();
        if (fop == null) {
            return "";
        }
        Group group = fop.getGroup();
        String groupName = group != null ? group.getName() : "";
        return Translator.translate("Group_number", groupName);
    }

    public default String inferMessage(BreakType bt) {
        if (bt == null) {
            return Translator.translate("PublicMsg.CompetitionPaused");
        }
        switch (bt) {
        case FIRST_CJ:
            return Translator.translate("PublicMsg.TimeBeforeCJ");
        case FIRST_SNATCH:
            return Translator.translate("PublicMsg.TimeBeforeSnatch");
        case BEFORE_INTRODUCTION:
            return Translator.translate("PublicMsg.BeforeIntroduction");
        case DURING_INTRODUCTION:
            return Translator.translate("PublicMsg.DuringIntroduction");
        case TECHNICAL:
            return Translator.translate("PublicMsg.CompetitionPaused");
        case JURY:
            return Translator.translate("PublicMsg.JuryDeliberation");
        case GROUP_DONE:
            return Translator.translate("PublicMsg.GroupDone");
        default:
            return "";
        }
    }

}