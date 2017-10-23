﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.InteropServices;
using System.Windows;

namespace SmartControlServer
{
    class MessageHelper
    {
        [DllImport("User32.dll")]
        private static extern int RegisterWindowMessage(string lpString);

        [DllImport("User32.dll", EntryPoint = "FindWindow")]
        public static extern Int32 FindWindowIntHandle(String lpClassName, String lpWindowName);

        [DllImport("user32.dll", SetLastError = true, EntryPoint = "FindWindow")]
        static extern IntPtr FindWindowPtr(string lpClassName, string lpWindowName);

        [DllImport("user32.dll", SetLastError = true, EntryPoint = "FindWindowEx")]
        public static extern IntPtr FindWindowEx(IntPtr hwndParent, IntPtr hwndChildAfter, string lpszClass, string lpszWindow);

        [DllImport("user32.dll", CharSet = CharSet.Auto, EntryPoint = "SendMessage")]
        public static extern IntPtr SendMessage2(IntPtr hWnd, UInt32 Msg, Int32 wParam, ref Point lParam);

        //For use with WM_COPYDATA and COPYDATASTRUCT
        [DllImport("User32.dll", EntryPoint = "SendMessage")]
        public static extern int SendMessage(int hWnd, int Msg, int wParam, ref COPYDATASTRUCT lParam);

        //For use with WM_COPYDATA and COPYDATASTRUCT
        [DllImport("User32.dll", EntryPoint = "PostMessage")]
        public static extern int PostMessage(int hWnd, int Msg, int wParam, ref COPYDATASTRUCT lParam);

        [DllImport("User32.dll", EntryPoint = "SendMessage")]
        public static extern int SendMessage(int hWnd, int Msg, int wParam, int lParam);

        [DllImport("User32.dll", EntryPoint = "PostMessage")]
        public static extern int PostMessage(int hWnd, int Msg, int wParam, int lParam);

        [DllImport("User32.dll", EntryPoint = "SetForegroundWindow")]
        public static extern bool SetForegroundWindow(int hWnd);



        public const int WM_USER = 0x400;
        public const int WM_COPYDATA = 0x4A;
        public const int WM_MOUSEWHEEL = 0x020A;
        public const int WHEEL_DELTA = 120;

        //Used for WM_COPYDATA for string messages
        public struct COPYDATASTRUCT
        {
            public IntPtr dwData;
            public int cbData;
            [MarshalAs(UnmanagedType.LPStr)]
            public string lpData;
        }

        public bool bringAppToFront(int hWnd)
        {
            return SetForegroundWindow(hWnd);
        }

        public int sendWindowsStringMessage(int hWnd, int wParam, string msg)
        {
            int result = 0;

            if (hWnd > 0)
            {
                byte[] sarr = System.Text.Encoding.Default.GetBytes(msg);
                int len = sarr.Length;
                COPYDATASTRUCT cds;
                cds.dwData = (IntPtr)100;
                cds.lpData = msg;
                cds.cbData = len + 1;
                result = SendMessage(hWnd, WM_COPYDATA, wParam, ref cds);
            }

            return result;
        }

        public int sendWindowsMessage(int hWnd, int Msg, int wParam, int lParam)
        {
            int result = 0;

            if (hWnd > 0)
            {
                result = SendMessage(hWnd, Msg, wParam, lParam);
            }

            return result;
        }

        public IntPtr sendWindowsMessage(IntPtr hWnd, UInt32 Msg, Int32 wParam, ref Point lParam)
        {
            return SendMessage2(hWnd, Msg, wParam, ref lParam);
        }

        public int getWindowId(string className, string windowName)
        {
            return FindWindowIntHandle(className, windowName);
        }

        public IntPtr getWindowPtr(string className, string windowName)
        {
            return FindWindowPtr(className, windowName);
        }

        public IntPtr getChildObjPtr(IntPtr parentWnd, IntPtr hwndChildAfter, string lpszClass, string lpszWindow)
        {
            return FindWindowEx(parentWnd, IntPtr.Zero, lpszClass, lpszWindow);
        }

    }
}
