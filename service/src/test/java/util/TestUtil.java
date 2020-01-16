package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;

public final class TestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtil.class);

    private TestUtil() {
        throw new UnsupportedOperationException("Instantiating a utility class");
    }

    public static String readJsonFileToString(final String resourceFilePath)
    {
        final JsonParser parser = new JsonParser();
        try{
            final Object obj = parser.parse(new InputStreamReader(TestUtil.class.getClassLoader()
                    .getResourceAsStream(resourceFilePath)));
            final JsonObject jsonObject =  (JsonObject) obj;
            return jsonObject.toString();
        } catch (Exception e) {
            LOGGER.error("Could not read json file {} due to exception", resourceFilePath, e);
        }
        return null;
    }
}