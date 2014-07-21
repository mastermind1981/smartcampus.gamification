package eu.trentorise.game.plugin.leaderboard.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.leaderboard.point.model.LeaderboardPointPlugin;
import eu.trentorise.game.plugin.leaderboard.point.request.LeaderboardPointPluginRequest;
import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class LeadearboardPointPluginControllerTest {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    public LeadearboardPointPluginControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setCustomizedGamificationPlugin method, of class LeaderboardPointPluginController.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetCustomizedGamificationPlugin() throws Exception {
        RestTemplateJsonServiceTestHelper<CustomizedGamificationPluginResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        MockGamePluginManager mock = new MockGamePluginManager();
        ObjectMapper mapper = new ObjectMapper();
        
        Game game = new Game();
        game.setId(135);
        
        LeaderboardPointPluginRequest request = new LeaderboardPointPluginRequest();
        request.setGame(game);
        LeaderboardPointPlugin plugin = mock.createGreenWeeklyLeadearboardPlugin();
        plugin.setId(null);
        request.setPlugin(plugin);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        CustomizedGamificationPluginResponse response = helper.executeTest("testLeaderboardPointSetCustomizedGamificationPlugin", 
                                                                           BASE_RELATIVE_URL + "/setCustomizedGamificationPlugin" + FINAL_PART_RELATIVE_URL,
                                                                           CustomizedGamificationPluginResponse.class, 
                                                                           jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            CustomizedGamificationPlugin customizedPlugin = response.getCustomizedGamificationPlugin();
            
            assertNotNull(customizedPlugin.getId());
            assertEquals(customizedPlugin.getGamificationPlugin().getId(), plugin.getGamificationPlugin().getId());
            assertEquals(customizedPlugin.getName(), plugin.getName());
            assertEquals(customizedPlugin.getVersion(), plugin.getVersion());
            assertEquals(customizedPlugin.getDescription(), plugin.getDescription());
        }
    }
}