package be.bstorm.akimts.rest.bxl.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ErrorDTO {

    private LocalDateTime receivedAt;
    private HttpMethod method;
    private String path;
    private String message;
    private int status;
    private Map<String, Object> infos;
    public ErrorDTO(LocalDateTime receivedAt, HttpMethod method, String path, String message, int status, Map<String, Object> infos) {
        this.receivedAt = receivedAt;
        this.method = method;
        this.path = path;
        this.message = message;
        this.status = status;
        this.infos = infos == null ? new HashMap<>() : infos;
    }

    public ErrorDTO addInfo(String key, Object value ){
        infos.put(key, value);
        return this;
    }

}
