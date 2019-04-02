using System;
using Google.Protobuf;
using System.Net.Sockets;
using System.Threading;

class CSharp
{

    const string BRIDGE_IP = "127.0.0.1";
    const int BRIDGE_PORT = 7331;
    static NetworkStream stream;

    static void Main(string[] args)
    {

        // CONNECT TO BRIDGE SOCKET
        TcpClient client = new TcpClient(BRIDGE_IP, BRIDGE_PORT);
        stream = client.GetStream();

        // SUBMIT A NEW TRANSACTION
        WrapperMessage submitTransactionBuilderRequestWrapper = new WrapperMessage
        {
            MessageType = WrapperMessage.Types.MessageType.SubmitTransactionBuilderRequest,
            SubmitTransactionBuilderRequest = new SubmitTransactionBuilderRequest
            {
                TransactionBuilder = new TransactionBuilder
                {
                    Address = "TEST9ADDRESS999999999999999999999999999999999999999999999999999999999999999999999",
                    Tag = "BRIDGE9TEST9999999999999999"
                }
            }
        };

        SendMessage(submitTransactionBuilderRequestWrapper);

        // REQUEST THE TRANSACTION
        WrapperMessage findTransactionsByAddressRequestWrapper = new WrapperMessage
        {
            MessageType = WrapperMessage.Types.MessageType.FindTransactionsByAddressRequest,
            FindTransactionsByAddressRequest = new FindTransactionsByAddressRequest
            {
                Address = "TEST9ADDRESS999999999999999999999999999999999999999999999999999999999999999999999"
            }
        };

        SendMessage(findTransactionsByAddressRequestWrapper);

        Thread.Sleep(1500);

        // READ THE RESPONSE
        WrapperMessage wrapper = ReadMessage();

        foreach (var transaction in wrapper.FindTransactionsByAddressResponse.Transaction)
            Console.WriteLine(transaction.Address + " " + transaction.Tag);

        stream.Close();
        Console.ReadKey();

    }

    static void SendMessage(WrapperMessage wrapperMessage)
    {
        byte[] buffer = wrapperMessage.ToByteArray();
        int bufferLength = buffer.Length;
        // send bufferLength first to the server so it knows how big the message will be
        stream.Write(IntegerToByteArray(bufferLength), 0, 4);
        // send buffer to server
        stream.Write(buffer, 0, buffer.Length);
    }

    static WrapperMessage ReadMessage()
    {
        // read bufferLength from the stream
        byte[] integerBytes = ReadFully(4);
        if (BitConverter.IsLittleEndian) // Big-Endian byte order required
            Array.Reverse(integerBytes);
        int bufferLength = BitConverter.ToInt32(integerBytes, 0);
        // read buffer from the stream
        byte[] buffer = ReadFully(bufferLength);
        return WrapperMessage.Parser.ParseFrom(buffer);
    }

    /**
    * Stream.Read() does not guarantee that the whole message will be read in.
    * Because of this, it's strongly recommended to use a method like ReadFully() which reads the socket stream until all relevant data has been received.
    */
    public static byte[] ReadFully(int len)
    {
        byte[] b = new byte[len];
        int n = 0;
        while (n < len)
        {
            int count = stream.Read(b, n, len - n);
            if (count < 0)
                throw new Exception("EOF");
            n += count;
        }
        return b;
    }

    static byte[] IntegerToByteArray(int intValue)
    {
        byte[] intBytes = BitConverter.GetBytes(intValue);
        if (BitConverter.IsLittleEndian) // Big-Endian byte order required
            Array.Reverse(intBytes);
        return intBytes;
    }

}
