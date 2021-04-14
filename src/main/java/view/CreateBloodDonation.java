package view;

import common.EMFactory;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodDonationLogic;
import logic.LogicFactory;

/**
 *
 * @author camyk
 */
@WebServlet(name = "CreateBloodDonation", urlPatterns = {"/CreateBloodDonation"})
public class CreateBloodDonation extends HttpServlet {

    private String errorMessage = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create Blood Donation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            out.println("Milliliters:<br>");
         
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");
            out.println("Blood Bank ID:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BANK_ID);
            out.println("<br>");
            out.println("Blood Group: <br>");;
            out.printf("<select name=\"%s\">", BloodDonationLogic.BLOOD_GROUP);
            out.printf("<option value=\"%s\">A</option>", BloodGroup.A);
            out.printf("<option value=\"%s\">B</option>", BloodGroup.B);
            out.printf("<option value=\"%s\">AB</option>", BloodGroup.AB);
            out.printf("<option value=\"%s\">O</option>", BloodGroup.O);
            out.println("</select><br><br>");
            out.println("RHD: <br>");
            out.printf("<select name=\"%s\">", BloodDonationLogic.RHESUS_FACTOR);
            out.printf("<option value=\"%s\">Positive</option>", RhesusFactor.Positive);
            out.printf("<option value=\"%s\">Negative</option>", RhesusFactor.Negative);
            out.println("</select><br><br>");
            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        EntityManager em = EMFactory.getEMF().createEntityManager();
        // BloodDonation bd = em.find(BloodDonation.class, BloodDonationLogic.BANK_ID);
        Integer bbInt= Integer.parseInt(request.getParameter(BloodDonationLogic.BANK_ID));
        BloodBank bb = em.find(BloodBank.class,bbInt );
        
  /**
   * check if dependency blood bank entity exists. if not, generate error message
   */              
        if (bb == null) {
            errorMessage = "BloodBank: \"" + BloodDonationLogic.BANK_ID + "\" does not exists";
        } else {
            try {
              
                BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
                BloodDonation bloodDonation = bdLogic.createEntity(request.getParameterMap());
                bdLogic.add(bloodDonation);

            } catch (Exception ex) {
                Logger.getLogger(CreateBloodDonation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (request.getParameter("add") != null) {
            processRequest(request, response);
        } else if (request.getParameter("view") != null) {
            response.sendRedirect("BloodDonationTable");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a Blood Donation Entity";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
