package view;

import common.EMFactory;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.DonationRecord;
import entity.Person;
import entity.RhesusFactor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.BloodDonationLogic;
import logic.DonationRecordLogic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author camyk
 */
@WebServlet(name = "DonateBloodForm", urlPatterns = {"/DonateBloodFrom"})
public class DonateBloodForm extends HttpServlet {

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
            out.println("<title>Donate Blood Form</title>");
            out.println("<link rel=\"stylesheet\" href=\"bloodform.css\">");
            out.println("</head>");
            out.println("<br>");

            //Person Section Addition.
            out.println("<div class=\"grid-container\">");
            out.println("<h2>Person</h2>");
            out.println("<form method=\"post\">");
            out.println("<div class=\"grid-item\">First Name:</div>");
            out.println("<div class=\"grid-item\">");
            out.printf("<input type=\"text\" id=\"fname\" name=\"%s\" value=\"\">", PersonLogic.FIRST_NAME);
            out.println("</div>");
            out.println("<div class=\"grid-item\">Last Name:</div>");
            out.println("<div class=\"grid-item\">");
            out.printf("<input type=\"text\" id=\"lname\" name=\"%s\" value=\"\">", PersonLogic.LAST_NAME);
            out.println("</div>");
            out.println("<div class=\"grid-item\">Phone:</div>");
            out.println("<div class=\"grid-item\">");
            out.printf("<input type=\"tel\" id=\"phone\" name=\"%s\" placeholder=\"613-613-6131\" pattern=\"[0-9]{3}-[0-9]{3}-[0-9]{4}\">", PersonLogic.PHONE);
            out.println("</div>");
            out.println("<div class=\"grid-item\">Address:</div>");
            out.println("<div class=\"grid-item\">");
            out.printf("<input type=\"text\" id=\"address\" name=\"%s\" value=\"\">", PersonLogic.ADDRESS);
            out.println("</div>");
            out.println("<div class=\"grid-item\">Date of Birth:</div>");
            out.println("<div class=\"grid-item\">");
            out.printf("<input type=\"datetime-local\" id=\"dob\" name=\"%s\" value=\"\"><br>", PersonLogic.BIRTH);
            out.println("</div>");
            out.println("</div>");

            out.println("<h2>Blood</h2>");
            //out.println("<div id=donateBlood");
            out.println("Milliliters:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");
            out.println("Blood Bank:<br>");
            //out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BANK_ID);
            out.printf("<select name=\"%s\">", BloodDonationLogic.BANK_ID);
            BloodBankLogic bbLogic = LogicFactory.getFor("BloodBank");
            List<BloodBank> bbList = bbLogic.getAll();
            for (BloodBank bloodbank: bbList) {
                out.printf("<option value=\"%s\">%s</option>", bloodbank.getId(), bloodbank.getName());
            }
            out.println("</select><br>");
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

            out.println("<h2>Donation Record</h2>");

            out.println("Tested:<br>");
            out.printf("<input type=\"radio\" name=\"%s\" value=\"false\" checked> No ", DonationRecordLogic.TESTED);
            out.printf("<input type=\"radio\" name=\"%s\" value=\"true\"> Yes<br>", DonationRecordLogic.TESTED);
            out.println("<br>");

            out.println("Administrator:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.ADMINISTRATOR);
            out.println("<br>");

            out.println("Hospital:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.HOSPITAL);
            out.println("<br>");

            out.println("Created:<br>");
            out.printf("<input type=\"datetime-local\" name=\"%s\"><br>", DonationRecordLogic.CREATED);
            out.println("<br>");

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
        //PersonLogic Test in POST.
        PersonLogic pLogic = LogicFactory.getFor("Person");

        String firstName = request.getParameter(PersonLogic.FIRST_NAME);
        String lastName = request.getParameter(PersonLogic.LAST_NAME);

        String fullName = firstName + " " + lastName;

        Person person;
        person = pLogic.createEntity(request.getParameterMap());

        if (pLogic.getPersonWithFirstName(firstName).isEmpty()) {
            try {
                pLogic.add(person);
                errorMessage = null;
            } catch (Exception ex) {
                errorMessage = ex.getMessage();
            }
        } else {
            //if duplicate print the error message
            errorMessage = "Name: \"" + fullName + "\" already exists";
        }

        EntityManager bankEM = EMFactory.getEMF().createEntityManager();
        Integer bbInt = Integer.parseInt(request.getParameter(BloodDonationLogic.BANK_ID));
        BloodBank bb = bankEM.find(BloodBank.class, bbInt);
        BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
        /**
         * check if dependency blood bank entity exists. if not, generate error
         * message
         */

        BloodDonation bloodDonation;
        bloodDonation = bdLogic.createEntity(request.getParameterMap());

        if (bb == null) {
            errorMessage = "BloodBank: \"" + BloodDonationLogic.BANK_ID + "\" does not exists";
        } else {
            try {
                bloodDonation.setBloodBank(bb);
                bdLogic.add(bloodDonation);

            } catch (Exception ex) {
                Logger.getLogger(CreateBloodDonation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        DonationRecordLogic drLogic = LogicFactory.getFor("DonationRecord");
        DonationRecord donationRecord = drLogic.createEntity(request.getParameterMap());

        try {
            donationRecord.setBloodDonation(bloodDonation);
            donationRecord.setPerson(person);
            drLogic.add(donationRecord);

        } catch (Exception ex) {
            Logger.getLogger(CreateDonationRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (request.getParameter("add") != null) {
            processRequest(request, response);

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
