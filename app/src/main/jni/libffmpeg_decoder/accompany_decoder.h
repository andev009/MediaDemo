//
// Created by kyle on 2020-03-05.
//

#ifndef MEDIADEMO_ACCOMPANY_DECODER_H
#define MEDIADEMO_ACCOMPANY_DECODER_H

extern "C" {
    	#include "3rdparty/ffmpeg/include/libavcodec/avcodec.h"
    	#include "3rdparty/ffmpeg/include/libavformat/avformat.h"
    	#include "3rdparty/ffmpeg/include/libavutil/avutil.h"
    	#include "3rdparty/ffmpeg/include/libavutil/samplefmt.h"
    	#include "3rdparty/ffmpeg/include/libavutil/common.h"
    	#include "3rdparty/ffmpeg/include/libavutil/channel_layout.h"
    	#include "3rdparty/ffmpeg/include/libavutil/opt.h"
    	#include "3rdparty/ffmpeg/include/libavutil/log.h"
    	#include "3rdparty/ffmpeg/include/libavutil/imgutils.h"
    	#include "3rdparty/ffmpeg/include/libavutil/mathematics.h"
    	#include "3rdparty/ffmpeg/include/libswscale/swscale.h"
    	#include "3rdparty/ffmpeg/include/libswresample/swresample.h"
};

#include "./common/common.h"

class AccompanyDecoder {
private:
/** 如果使用了快进或者快退命令，则先设置以下参数 **/
	bool seek_req;
	bool seek_resp;
	float seek_seconds;

	float actualSeekPosition;

	AVFormatContext* avFormatContext;
	AVCodecContext * avCodecContext;
	int stream_index;
	float timeBase;
	AVFrame *pAudioFrame;
	AVPacket packet;

	char* accompanyFilePath;

	bool seek_success_read_frame_success;
	int packetBufferSize;

	/** 每次解码出来的audioBuffer以及这个audioBuffer的时间戳以及当前类对于这个audioBuffer的操作情况 **/
	short* audioBuffer;
	float position;
	int audioBufferCursor;
	int audioBufferSize;
	float duration;
	bool isNeedFirstFrameCorrectFlag;
	float firstFrameCorrectionInSecs;

	SwrContext *swrContext;
	void *swrBuffer;
	int swrBufferSize;
	int init(const char* fileString);

public:
	AccompanyDecoder();
	virtual ~AccompanyDecoder();

	virtual void destroy();

	int getMusicMeta(const char* fileString, int * metaData);
};

#endif //MEDIADEMO_ACCOMPANY_DECODER_H
