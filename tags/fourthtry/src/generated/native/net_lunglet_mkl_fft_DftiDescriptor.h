/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class net_lunglet_mkl_fft_DftiDescriptor */

#ifndef _Included_net_lunglet_mkl_fft_DftiDescriptor
#define _Included_net_lunglet_mkl_fft_DftiDescriptor
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    commitDescriptor
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_commitDescriptor
  (JNIEnv *, jclass, jlong);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    computeBackward
 * Signature: (JLjava/nio/Buffer;)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_computeBackward__JLjava_nio_Buffer_2
  (JNIEnv *, jclass, jlong, jobject);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    computeBackward
 * Signature: (JLjava/nio/Buffer;Ljava/nio/Buffer;)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_computeBackward__JLjava_nio_Buffer_2Ljava_nio_Buffer_2
  (JNIEnv *, jclass, jlong, jobject, jobject);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    computeForward
 * Signature: (JLjava/nio/Buffer;)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_computeForward__JLjava_nio_Buffer_2
  (JNIEnv *, jclass, jlong, jobject);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    computeForward
 * Signature: (JLjava/nio/Buffer;Ljava/nio/Buffer;)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_computeForward__JLjava_nio_Buffer_2Ljava_nio_Buffer_2
  (JNIEnv *, jclass, jlong, jobject, jobject);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    createDescriptor
 * Signature: ([JII[I)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_createDescriptor
  (JNIEnv *, jclass, jlongArray, jint, jint, jintArray);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    freeDescriptor
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_freeDescriptor
  (JNIEnv *, jclass, jlong);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    getFloatValue
 * Signature: (JI[J)F
 */
JNIEXPORT jfloat JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getFloatValue
  (JNIEnv *, jclass, jlong, jint, jlongArray);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    getIntArrayValue
 * Signature: (JI[J)[I
 */
JNIEXPORT jintArray JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getIntArrayValue
  (JNIEnv *, jclass, jlong, jint, jlongArray);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    getIntValue
 * Signature: (JI[J)I
 */
JNIEXPORT jint JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getIntValue
  (JNIEnv *, jclass, jlong, jint, jlongArray);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    getStringValue
 * Signature: (JI[J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getStringValue
  (JNIEnv *, jclass, jlong, jint, jlongArray);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    setValue
 * Signature: (JIF)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JIF
  (JNIEnv *, jclass, jlong, jint, jfloat);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    setValue
 * Signature: (JII)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JII
  (JNIEnv *, jclass, jlong, jint, jint);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    setValue
 * Signature: (JI[I)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JI_3I
  (JNIEnv *, jclass, jlong, jint, jintArray);

/*
 * Class:     net_lunglet_mkl_fft_DftiDescriptor
 * Method:    setValue
 * Signature: (JILjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JILjava_lang_String_2
  (JNIEnv *, jclass, jlong, jint, jstring);

#ifdef __cplusplus
}
#endif
#endif
