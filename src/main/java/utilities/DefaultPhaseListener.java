package utilities;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletContext;

/**
 *
 * @author mario
 */

public class DefaultPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {       
        FacesContext facesContext = event.getFacesContext();
        if(facesContext.getViewRoot() == null){
            try {
                ExternalContext econtext = facesContext.getExternalContext();
                String contextPath = ((ServletContext) econtext.getContext()).getContextPath();
                econtext.redirect(contextPath  + "/");
            } catch (IOException ex) {
                Logger.getLogger(DefaultPhaseListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
