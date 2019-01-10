        package eaglesfe.common;

        import org.firstinspires.ftc.robotcore.external.Telemetry;
        import org.java_websocket.WebSocket;
        import org.java_websocket.handshake.ClientHandshake;
        import org.java_websocket.server.WebSocketServer;
        import org.json.JSONObject;

        import java.net.InetSocketAddress;
        import java.net.UnknownHostException;
        import java.util.Locale;

public class BirdseyeServer extends WebSocketServer{

    private Telemetry opModeTelemetry;

    private void postToTelemetry(String message) {
        opModeTelemetry.addData("BIRDSEYE", message);
        opModeTelemetry.update();
    }

    private BirdseyeServer(int port, Telemetry telemetry) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
        opModeTelemetry = telemetry;
    }

    public static BirdseyeServer GetInstance(int port, Telemetry telemetry) {
        BirdseyeServer server = null;
        try {
            server = new BirdseyeServer(port, telemetry);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return server;
    }

    public void broadcast(JSONObject telemetry) {
        this.broadcast(telemetry.toString());
    }

    public String incoming = "";

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("BIRDSEYE VIEW ESTABLISHED");
        postToTelemetry("BIRDSEYE VIEW ESTABLISHED!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        this.incoming = message;
        postToTelemetry(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        postToTelemetry("Server encountered an error. " + ex.getMessage());
    }


    @Override
    public void onStart() {
        postToTelemetry(String.format(Locale.US, "The eagle has perched on port %d!", this.getPort()));
        opModeTelemetry.update();
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
}