package practicum.hit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "app", nullable = false, length = 128)
    @NotBlank(message = "app не может быть пустым")
    private String app;

    @Column(name = "uri", nullable = false, length = 256)
    @NotBlank(message = "uri не может быть пустым")
    private String uri;

    @Column(name = "ip", nullable = false, length = 64)
    @NotBlank(message = "IP не может быть пустым")
    private String ip;

    @Column(name = "hit_time", nullable = false)
    @NotNull(message = "Timestamp не может быть пустым")
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}