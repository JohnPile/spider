package pile.spider.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SpiderSettingsTester {

    @Test
    public void testDefaults() {
        String args[] = new String[]{};
        SpiderSettings settings = new SpiderSettings(args, AllowedSite.TechCrunch, 5, 10);
        assertEquals(AllowedSite.TechCrunch, settings.getSite());
        assertEquals(5, settings.getMaxSearchPages());
        assertEquals(10, settings.getPoolSize());
    }

    @Test
    public void testValidOverrides() {
        String args[] = new String[]{"TechCrunch", "7", "12"};
        SpiderSettings settings = new SpiderSettings(args, AllowedSite.TechCrunch, 5, 10);
        assertEquals(AllowedSite.TechCrunch, settings.getSite());
        assertEquals(7, settings.getMaxSearchPages());
        assertEquals(12, settings.getPoolSize());
    }

    @Test
    public void testValidOverridesSubset() {
        String args[] = new String[]{"TechCrunch", "7"};
        SpiderSettings settings = new SpiderSettings(args, AllowedSite.TechCrunch, 5, 10);
        assertEquals(AllowedSite.TechCrunch, settings.getSite());
        assertEquals(7, settings.getMaxSearchPages());
        assertEquals(10, settings.getPoolSize());
    }

    @Test
    public void testInvalidSite() {
        String args[] = new String[]{"Fickle"};
        try {
            SpiderSettings settings = new SpiderSettings(args, AllowedSite.TechCrunch, 5, 10);
            fail("Should have failed this unregistered site");
        } catch (IllegalArgumentException ex) {
            assertEquals("No enum constant pile.spider.model.AllowedSite.Fickle", ex.getMessage());
        }
    }
}
