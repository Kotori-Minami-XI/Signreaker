package com.kotori;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;

import java.io.File;

public class TtEncrypt {
    private final AndroidEmulator emulator;
    private final VM vm;
    private final Module module;

    private final DvmClass TTEncryptUtils;

    public TtEncrypt() {
        emulator = AndroidEmulatorBuilder.for32Bit().setProcessName("com.qidian.dldl.official").build(); // 创建模拟器实例，要模拟32位或者64位，在这里区分
        final Memory memory = emulator.getMemory(); // 模拟器的内存操作接口
        memory.setLibraryResolver(new AndroidResolver(23)); // 设置系统类库解析

        vm = emulator.createDalvikVM(null); // 创建Android虚拟机
        vm.setVerbose(true); // 设置是否打印Jni调用细节
        DalvikModule dm = vm.loadLibrary(new File("unidbg-android/src/test/resources/example_binaries/libttEncrypt.so"), false); // 加载libttEncrypt.so到unicorn虚拟内存，加载成功以后会默认调用init_array等函数
        dm.callJNI_OnLoad(emulator); // 手动执行JNI_OnLoad函数
        module = dm.getModule(); // 加载好的libttEncrypt.so对应为一个模块

        TTEncryptUtils = vm.resolveClass("com/bytedance/frameworks/core/encrypt/TTEncryptUtils");
        byte[] data = new byte[16];
        DvmObject<?> dvmObject = TTEncryptUtils.callStaticJniMethodObject(emulator, "ttEncrypt([BI)[B", new ByteArray(vm, data), 1);// 执行Jni方法
        System.out.println(dvmObject.getValue());
    }

    public static void main(String[] args) {
        TtEncrypt full = new TtEncrypt();
    }
}
