package otus.highload.homework.util;

import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class Resources {
    @NonNull
    private final String path;

    public Resources(@NonNull String path) {
        this.path = path.replaceAll("/+$", "") + "/";
    }

    @NonNull
    public byte[] loadAsBytes(@NonNull String resource) throws IOException {
        try (var in = resourceAsStream(resource)) {
            return StreamUtils.copyToByteArray(in);
        }
    }

    @NonNull
    public String loadAsString(@NonNull String resource, @NonNull Charset charset) throws IOException {
        try (var in = resourceAsStream(resource)) {
            return StreamUtils.copyToString(in, charset);
        }
    }

    public void process(@NonNull String resource, @NonNull Consumer<InputStream> consumer) throws IOException {
        try (var in = resourceAsStream(resource)) {
            consumer.accept(in);
        }
    }

    public <R> R process(@NonNull String resource, @NonNull Function<InputStream, R> consumer) throws IOException {
        try (var in = resourceAsStream(resource)) {
            return consumer.apply(in);
        }
    }

    @NonNull
    private InputStream resourceAsStream(@NonNull String resource) {
        var resourcePath = path + resource;
        var stream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        return Objects.requireNonNull(stream);
    }
}
