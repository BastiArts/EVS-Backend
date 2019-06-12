package service;

import com.google.gson.Gson;
import javax.ws.rs.*;
import repository.Repository;
import entity.User;
import evs.ldapconnection.EVSColorizer;
import evs.ldapconnection.LdapAuthException;
import evs.ldapconnection.LdapException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import jwt.JwtBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.OutputStream;
import java.io.IOException;
import org.glassfish.jersey.media.multipart.*;

/**
 *
 * @author M. Fadljevic
 */
@Path("users")
public class UserService {

    Repository repo = Repository.getInstance();

    /**
     * Login Path with parameters to proof if the username and password is the
     * same as the username and password from the school
     *
     * @param username
     * @param password
     * @return
     * @throws evs.ldapconnection.LdapException
     * @throws evs.ldapconnection.LdapAuthException
     */
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    public String login(
            @QueryParam("user") String username,
            @QueryParam("pwd") String password) throws LdapException, LdapAuthException {

        System.out.println(EVSColorizer.RED + "Some data are incomming: " + username + EVSColorizer.reset());
        User endUser = repo.proofLogin(username, password);

        if (endUser != null) {
            Gson gsonObject = new Gson();

            //endUser.setPicturePath(username + "_PB.jpg");
            System.out.println("");
            System.out.println("This is user: " + endUser.getFirstname());
            repo.insertUser(endUser);
            String retString = gsonObject.toJson(endUser);
            System.out.println(retString);
            return retString;
        } else {
            return "{}";
        }
    }

    /**
     *
     * @return
     */
    @GET
    @Path("jwtTest")
    @Produces(MediaType.TEXT_PLAIN)
    public String jwtTesting() {
        return new JwtBuilder().create("Manuel");
    }

    /**
     * Just a short message for testing if the server is running
     * @return
     */
    @GET
    @Path("msg")
    @Produces(MediaType.TEXT_PLAIN)
    public String message() {
        return "Java SE Server powered by EVS GmbH!";
    }

    /**
     * Updates an existing user, if picturePath changes or something else
     * 
     * @param user
     * @return
     */
    @POST
    @Path("updateUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String setProfilePath(String user) {
        Gson gson = new Gson();
        User u = gson.fromJson(user, User.class);
        repo.updateUser(u);
        return gson.toJson(u);
    }
    
    /**
     * Photoupload from Herr Professor Lackinger
     * 
     * @param file
     * @return
     */
    @POST
    @Path("uploadimage/{username}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public void uploadImage(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData,
            @PathParam("username") String username) throws Exception {
        String UPLOAD_PATH = "uploads/avatar/";
        try {
            int read = 0;
            byte[] bytes = new byte[1024];

            if (fileMetaData.getName().endsWith(".jpg")) {
                UPLOAD_PATH += username + ".jpg";

            } else if (fileMetaData.getName().endsWith(".png")) {
                UPLOAD_PATH += username + ".png";
            }
            repo.updateUser(username, UPLOAD_PATH);
            OutputStream out = new FileOutputStream(new File(UPLOAD_PATH));
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new Exception("Error while uploading file. Please try again !!");
        }
    }
}
