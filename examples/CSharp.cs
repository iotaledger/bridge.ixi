using System;
using Google.Protobuf;
using System.Net.Sockets;
using System.Threading;

class CSharp
{

    const int BRIDGE_PORT = 7331;
    const string BRIDGE_IP = "127.0.0.1";
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
                    Tag = "BRIDGE9TEST"
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

        Thread.Sleep(3000);

        // READ THE RESPONSE
        WrapperMessage wrapper = ReadMessage();

        foreach (var transaction in wrapper.FindTransactionsByAddressResponse.Transaction)
            Console.WriteLine(transaction.Address + " " + transaction.Tag);

        stream.Close();
        Console.ReadKey();

    }

    static byte[] IntegerToByteArray(int intValue)
    {
        byte[] intBytes = BitConverter.GetBytes(intValue);
        if (BitConverter.IsLittleEndian) // Big-Endian byte order required
            Array.Reverse(intBytes);
        return intBytes;
    }

    static void SendMessage(WrapperMessage wrapperMessage)
    {
        byte[] buffer = wrapperMessage.ToByteArray();
        int bufferLength = buffer.Length;
        // send buffer length first to the server so it knows how big the message will be
        byte[] integerBytes = IntegerToByteArray(bufferLength);
        stream.Write(integerBytes, 0, 4);
        // send buffer to server
        stream.Write(buffer, 0, buffer.Length);
    }

    static WrapperMessage ReadMessage()
    {
        byte[] integerBytes = new byte[4];
        stream.Read(integerBytes, 0, 4);
        if (BitConverter.IsLittleEndian) // Big-Endian byte order required
            Array.Reverse(integerBytes);
        int bufferLength = BitConverter.ToInt32(integerBytes, 0);
        byte[] buffer = new byte[bufferLength];
        stream.Read(buffer, 0, bufferLength);
        return WrapperMessage.Parser.ParseFrom(buffer);
    }

}
