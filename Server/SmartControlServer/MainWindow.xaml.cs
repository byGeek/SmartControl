using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Threading;
using System.Net;

namespace SmartControlServer
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private SocketServerHost serverHost;
        private Thread serverThread;
        public MainWindow()
        {
            InitializeComponent();
            serverHost = new SocketServerHost();
            txtIP.Text = GetLocalIpAddress();
            this.Closed += MainWindow_Closed;
        }

        private void MainWindow_Closed(object sender, EventArgs e)
        {
            try
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
            catch { }
        }

        private void chkServerOn_Checked(object sender, RoutedEventArgs e)
        {
            if(chkServerOn.IsChecked.Value)
            {
                try
                {
                    serverThread = new Thread(serverHost.StartListening);
                    serverThread.IsBackground = true;
                    serverThread.Start();
                }
                catch(ThreadStartException ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
            else
            {
                if (serverHost != null)
                {
                    serverHost.StopListening();
                }

                if(serverThread != null && serverThread.IsAlive)
                {
                    try
                    {
                        //TODO user flag
                        serverThread.Abort();
                    }
                    catch(ThreadAbortException ex)
                    {
                        MessageBox.Show(ex.Message);
                    }
                }
            }
        }

        private string GetLocalIpAddress()
        {
            if(!System.Net.NetworkInformation.NetworkInterface.GetIsNetworkAvailable())
            {
                return "Network unavailabel";
            }
            IPHostEntry entry = Dns.GetHostEntry(Dns.GetHostName());

            var ip = entry.AddressList.FirstOrDefault(x => x.AddressFamily == System.Net.Sockets.AddressFamily.InterNetwork);
            if(ip == null)
            {
                return "Can not get ip address";
            }
            else
            {
                return ip.ToString();
            }
        }
    }
}
