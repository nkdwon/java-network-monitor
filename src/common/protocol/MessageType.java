package common.protocol;

public enum MessageType {
    REGISTER,
    REGISTER_OK,
    HEARTBEAT,
    STATUS,
    GET_MACHINE_LIST,
    GET_MACHINE_DETAILS,
    DISCONNECT,
    ERROR
}