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
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;
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
        MainActivity mainActivity = new MainActivity();
    }

    public MainActivity() {
        emulator = AndroidEmulatorBuilder.for32Bit().setProcessName("com.qidian.dldl.official").build(); // 创建模拟器实例，要模拟32位或者64位，在这里区分
        final Memory memory = emulator.getMemory(); // 模拟器的内存操作接口
        memory.setLibraryResolver(new AndroidResolver(23)); // 设置系统类库解析

        vm = emulator.createDalvikVM(null); // 创建Android虚拟机
        vm.setVerbose(false); // 设置是否打印Jni调用细节
        DalvikModule dm = vm.loadLibrary(new File("unidbg-android/src/test/resources/example_binaries/libcms.so"), false); // 加载libttEncrypt.so到unicorn虚拟内存，加载成功以后会默认调用init_array等函数
        dm.callJNI_OnLoad(emulator); // 手动执行JNI_OnLoad函数
        module = dm.getModule(); // 加载好的libttEncrypt.so对应为一个模块

        cNative = vm.resolveClass("com/kotori/Tiktok");

        int time = (int) (System.currentTimeMillis() / 1000);
        byte[] data = "暂时随便写的,这里是url经过处理后的data".getBytes();
        String methodSign = "leviathan(II[B)[B";

        cNative.callStaticJniMethodObject(emulator, methodSign, -1, time, new ByteArray(vm,data));



    }


}
