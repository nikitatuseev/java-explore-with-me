package practicum.stat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UriStat {
    private String app;
    private String uri;
    private Long hits;
}