package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route
public class MainView extends AppLayout {
    public MainView() {

        var nav = getSideNav();
        var scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
    }


    private SideNav getSideNav() {
        var sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Post", "/post", VaadinIcon.NOTEBOOK.create()),
                new SideNavItem("Book", "/book", VaadinIcon.BOOK.create())
        );
        return sideNav;
    }
}
