package common.network;

import java.io.Serializable;

/**
 * Gói tin phản hồi do Server gửi về Client.
 *
 * - {@code success}: true nếu thao tác thành công.
 * - {@code message}: thông báo mô tả kết quả (thành công / mô tả lỗi).
 * - {@code data}   : dữ liệu trả về (DTO, List, Map, primitive boxed…).
 *                    Có thể là null nếu chỉ cần biết success/message.
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String  message;
    private Object  data;

    public Response() {}

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data    = data;
    }

    // ---- Factory helpers ----

    /** Tạo response thành công với dữ liệu. */
    public static Response ok(Object data) {
        return new Response(true, "OK", data);
    }

    /** Tạo response thành công với thông điệp tuỳ chỉnh. */
    public static Response ok(String message, Object data) {
        return new Response(true, message, data);
    }

    /** Tạo response thành công không kèm dữ liệu. */
    public static Response ok(String message) {
        return new Response(true, message, null);
    }

    /** Tạo response thất bại. */
    public static Response fail(String message) {
        return new Response(false, message, null);
    }

    // ---- Getters / Setters ----

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    @Override
    public String toString() {
        return "Response{success=" + success + ", message='" + message + "', data=" + data + '}';
    }
}
