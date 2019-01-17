import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/user/*")
public class ServletUser extends HttpServlet {


    protected void doGet(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {


    }

    protected void doPost(HttpServletResponse response,  HttpServletRequest request) throws ServletException, IOException {

    }
}
