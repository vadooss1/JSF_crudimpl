package com.vvz.model;

import org.primefaces.event.RowEditEvent;
import com.vvz.persisting.DAO;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name="dtEditView")
@ViewScoped
public class EditView implements Serializable {

    public static DAO dao = new DAO();

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Contact Edited", String.valueOf(((User) event.getObject()).getId()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        dao.addUser(((User) event.getObject()));
    }


    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", String.valueOf(((User) event.getObject()).getId()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
