package com.shug.httputil.client;

import com.shug.httputil.client.exception.HttpProcessException;
import java.io.UnsupportedEncodingException;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
public class ResponseContent {

    private ResponseContent() {}

    private String encoding;

    private byte[] contentBytes;

    private String content;

    private int statusCode;

    private String contentType;

    public String getEncoding() {
        return encoding;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() throws HttpProcessException  {
        try {
            if (content != null || contentBytes == null)
                return content;
            return content = new String(contentBytes, this.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new HttpProcessException(e);
        }
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public int getStatusCode() {
        return statusCode;
    }


    public static class Builder {
        private String _encoding = "utf-8";
        private String _contentType = "";
        private int _statusCode = 200;
        private byte[] _contentBytes;

        public Builder encoding(String encoding) {
            _encoding = encoding;
            return this;
        }

        public Builder contentType(String contentType) {
            _contentType = contentType;
            return this;
        }

        public Builder statusCode(int statuCode) {
            _statusCode = statuCode;
            return this;
        }

        public Builder contentBytes(byte[] contentBytes) {
            _contentBytes = contentBytes;
            return this;
        }

        public ResponseContent build() {
            ResponseContent content = new ResponseContent();
            content.encoding = _encoding;
            content.contentType = _contentType;
            content.statusCode = _statusCode;
            content.contentBytes = _contentBytes;
            return  content;
        }
    }
}
