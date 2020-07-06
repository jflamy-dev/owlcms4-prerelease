/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.spreadsheet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.vaadin.flow.component.UI;

import app.owlcms.data.athlete.Athlete;
import app.owlcms.data.competition.Competition;
import app.owlcms.data.jpa.JPAService;
import app.owlcms.i18n.Translator;
import app.owlcms.init.OwlcmsSession;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Result sheet, with team rankings
 *
 * @author jflamy
 *
 */
public class JXLSCompetitionBook extends JXLSWorkbookStreamSource {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(JXLSCompetitionBook.class);
    private byte[] finalPackageTemplate;

    public JXLSCompetitionBook(boolean excludeNotWeighed, UI ui) {
        super(ui);
    }

    public JXLSCompetitionBook(UI ui) {
        // by default, we exclude athletes who did not weigh in.
        super(ui);
    }

    @Override
    public InputStream getTemplate(Locale locale) throws IOException {
        Competition current = Competition.getCurrent();
        finalPackageTemplate = current.getFinalPackageTemplate();
        if (finalPackageTemplate == null) {
            finalPackageTemplate = loadDefaultPackageTemplate(locale, current);
        }
        InputStream stream = new ByteArrayInputStream(finalPackageTemplate);
        return stream;
    }

    @Override
    protected void configureTransformer(XLSTransformer transformer) {
        super.configureTransformer(transformer);
        transformer.markAsFixedSizeCollection("clubs");
        transformer.markAsFixedSizeCollection("mTeam");
        transformer.markAsFixedSizeCollection("wTeam");
        transformer.markAsFixedSizeCollection("mwTeam");
        transformer.markAsFixedSizeCollection("mCombined");
        transformer.markAsFixedSizeCollection("wCombined");
        transformer.markAsFixedSizeCollection("mCustom");
        transformer.markAsFixedSizeCollection("wCustom");
    }

    @Override
    protected List<Athlete> getSortedAthletes() {
        // not used (setReportingInfo does all the work)
        return null;
    }

    /*
     * team result sheets need columns hidden, print area fixed
     *
     * @see org.concordiainternational.competition.spreadsheet.JXLSWorkbookStreamSource#
     * postProcess(org.apache.poi.ss.usermodel.Workbook)
     */
    @Override
    protected void postProcess(Workbook workbook) {
        super.postProcess(workbook);
        @SuppressWarnings("unchecked")
        int nbClubs = ((Set<String>) getReportingBeans().get("clubs")).size();

        setTeamSheetPrintArea(workbook, "MT", nbClubs);
        setTeamSheetPrintArea(workbook, "WT", nbClubs);
        setTeamSheetPrintArea(workbook, "MWT", nbClubs);

        setTeamSheetPrintArea(workbook, "MXT", nbClubs);
        setTeamSheetPrintArea(workbook, "WXT", nbClubs);

        setTeamSheetPrintArea(workbook, "MCT", nbClubs);
        setTeamSheetPrintArea(workbook, "WCT", nbClubs);
        setTeamSheetPrintArea(workbook, "MWCT", nbClubs);

        translateSheets(workbook);
        workbook.setForceFormulaRecalculation(true);

    }

    @Override
    protected void setReportingInfo() {
        super.setReportingInfo();
        Competition competition = Competition.getCurrent();
        competition.computeGlobalRankings(true);
        setReportingBeans(competition.getReportingBeans());
    }

    private byte[] loadDefaultPackageTemplate(Locale locale, Competition current) {
        JPAService.runInTransaction((em) -> {
            String protocolTemplateFileName = "/templates/competitionBook/CompetitionBook_Total_" + locale.getLanguage()
                    + ".xls";
            InputStream stream = this.getClass().getResourceAsStream(protocolTemplateFileName);
            try {
                finalPackageTemplate = ByteStreams.toByteArray(stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            current.setFinalPackageTemplate(finalPackageTemplate);
            Competition merge = em.merge(current);
            Competition.setCurrent(merge);
            return merge;
        });
        return finalPackageTemplate;
    }

    private void setTeamSheetPrintArea(Workbook workbook, String sheetName, int nbClubs) {
        // int sheetIndex = workbook.getSheetIndex(sheetName);
        // if (sheetIndex >= 0) {
        // workbook.setPrintArea(sheetIndex, 0, 4, TEAMSHEET_FIRST_ROW,
        // TEAMSHEET_FIRST_ROW+nbClubs);
        // }
    }

    private void translateSheets(Workbook workbook) {
        int nbSheets = workbook.getNumberOfSheets();
        for (int sheetIndex = 0; sheetIndex < nbSheets; sheetIndex++) {
            Sheet curSheet = workbook.getSheetAt(sheetIndex);
            String sheetName = curSheet.getSheetName();
            workbook.setSheetName(sheetIndex,
                    Translator.translate("CompetitionBook." + sheetName, OwlcmsSession.getLocale()));

            String leftHeader = Translator.translateOrElseNull("CompetitionBook." + sheetName + "_LeftHeader",
                    OwlcmsSession.getLocale());
            if (leftHeader != null) {
                curSheet.getHeader().setLeft(leftHeader);
            }
            String centerHeader = Translator.translateOrElseNull("CompetitionBook." + sheetName + "_CenterHeader",
                    OwlcmsSession.getLocale());
            if (centerHeader != null) {
                curSheet.getHeader().setCenter(centerHeader);
            }
            String rightHeader = Translator.translateOrElseNull("CompetitionBook." + sheetName + "_RightHeader",
                    OwlcmsSession.getLocale());
            if (rightHeader != null) {
                curSheet.getHeader().setRight(rightHeader);
            }

            String leftFooter = Translator.translateOrElseNull("CompetitionBook." + sheetName + "_LeftFooter",
                    OwlcmsSession.getLocale());
            if (leftFooter != null) {
                curSheet.getFooter().setLeft(leftFooter);
            }
            String centerFooter = Translator.translateOrElseNull("CompetitionBook." + sheetName + "_CenterFooter",
                    OwlcmsSession.getLocale());
            if (centerFooter != null) {
                curSheet.getFooter().setCenter(centerFooter);
            }
            String rightFooter = Translator.translateOrElseNull("CompetitionBook." + sheetName + "_RightFooter",
                    OwlcmsSession.getLocale());
            if (rightFooter != null) {
                curSheet.getFooter().setRight(rightFooter);
            }
        }
    }
}
