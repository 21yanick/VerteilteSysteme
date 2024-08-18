package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import ch.hftm.entity.Comment;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BlogResourceTest {

    @Test
    public void testAddComment() {
        Comment comment = new Comment("Dies ist ein Testkommentar");

        given()
            .contentType("application/json")
            .body(comment)
            .when()
            .post("/blogs/1/comments")
            .then()
            .statusCode(201);
    }
}
