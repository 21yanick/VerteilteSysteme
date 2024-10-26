package ch.hftm;

import ch.hftm.dto.BlogDTO;
import ch.hftm.dto.CommentDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class BlogResourceTest {

    private static String authToken;

    @BeforeAll
    public static void getAuthToken() {
        // Hole den Access Token von Keycloak
        authToken = given()
            .param("client_id", "blog-client")
            .param("username", "adminuser")
            .param("password", "adminpassword")
            .param("grant_type", "password")
            .param("client_secret", "dn0uRMYmeN2ID2ieF7FC0nG8ixihDK2W")
            .when()
            .post("http://localhost:8080/realms/blog-backend/protocol/openid-connect/token")
            .then()
            .extract()
            .path("access_token");
    }

    @Test
    public void testCreateBlog() {
        BlogDTO newBlog = new BlogDTO();
        newBlog.setTitle("Test Blog");
        newBlog.setContent("This is a test content");

        given()
            .auth().oauth2(authToken)
            .contentType("application/json")
            .body(newBlog)
            .when()
            .post("/blogs")
            .then()
            .statusCode(201)
            .body("title", equalTo("Test Blog"))
            .body("content", equalTo("This is a test content"));
    }

    @Test
    public void testGetAllBlogs() {
        given()
            .auth().oauth2(authToken)
            .when().get("/blogs")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testAddCommentToBlog() {
        BlogDTO newBlog = new BlogDTO();
        newBlog.setTitle("Test Blog for Comments");
        newBlog.setContent("This is a test blog for comments");

        Integer blogId = given()
            .auth().oauth2(authToken)
            .contentType("application/json")
            .body(newBlog)
            .when()
            .post("/blogs")
            .then()
            .statusCode(201)
            .extract().path("id");

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText("This is a test comment");

        given()
            .auth().oauth2(authToken)
            .contentType("application/json")
            .body(commentDTO)
            .when()
            .post("/blogs/" + blogId + "/comments")
            .then()
            .statusCode(201);
    }

    @Test
    public void testAddCommentToNonExistingBlog() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText("This is a test comment");

        given()
            .auth().oauth2(authToken)
            .contentType("application/json")
            .body(commentDTO)
            .when()
            .post("/blogs/9999/comments")
            .then()
            .statusCode(404)
            .body("message", is("Blog mit ID 9999 nicht gefunden"));
    }

    @Test
    public void testPagination() {
        given()
            .auth().oauth2(authToken)
            .queryParam("page", 0)
            .queryParam("size", 2)
            .when()
            .get("/blogs")
            .then()
            .statusCode(200)
            .body("$.size()", is(2));
    }

    @Test
    public void testUpdateBlog() {
        BlogDTO newBlog = new BlogDTO();
        newBlog.setTitle("Original Title");
        newBlog.setContent("Original Content");

        Integer blogId = given()
            .auth().oauth2(authToken)
            .contentType("application/json")
            .body(newBlog)
            .when()
            .post("/blogs")
            .then()
            .statusCode(201)
            .extract().path("id");

        BlogDTO updatedBlog = new BlogDTO();
        updatedBlog.setTitle("Updated Title");
        updatedBlog.setContent("Updated Content");

        given()
            .auth().oauth2(authToken)
            .contentType("application/json")
            .body(updatedBlog)
            .when()
            .put("/blogs/" + blogId)
            .then()
            .statusCode(200)
            .body("title", is("Updated Title"))
            .body("content", is("Updated Content"));
    }

    @Test
    public void testDeleteBlog() {
        BlogDTO newBlog = new BlogDTO();
        newBlog.setTitle("Test Blog");
        newBlog.setContent("This is a test content");

        Integer blogId = given()
            .auth().oauth2(authToken)
            .contentType("application/json")
            .body(newBlog)
            .when()
            .post("/blogs")
            .then()
            .statusCode(201)
            .extract().path("id");

        given()
            .auth().oauth2(authToken)
            .when()
            .delete("/blogs/" + blogId)
            .then()
            .statusCode(204);

        given()
            .auth().oauth2(authToken)
            .when()
            .get("/blogs/" + blogId)
            .then()
            .statusCode(404)
            .body("message", is("Blog mit ID " + blogId + " nicht gefunden"));
    }
}