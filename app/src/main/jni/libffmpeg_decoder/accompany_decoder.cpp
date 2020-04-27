#include "accompany_decoder.h"

#define LOG_TAG "AccompanyDecoder"

AccompanyDecoder::AccompanyDecoder() {

}

AccompanyDecoder::~AccompanyDecoder() {

}


int AccompanyDecoder::getMusicMeta(const char* fileString, int * metaData){
	init(fileString);
	int sampleRate = avCodecContext->sample_rate;
	LOGI("sampleRate is %d", sampleRate);
	int bitRate = avCodecContext->bit_rate;
	LOGI("bitRate is %d", bitRate);
	destroy();
	metaData[0] = sampleRate;
	metaData[1] = bitRate;
	return 0;
}

int AccompanyDecoder::init(const char* audioFile) {
	LOGI("enter AccompanyDecoder::init");
	audioBuffer = NULL;
	position = -1.0f;
	audioBufferCursor = 0;
	audioBufferSize = 0;
	swrContext = NULL;
	swrBuffer = NULL;
	swrBufferSize = 0;
	seek_success_read_frame_success = true;
	isNeedFirstFrameCorrectFlag = true;
	firstFrameCorrectionInSecs = 0.0f;

	avcodec_register_all();
	av_register_all();
    avFormatContext = avformat_alloc_context();
	// 打开输入文件
	LOGI("open accompany file %s....", audioFile);
    if(NULL == accompanyFilePath){
		int length = strlen(audioFile);
		accompanyFilePath = new char[length + 1];
		//由于最后一个是'\0' 所以memset的长度要设置为length+1
		memset(accompanyFilePath, 0, length + 1);
		memcpy(accompanyFilePath, audioFile, length + 1);
	}

    int result = avformat_open_input(&avFormatContext, audioFile, NULL, NULL);
	if (result != 0) {
		LOGI("can't open file %s result is %d", audioFile, result);
		return -1;
	} else {
		LOGI("open file %s success and result is %d", audioFile, result);
	}
    avFormatContext->max_analyze_duration = 50000;
	//检查在文件中的流的信息
	result = avformat_find_stream_info(avFormatContext, NULL);
	if (result < 0) {
		LOGI("fail avformat_find_stream_info result is %d", result);
		return -1;
	} else {
		LOGI("sucess avformat_find_stream_info result is %d", result);
	}
	stream_index = av_find_best_stream(avFormatContext, AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);
	LOGI("stream_index is %d", stream_index);
	// 没有音频
	if (stream_index == -1) {
		LOGI("no audio stream");
		return -1;
	}

    //音频流
	AVStream* audioStream = avFormatContext->streams[stream_index];
	if (audioStream->time_base.den && audioStream->time_base.num)
		timeBase = av_q2d(audioStream->time_base);
	else if (audioStream->codec->time_base.den && audioStream->codec->time_base.num)
		timeBase = av_q2d(audioStream->codec->time_base);
	//获得音频流的解码器上下文
	avCodecContext = audioStream->codec;
	// 根据解码器上下文找到解码器
	LOGI("avCodecContext->codec_id is %d AV_CODEC_ID_AAC is %d", avCodecContext->codec_id, AV_CODEC_ID_AAC);
	AVCodec * avCodec = avcodec_find_decoder(avCodecContext->codec_id);
	if (avCodec == NULL) {
		LOGI("Unsupported codec ");
		return -1;
	}
    // 打开解码器
	result = avcodec_open2(avCodecContext, avCodec, NULL);
	if (result < 0) {
		LOGI("fail avformat_find_stream_info result is %d", result);
		return -1;
	} else {
		LOGI("sucess avformat_find_stream_info result is %d", result);
	}

    LOGI("channels is %d sampleRate is %d", avCodecContext->channels, avCodecContext->sample_rate);
   	pAudioFrame = av_frame_alloc();
	return 1;
}



void AccompanyDecoder::destroy() {
//	LOGI("start destroy!!!");
	if (NULL != swrBuffer) {
		free(swrBuffer);
		swrBuffer = NULL;
		swrBufferSize = 0;
	}
	if (NULL != swrContext) {
		swr_free(&swrContext);
		swrContext = NULL;
	}
	if (NULL != pAudioFrame) {
		av_free (pAudioFrame);
		pAudioFrame = NULL;
	}
	if (NULL != avCodecContext) {
		avcodec_close(avCodecContext);
		avCodecContext = NULL;
	}
	if (NULL != avFormatContext) {
		LOGI("leave LiveReceiver::destory");
		avformat_close_input(&avFormatContext);
		avFormatContext = NULL;
	}
//	LOGI("end destroy!!!");
}

