using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using Forms = System.Windows.Forms;

namespace SmartControlServer
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        private System.Windows.Forms.NotifyIcon _notifyIcon;
        private Controller _serverController;

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            if(IsProcessOpen())
            {
                MessageBox.Show("There is already a Server opened");
                return;
            }

            AppDomain.CurrentDomain.UnhandledException += CurrentDomain_UnhandledException;

            _serverController = new Controller();

            CreateContextMenu();

        }

        void CurrentDomain_UnhandledException(object sender, UnhandledExceptionEventArgs e)
        {
            var exception = (Exception)e.ExceptionObject;
            System.Diagnostics.Debug.WriteLine(exception.Message);
            System.Diagnostics.Debug.WriteLine(exception.StackTrace);

            MessageBox.Show("Exception occurred! Application will be closed");
        }

        private Forms.ToolStripMenuItem _startMenuItem;
        private Forms.ToolStripMenuItem _stopMenuItem;
        private Forms.ToolStripMenuItem _exitMenuItem;

        private void CreateContextMenu()
        {
            if(_notifyIcon == null)
            {
                _notifyIcon = new System.Windows.Forms.NotifyIcon();
                _notifyIcon.ContextMenuStrip = new System.Windows.Forms.ContextMenuStrip();

                _startMenuItem = new Forms.ToolStripMenuItem("Start Server");
                _startMenuItem.Click += _startMenuItem_Click;
                _notifyIcon.ContextMenuStrip.Items.Add(_startMenuItem);

                _stopMenuItem = new Forms.ToolStripMenuItem("Stop Server");
                _stopMenuItem.Click += _stopMenuItem_Click;
                _notifyIcon.ContextMenuStrip.Items.Add(_stopMenuItem);

                var seperator = new Forms.ToolStripSeparator();
                _notifyIcon.ContextMenuStrip.Items.Add(seperator);

                _exitMenuItem = new Forms.ToolStripMenuItem("Exit");
                _exitMenuItem.Click += _exitMenuItem_Click;
                _notifyIcon.ContextMenuStrip.Items.Add(_exitMenuItem);

                _notifyIcon.ContextMenuStrip.Opening += ContextMenuStrip_Opening;
                _notifyIcon.Icon = SmartControlServer.Properties.Resources.trayIcon;
                _notifyIcon.Visible = true;

                var text = "Server IP: " + _serverController.IP;
                _notifyIcon.Text = text;
                _notifyIcon.ShowBalloonTip(3 * 1000, "Info", text, Forms.ToolTipIcon.Info);
            }
        }

        void ContextMenuStrip_Opening(object sender, System.ComponentModel.CancelEventArgs e)
        {
            SetMenuItem();
        }

        void _exitMenuItem_Click(object sender, EventArgs e)
        {
            _serverController.Dispose();
            Application.Current.Shutdown();
        }

        void _stopMenuItem_Click(object sender, EventArgs e)
        {
            _serverController.StopServer();
            _notifyIcon.ShowBalloonTip(2 * 1000, "Info", "Server stopped", Forms.ToolTipIcon.Info);
        }

        void _startMenuItem_Click(object sender, EventArgs e)
        {
            _serverController.StartServer();
            _notifyIcon.ShowBalloonTip(2 * 1000, "Info", "Server started", Forms.ToolTipIcon.Info);
            
        }

        void SetMenuItem()
        {
            var status = _serverController.Status;
            switch (status)
            {
                case ServerStatus.Running:
                    _startMenuItem.Enabled = false;
                    _stopMenuItem.Enabled = true;
                    _exitMenuItem.Enabled = true;
                    break;
                case ServerStatus.Idle:
                    _startMenuItem.Enabled = true;
                    _stopMenuItem.Enabled = false;
                    _exitMenuItem.Enabled = true;
                    break;
                default:
                    break;
            }
        }

        bool IsProcessOpen()
        {
            return System.Diagnostics.Process.GetProcessesByName(
                System.Diagnostics.Process.GetCurrentProcess().ProcessName).Length > 1;
        }
    }

    
}
