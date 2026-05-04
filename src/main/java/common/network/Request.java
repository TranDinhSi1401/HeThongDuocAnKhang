package common.network;

import java.io.Serializable;

/**
 * Gói tin yêu cầu do Client gửi lên Server.
 *
 * - {@code command}: loại hành động cần thực hiện (xem {@link CommandType}).
 * - {@code data}   : dữ liệu đính kèm (DTO hoặc kiểu nguyên thủy đã boxed).
 *                    Có thể là null nếu lệnh không cần tham số đầu vào.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private CommandType command;
    private Object data;

    public Request() {}

    public Request(CommandType command, Object data) {
        this.command = command;
        this.data    = data;
    }

    public CommandType getCommand() { return command; }
    public void setCommand(CommandType command) { this.command = command; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    @Override
    public String toString() {
        return "Request{command=" + command + ", data=" + data + '}';
    }
}
