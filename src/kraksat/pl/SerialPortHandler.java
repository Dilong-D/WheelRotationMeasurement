package kraksat.pl;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;

public class SerialPortHandler {

    private static SerialPort serialPort;
    private static OutputStream outputStream;

    public void initComunication() {
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) portList.nextElement();
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (port.getName().equals("COM4")) {
                    try {
                        serialPort = (SerialPort) port.open("Serial comunication between Arduino and Rotation Measurement App", 2000);
                    } catch (PortInUseException e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                    try {
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                    setSerialPort();

                }
            }
        }
    }

    public void sendThrowSerial(String message) {
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private void setSerialPort() {
        try {
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

}

