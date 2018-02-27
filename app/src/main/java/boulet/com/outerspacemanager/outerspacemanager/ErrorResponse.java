package boulet.com.outerspacemanager.outerspacemanager;

/**
 * Created by aboulet on 27/02/2018.
 */

public class ErrorResponse {
    protected String code;
    protected String internalCode;
    protected String status;
    protected String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse(String code, String internalCode, String status, String message) {
        this.code = code;
        this.internalCode = internalCode;
        this.status = status;
        this.message = message;
    }
}