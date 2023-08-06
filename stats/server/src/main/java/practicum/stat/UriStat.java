package practicum.stat;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UriStat {
    private String app;
    private String uri;
    private Long hits;
}