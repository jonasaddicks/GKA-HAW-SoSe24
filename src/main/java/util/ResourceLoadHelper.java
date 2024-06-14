package util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class ResourceLoadHelper {

    public static URI loadResource(String name) throws URISyntaxException, NullPointerException {
        return Objects.requireNonNull(ResourceLoadHelper.class.getResource(name), String.format("resource with name '%s' not found", name)).toURI();
    }
}
