using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;
using System.Net;

namespace SmartControlServer
{
    internal class Controller
    {
        private SocketServerHost serverHost;
        private Thread serverThread;
        private ServerStatus _status;
        public ServerStatus Status
        {
            get { return _status; }
        }

        private string _ip;
        public string IP
        {
            get { return _ip; }
        }

        public Controller()
        {
            serverHost = new SocketServerHost();
            _ip = GetLocalIpAddress();
        }
        public void StartServer()
        {
            serverThread = new Thread(serverHost.StartListening);
            serverThread.IsBackground = true;
            serverThread.Start();
            _status = ServerStatus.Running;
        }

        public void StopServer()
        {
            if (serverHost != null)
            {
                serverHost.StopListening();
            }

            if (serverThread != null && serverThread.IsAlive)
            {
                //TODO user flag
                serverThread.Abort();
            }
            _status = ServerStatus.Idle;
        }

        public void Dispose()
        {
            if (serverHost != null)
            {
                serverHost.Dispose();
            }
            if (serverThread != null)
            {
                serverThread.Abort();
            }
        }

        private string GetLocalIpAddress()
        {
            if (!System.Net.NetworkInformation.NetworkInterface.GetIsNetworkAvailable())
            {
                return "Network unavailabel";
            }
            IPHostEntry entry = Dns.GetHostEntry(Dns.GetHostName());

            var ip = entry.AddressList.FirstOrDefault(x => x.AddressFamily == System.Net.Sockets.AddressFamily.InterNetwork);
            if (ip == null)
            {
                return "Can not get ip address";
            }
            else
            {
                return ip.ToString();
            }
        }
    }

    internal enum ServerStatus
    {
        Idle,
        Running
    }
}
