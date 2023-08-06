package practicum;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UriStatDto {
    private String app;
    private String uri;
    private Long hits;
}
