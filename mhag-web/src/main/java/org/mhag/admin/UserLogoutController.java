package org.mhag.admin;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 19-Oct-2011 19:26:32
 */
public class UserLogoutController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();


        final HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("username");
            session.removeAttribute("userId");
            session.invalidate();
        }

        return new ModelAndView("reglogin", "model", model);
    }
}