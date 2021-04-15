package view;

import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Jamie
 */

@WebServlet( name = "CreatePerson", urlPatterns = { "/CreatePerson" } )
public class CreatePerson extends HttpServlet {
    
    private String errorMessage = null;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException {
        response.setContentType( "text/html;charset=UTF-8" );
        try( PrintWriter out = response.getWriter() ) {
            out.println( "<!DOCTYPE html>" );
            out.println( "<html>" );
            out.println( "<head>" );
            out.println( "<title>Create Person</title>" );
            out.println( "</head>" );
            out.println( "<body>" );
            out.println( "<div style=\"text-align: center;\">" );
            out.println( "<div style=\"display: inline-block; text-align: left;\">" );
            out.println( "<form method=\"post\">" );
            out.println( "First Name:<br>" );
                
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.FIRST_NAME );
            out.println( "<br>" );
            out.println( "Last Name:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.LAST_NAME );
            out.println( "<br>" );
                
            out.println( "Phone Number:<br>" );
            out.printf( "<input type=\"tel\" name=\"%s\" placeholder=\"613-613-6131\"><br>", PersonLogic.PHONE );
            out.println( "<br>" );
                
            out.println( "Address:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.ADDRESS );
            out.println( "<br>" );
                
            out.println( "Birth Date:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" placeholder=\"yyyy-mm-dd hh:mm:ss\"><br>", PersonLogic.BIRTH );
            out.println( "<br>" );
                
            out.println( "<input type=\"submit\" name=\"view\" value=\"Add and View\">" );
            out.println( "<input type=\"submit\" name=\"add\" value=\"Add\">" );
            out.println( "</form>" );
                
            if( errorMessage != null && !errorMessage.isEmpty() ){
                out.println( "<p color=red>" );
                out.println( "<font color=red size=4px>" );
                out.println( errorMessage );
                out.println( "</font>" );
                out.println( "</p>" );
            }
            
            out.println( "<pre>" );
            out.println( "Submitted keys and values:" );
            out.println( toStringMap( request.getParameterMap() ) );
            out.println( "</pre>" );
            out.println( "</div>" );
            out.println( "</div>" );
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach( ( k, v ) -> builder.append( "Key=" ).append( k )
        .append( ", " )
        .append( "Value/s=" ).append( Arrays.toString( v ) )
        .append( System.lineSeparator() ) );
        return builder.toString();
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * get method is called first when requesting a URL. since this servlet will create a host this method simple
     * delivers the html code. creation will be done in doPost method.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "GET" );
        processRequest( request, response );
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * this method will handle the creation of entity. as it is called by user submitting data through browser.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "POST" );
        PersonLogic pLogic = LogicFactory.getFor( "Person" );
        
        String firstName = request.getParameter( PersonLogic.FIRST_NAME );
        String lastName = request.getParameter( PersonLogic.LAST_NAME );
        
        String fullName = firstName +" "+ lastName;
        
        //TODO: Fix this area. Definitely a better way of doing this.
        if( pLogic.getPersonWithFirstName( firstName ).isEmpty()) {
            try {
                Person person = pLogic.createEntity( request.getParameterMap() );
                pLogic.add( person );
                errorMessage = null;
            } catch( Exception ex ) {
                errorMessage = ex.getMessage();
            }
        } else {
            //if duplicate print the error message
            errorMessage = "Name: \"" + fullName + "\" already exists";
        }
        if( request.getParameter( "add" ) != null ){
            //if add button is pressed return the same page
            processRequest( request, response );
        } else if( request.getParameter( "view" ) != null ){
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect( "PersonTable" );
        }
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a Person Entity";
    }
    
    private static final boolean DEBUG = true;

    @Override
    public void log( String msg ) {
        if( DEBUG ){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log( message );
        }
    }
    
    @Override
    public void log( String msg, Throwable t ) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
        getServletContext().log( message, t );
    }
}
