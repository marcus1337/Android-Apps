package MAIN;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Main {


	public static void main(String args[]) throws Exception {

		Client client = new Client();

		client.start();
		
	}
	

	public static void setByteData(byte[] data, int begin, int end, byte value) {
		for (int i = begin; i <= end; i++) {
			int nowByte = i / 8;
			int nowBit = i - nowByte * 8;

			if (getBit((end - i), value) == 1)
				data[nowByte] = (byte) (data[nowByte] | (1 << nowBit));
			else
				data[nowByte] = (byte) (data[nowByte] & ~(1 << nowBit));
		}

	}

	public static int getByteData(byte[] data, int begin, int end) {
		int result = 0;
		for (int i = begin; i <= end; i++) {
			int nowByte = i / 8;
			int nowBit = i - nowByte * 8;
			int powBy = (end - i);

			if (getBit(nowBit, data[nowByte]) != 0)
				result += Math.pow(2, powBy);
		}
		return result;
	}

	public static int getBit(int position, byte ID)
	{
	   return (byte) ((ID >> position) & 1);
	}
	
}
