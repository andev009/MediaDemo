#include <jni.h>
#include <android/log.h>
#include <cassert>
#include <cstdlib>
#include <iostream>
#include "./common/common.h"
#include "./libffmpeg_decoder/accompany_decoder.h"

#ifdef __cplusplus
extern "C" {
#endif

jstring get_FFmpeg_Version(JNIEnv * env, jclass obj){




   return env->NewStringUTF(av_version_info());
}


void ffmpeg_init(const char* accompanyPath, const char* pcmFilePath){
	AccompanyDecoder* tempDecoder = new AccompanyDecoder();
	int accompanyMetaData[2];
	tempDecoder->getMusicMeta(accompanyPath, accompanyMetaData);

    LOGD("start ffmpeg_init");
  	LOGD("########## accompanyMetaData[0] = %d", accompanyMetaData[0]);
  	LOGD("########## accompanyMetaData[1] = %d", accompanyMetaData[1]);
}


void ffmpeg_decode_mp3(JNIEnv * env, jclass obj, jstring mp3PathParam, jstring pcmPathParam){
	const char* mp3Path = env->GetStringUTFChars(mp3PathParam, NULL);
	const char* pcmPath = env->GetStringUTFChars(pcmPathParam, NULL);

	ffmpeg_init(mp3Path, pcmPath);
}

static JNINativeMethod getMethods[] = {
  {"getFFmpegVersion","()Ljava/lang/String;",(void*)get_FFmpeg_Version},
  {"decode","(Ljava/lang/String;Ljava/lang/String;)V",(void*)ffmpeg_decode_mp3},
};

static int registerNativeMethods(JNIEnv* env, const char* className,JNINativeMethod* getMethods,int methodsNum){
    jclass clazz;
    //找到声明native方法的类
    clazz = env->FindClass(className);
    if(clazz == NULL){
        return JNI_FALSE;
    }
   //注册函数 参数：java类 所要注册的函数数组 注册函数的个数
    if(env->RegisterNatives(clazz,getMethods,methodsNum) < 0){
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

static int registerNatives(JNIEnv* env){
    //指定类的路径，通过FindClass 方法来找到对应的类
    const char* className  = "com/andev/mediademo/ffmpegdecode/FFmpegDecodeMP3";
    return registerNativeMethods(env,className,getMethods, sizeof(getMethods)/ sizeof(getMethods[0]));
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved){

    LOGD("start jni load");
    JNIEnv* env = NULL;
   //获取JNIEnv
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);
    //注册函数 registerNatives ->registerNativeMethods ->env->RegisterNatives
    if(!registerNatives(env)){
        return -1;
    }
    //返回jni 的版本
    return JNI_VERSION_1_6;
}


#ifdef __cplusplus
}
#endif