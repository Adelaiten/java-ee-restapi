package servletHelpers;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class ServletHelper {

    public String parseRequest(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String formData = br.readLine();
        System.out.println(formData);
        return formData;
    }
}
