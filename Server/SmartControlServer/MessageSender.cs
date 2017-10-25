using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Diagnostics;

namespace SmartControlServer
{
    class MessageSender
    {
        public MessageSender()
        {
            _msgHelper = new MessageHelper();
        }

        private MessageHelper _msgHelper;

        public void SendMessage(string param)
        {
            Debug.WriteLine("SendMessage: "+param);
            if(param == CmdType.SCROLL_UP)
            {
                ScrollView(1);
            }
            else if(param == CmdType.SCROLL_DOWN)
            {
                ScrollView(-1);
            }
            
        }

        private void ScrollView(int pos)
        {
            //chrome receive the message but will not scroll
            //IntPtr hwnd = _msgHelper.getWindowPtr("Chrome_WidgetWin_1", "TestStack.White - Google Chrome");
            //IntPtr objWnd = _msgHelper.getChildObjPtr(IntPtr.Zero, IntPtr.Zero, "Chrome_WidgetWin_1", null);

            //Point p = new Point();
            //_msgHelper.sendWindowsMessage(objWnd, MessageHelper.WM_MOUSEWHEEL,
            //    (MessageHelper.WHEEL_DELTA * pos) << 16, ref p);

            //can not debug, because when you debug your app, the foregroundwindow will
            //always be visual studio
            //var hwnd = MessageHelper.GetForegroundWindow();
            //Point p = new Point(0, 0);
            //_msgHelper.sendWindowsMessage(hwnd, MessageHelper.WM_MOUSEWHEEL,
                //(MessageHelper.WHEEL_DELTA * pos) << 16, ref p);

            //send to notepad
            //TODO SET a ui parameter
            IntPtr hwnd = _msgHelper.getWindowPtr(null, "smart_control_demo.txt - Notepad");
            IntPtr objWnd = _msgHelper.getChildObjPtr(hwnd, IntPtr.Zero, "Edit", null);
            Point p = new Point(0, 0);

            _msgHelper.sendWindowsMessage(objWnd, MessageHelper.WM_MOUSEWHEEL,
                (MessageHelper.WHEEL_DELTA * pos) << 16, ref p);
        }
    }
}
