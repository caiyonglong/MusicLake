package com.cyl.musiclake.api;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Author   : D22434
 * version  : 2018/1/22
 * function :
 */

public class FileResponseBody<T> extends ResponseBody {
    /**
     * 实际请求体
     */
    private ResponseBody mResponseBody;
    /**
     * BufferedSource
     */
    private BufferedSource mBufferedSource;

    private ProgressListener listener;

    public FileResponseBody(ResponseBody responseBody, ProgressListener listener) {
        super();
        this.mResponseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mResponseBody.source();
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long bytesReader = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReader += bytesRead == -1 ? 0 : bytesRead;
                //实时发送当前已读取的字节和总字节
                listener.onLoading(bytesReader, mResponseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
