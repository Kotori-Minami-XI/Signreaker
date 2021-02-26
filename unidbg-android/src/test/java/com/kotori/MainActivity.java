package com.kotori;


import com.github.unidbg.*;
import com.github.unidbg.Module;
import com.github.unidbg.arm.HookStatus;
import com.github.unidbg.arm.context.Arm32RegisterContext;
import com.github.unidbg.arm.context.RegisterContext;
import com.github.unidbg.debugger.DebuggerType;
import com.github.unidbg.hook.HookContext;
import com.github.unidbg.hook.ReplaceCallback;
import com.github.unidbg.hook.hookzz.*;
import com.github.unidbg.hook.xhook.IxHook;
import com.github.unidbg.linux.android.AndroidARMEmulator;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.XHookImpl;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.utils.Inspector;

import java.io.File;
import java.io.IOException;


import java.io.File;

public class MainActivity {
    private final AndroidEmulator emulator;
    private final VM vm;
    private final Module module;
    private DvmClass cNative;

    public static void main(String[] args) {
        testTiktokJni();
    }

    public MainActivity(String soPath, String classPath) {
        emulator = AndroidEmulatorBuilder.for32Bit().setProcessName("com.Kotori.jni.official").build(); // 创建模拟器实例，要模拟32位或者64位，在这里区分
        final Memory memory = emulator.getMemory(); // 模拟器的内存操作接口
        memory.setLibraryResolver(new AndroidResolver(23)); // 设置系统类库解析

        vm = emulator.createDalvikVM(null); // 创建Android虚拟机
        vm.setVerbose(false); // 设置是否打印Jni调用细节
        DalvikModule dm = vm.loadLibrary(new File(soPath), false); // 加载libttEncrypt.so到unicorn虚拟内存，加载成功以后会默认调用init_array等函数
        dm.callJNI_OnLoad(emulator); // 手动执行JNI_OnLoad函数
        module = dm.getModule(); // 加载好的libttEncrypt.so对应为一个模块

        cNative = vm.resolveClass(classPath);
    }

    public static void testKotoriJni() {
        String soPath = "unidbg-android/src/test/resources/example_binaries/libkotori.so";
        String classPath = "com/kotori/KotoriJni";
        MainActivity mainActivity = new MainActivity(soPath, classPath);

        String methodSign = "sing()V";
        mainActivity.cNative.callStaticJniMethodObject(mainActivity.emulator, methodSign);
    }

    public static void testTiktokJni() {
//        String soPath = "unidbg-android/src/test/resources/example_binaries/libcms.so";
//        String classPath = "com/kotori/TiktokJni";
//        MainActivity mainActivity = new MainActivity(soPath, classPath);
//
//        int time = (int) (System.currentTimeMillis() / 1000);
//        byte[] data = "暂时随便写的,这里是url经过处理后的data".getBytes();

        //String methodSign = "leviathan(II[B)[B";
        //mainActivity.cNative.callStaticJniMethodObject(mainActivity.emulator, methodSign, -1, time, new ByteArray(mainActivity.vm,data));

        String soPath = "unidbg-android/src/test/resources/example_binaries/libcms.so";
        String classPath = "com/ss/sys/ces/a";
        MainActivity mainActivity = new MainActivity(soPath, classPath);

        String methodSign = "leviathan(II[B)[B";

        byte[] data = "data".getBytes();
        int time = (int) (System.currentTimeMillis() / 1000);
        DvmObject<?> dvmObject = mainActivity.cNative.callStaticJniMethodObject(mainActivity.emulator, methodSign, -1, time, new ByteArray(mainActivity.vm,data));
        System.out.println(dvmObject.getValue());
    }

}
