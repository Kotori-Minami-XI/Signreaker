package com.kotori;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import com.sun.jna.Native;

import java.io.File;

public class Cms extends AbstractJni {
    private final AndroidEmulator emulator;
    private final VM vm;
    private final Module module;

    private final DvmClass Cms;

    public Cms() {
        emulator = AndroidEmulatorBuilder.for32Bit().setProcessName("com.qidian.dldl.official").build(); // 创建模拟器实例，要模拟32位或者64位，在这里区分
        final Memory memory = emulator.getMemory(); // 模拟器的内存操作接口
        memory.setLibraryResolver(new AndroidResolver(23)); // 设置系统类库解析

        vm = emulator.createDalvikVM(null); // 创建Android虚拟机
        vm.setVerbose(true); // 设置是否打印Jni调用细节
        vm.setJni(this);

        DalvikModule dm = vm.loadLibrary(new File("unidbg-android/src/test/resources/example_binaries/libcms.so"), false); // 加载libttEncrypt.so到unicorn虚拟内存，加载成功以后会默认调用init_array等函数
        dm.callJNI_OnLoad(emulator); // 手动执行JNI_OnLoad函数
        module = dm.getModule(); // 加载好的libttEncrypt.so对应为一个模块

        Cms = vm.resolveClass("com/ss/sys/ces/a");

        byte[] data = new byte[16];
        int time = (int) (System.currentTimeMillis() / 1000);

        DvmObject<?> dvmObject = Cms.callStaticJniMethodObject(emulator, "leviathan(II[B)[B", -1, time, new ByteArray(vm, data));// 执行Jni方法
        System.out.println(dvmObject == null);
        //System.out.println(dvmObject.getValue());
    }

    public static void main(String[] args) {
        Cms cms = new Cms();
    }
}
