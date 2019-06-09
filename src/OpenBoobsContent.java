import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OpenBoobsContent {
    private String url;
    private int rank;
    private ContentType type;
}
