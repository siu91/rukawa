package org.siu.rukawa.datasource;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpSession;

/**
 * @Author Siu
 * @Date 2020/3/8 15:16
 * @Version 0.0.1
 */
public class CoreTest extends AbstractMockMvcTest {

    /**
     * -- ----------------------------
     * -- Table structure for users
     * -- ----------------------------
     * DROP TABLE IF EXISTS "public"."users";
     * CREATE TABLE "public"."users" (
     * "id" int8 NOT NULL DEFAULT nextval('users_id_seq'::regclass),
     * "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
     * "age" int4,
     * "pass" varchar(255) COLLATE "pg_catalog"."default" NOT NULL
     * )
     * ;
     * <p>
     * -- ----------------------------
     * -- Primary Key structure for table users
     * -- ----------------------------
     * ALTER TABLE "public"."users" ADD CONSTRAINT "users_pkey" PRIMARY KEY ("id");
     *
     * @throws Exception
     */


    @Test
    public void testHeader() throws Exception {
        String mvcResult = this.getMockMvc().perform(MockMvcRequestBuilders.get("/v1/api/get_ds_from_header")
                .contentType(MediaType.APPLICATION_JSON)
                .header("ds", "secondary")
                .accept(MediaType.APPLICATION_JSON))/*.andDo(print())*/
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("success")))
                .andReturn().getResponse().getContentAsString();
        System.out.println("Result:\n" + mvcResult);

    }


    @Test
    public void testSession() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("ds", "secondary");
        String mvcResult = this.getMockMvc().perform(MockMvcRequestBuilders.get("/v1/api/get_ds_from_session")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))/*.andDo(print())*/
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("success")))
                .andReturn().getResponse().getContentAsString();
        System.out.println("Result:\n" + mvcResult);

    }


    @Test
    public void testParam() throws Exception {
        String mvcResult = this.getMockMvc().perform(MockMvcRequestBuilders.get("/v1/api/get_ds_from_param?ds=secondary")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))/*.andDo(print())*/
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("success")))
                .andReturn().getResponse().getContentAsString();
        System.out.println("Result:\n" + mvcResult);

    }


}
