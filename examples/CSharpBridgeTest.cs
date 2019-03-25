using System;
using Google.Protobuf;
using System.Net.Sockets;

namespace MyIxiModule
{
    class CSharpBridgeTest
    {

        const int BRIDGE_PORT = 7331;
        const string BRIDGE_IP = "127.0.0.1";

        static void Main(string[] args)
        {

            // CONNECT TO BRIDGE
            TcpClient client = new TcpClient(BRIDGE_IP, BRIDGE_PORT);
            NetworkStream nwStream = client.GetStream();

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

            submitTransactionBuilderRequestWrapper.WriteDelimitedTo(nwStream);

            // REQUEST THE SUBMITTED TRANSACTION
            WrapperMessage findTransactionsByAddressRequestWrapper = new WrapperMessage
            {
                MessageType = WrapperMessage.Types.MessageType.FindTransactionsByAddressRequest,
                FindTransactionsByAddressRequest = new FindTransactionsByAddressRequest
                {
                    Address = "TEST9ADDRESS999999999999999999999999999999999999999999999999999999999999999999999"
                }
            };

            findTransactionsByAddressRequestWrapper.WriteDelimitedTo(nwStream);

            // READ THE RESPONSE
            WrapperMessage wrapper = new WrapperMessage();
            wrapper.MergeDelimitedFrom(nwStream);

            foreach (var transaction in wrapper.FindTransactionsByAddressResponse.Transaction)
            {
                Console.WriteLine(transaction.Address + " " + transaction.Tag);
            }

            Console.ReadKey();

        }
    }
}