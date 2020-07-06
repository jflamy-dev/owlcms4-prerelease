/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.displays.attemptboard;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@SuppressWarnings("serial")
@Tag("attempt-board-template")
@JsModule("./components/AttemptBoard.js")
@Route("displays/athleteFacingAttempt")
@Push
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class AthleteFacingAttemptBoard extends AttemptBoard {

    public AthleteFacingAttemptBoard() {
        super();
        setPublicFacing(false);
    }

    @Override
    public String getPageTitle() {
        return getTranslation("AttemptAF");
    }

    public boolean isPublicFacing() {
        return Boolean.TRUE.equals(getModel().isPublicFacing());
    }

    public void setPublicFacing(boolean publicFacing) {
        getModel().setPublicFacing(publicFacing);
    }

    /*
     * (non-Javadoc)
     *
     * @see app.owlcms.displays.attemptboard.AttemptBoard#onAttach(com.vaadin.flow. component.AttachEvent)
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        decisions.setPublicFacing(false);
    }
}
