#include "com_andev_mediademo_learnjni_JniTest.h"
#include "./common/common.h"

JNIEXPORT jstring JNICALL Java_com_andev_mediademo_learnjni_JniTest_getStringFromC
(JNIEnv * env, jclass obj){
   // return env->NewStringUTF("String From JniTest");
   //set_av_log_level(AV_LOG_DEBUG);

   av_log(NULL, AV_LOG_INFO, "hello world");


   return env->NewStringUTF(av_version_info());
}

JNIEXPORT jint JNICALL Java_com_andev_mediademo_learnjni_JniTest_testArray
  (JNIEnv * env, jobject obj, jintArray jarray){

  	jint* arrayData = env->GetIntArrayElements(jarray, 0);

  	arrayData[0] = 3;
  	arrayData[1] = 6;

    LOGD("start print array");
  	LOGD("########## arrayData[0] = %d", arrayData[0]);
  	LOGD("########## arrayData[1] = %d", arrayData[1]);

  	env->ReleaseIntArrayElements(jarray, arrayData, 0);

}