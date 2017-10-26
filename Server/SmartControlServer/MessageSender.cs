using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Diagnostics;
using WindowsInput;
using WindowsInput.Native;


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
#if DEBUG
            //send to notepad
            IntPtr hwnd = _msgHelper.getWindowPtr(null, "smart_control_demo.txt - Notepad");
            IntPtr objWnd = _msgHelper.getChildObjPtr(hwnd, IntPtr.Zero, "Edit", null);
            Point p = new Point(0, 0);

            _msgHelper.sendWindowsMessage(objWnd, MessageHelper.WM_MOUSEWHEEL,
                (MessageHelper.WHEEL_DELTA * pos) << 16, ref p);

#else
            var sim = new InputSimulator();
            sim.Mouse.VerticalScroll(pos);
#endif


        }
    }
}
