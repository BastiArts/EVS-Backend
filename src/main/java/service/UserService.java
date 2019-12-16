package service;

import com.google.gson.Gson;
import entity.Equipment;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.*;
import org.json.JSONObject;

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
            // Wenn user in der DB schon vorhanden
            if (repo.findUser(endUser.getUsername()) == null) {
                if (!endUser.isStudent()) {
                    endUser.setEmail(endUser.getFirstname().charAt(0) + "." + endUser.getLastname() + "@htl-leonding.ac.at");
                    endUser.setEmail(endUser.getEmail().toLowerCase());
                }
                repo.insertUser(endUser);
                String retString = gsonObject.toJson(endUser);
                System.out.println(retString);
                return retString;
            } else {
                return gsonObject.toJson(repo.findUser(endUser.getUsername()));
            }
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
     *
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

    @Context
    private UriInfo context;
    private static final String UPLOAD_FOLDER = "uploads/avatar/";

    /**
     * Returns text response to caller containing uploaded file location
     *
     * @return error response in case of missing parameters an internal
     * exception or success response if file has been stored successfully
     */
    @POST
    @Path("uploadimage")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("userid") String userid) {
        // check if all form parameters are provided
        JSONObject json = new JSONObject();

        if (uploadedInputStream == null || fileDetail == null || userid == null || userid == "") {
            System.out.println("");
            System.out.println("");
            System.out.println(EVSColorizer.cyan()
                    + "Formdata Parameter not correct"
                    + EVSColorizer.reset());
            json.append("status", "failed");
            json.append("exception", "formdataparam not correct");
            return new Gson().toJson(json);
        }
        // create our destination folder, if it not exists
        try {
            createFolderIfNotExists(UPLOAD_FOLDER);
        } catch (SecurityException se) {
            System.out.println("");
            System.out.println("");
            System.out.println(EVSColorizer.cyan()
                    + "Destination Folder could not be created"
                    + EVSColorizer.reset());
            json.append("status", "failed");
            json.append("exception", "cannot create destinationfolder on vm");
            return new Gson().toJson(json);
        }
        String type = fileDetail.getType();
        String uploadedFileLocation = UPLOAD_FOLDER + userid  + "_" + fileDetail.getFileName();
        System.out.println(EVSColorizer.cyan()
                + fileDetail.getType()
                + EVSColorizer.reset());
        try {
            saveToFile(uploadedInputStream, uploadedFileLocation);
            repo.updateUser(userid, uploadedFileLocation);
        } catch (IOException e) {
            System.out.println("");
            System.out.println("");
            System.out.println(EVSColorizer.cyan()
                    + "File could not be safed (maybe because of same filename)"
                    + EVSColorizer.reset());
            json.append("status", "failed");
            json.append("exception", "could not save file");
            return new Gson().toJson(json);
        }
        json.append("status", "success");
        json.append("filename", fileDetail.getFileName());
        return new Gson().toJson(json);
    }

    /**
     * Utility method to save InputStream data to target location/file
     *
     * @param inStream - InputStream to be saved
     * @param target - full path to destination file
     */
    private void saveToFile(InputStream inStream, String target)
            throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        out = new FileOutputStream(new File(target));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }

    /**
     * Creates a folder to desired location if it not already exists
     *
     * @param dirName - full path to the folder
     * @throws SecurityException - in case you don't have permission to create
     * the folder
     */
    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }
}
