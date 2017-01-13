/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package iflytek.pagedemo.http;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestParams implements Serializable{
    private static String ENCODING = "UTF-8";

    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, FileWrapper> fileParams;

    /**
     * Constructs a new empty <code>RequestParams</code> instance.
     */
    public RequestParams() {
        init();
    }

    /**
     * Constructs a new RequestParams instance containing the key/value
     * string params from the specified map.
     * @param source the source key/value string map to add.
     */
    public RequestParams(Map<String, String> source) {
        init();

        for(Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string param.
     * @param key the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public RequestParams(String key, String value) {
        init();

        put(key, value);
    }

    /**
     * Constructs a new RequestParams instance and populate it with multiple
     * initial key/value string param.
     * @param keysAndValues a sequence of keys and values. Objects are
     * automatically converted to Strings (including the value {@code null}).
     * @throws IllegalArgumentException if the number of arguments isn't even.
     */
    public RequestParams(Object... keysAndValues) {
      init();
      int len = keysAndValues.length;
      if (len % 2 != 0)
        throw new IllegalArgumentException("Supplied arguments must be even");
      for (int i = 0; i < len; i += 2) {
        String key = String.valueOf(keysAndValues[i]);
        String val = String.valueOf(keysAndValues[i + 1]);
        put(key, val);
      }
    }

    /**
     * Adds a key/value string pair to the request.
     * @param key the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value){
        if(key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * Adds a file to the request.
     * @param key the key name for the new param.
     * @param file the file to add.
     */
    public void put(String key, File file) throws FileNotFoundException {
        if(key != null && file != null) {
           fileParams.put(key, new FileWrapper(key, file));
        }
    }

    /**
     * Adds param with more than one value.
     * @param key the key name for the new param.
     * @param values is the ArrayList with values for the param.
     */
//    public void put(String key, ArrayList<String> values)  {
//        if(key != null && values != null) {
//            urlParamsWithArray.put(key, values);
//        }
//    }

    /**
     * Adds an input stream to the request.
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     */
    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    /**
     * Adds an input stream to the request.
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param fileName the name of the file.
     */
    public void put(String key, InputStream stream, String fileName) {
        put(key, stream, fileName, null);
    }

    /**
     * Adds an input stream to the request.
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param fileName the name of the file.
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, InputStream stream, String fileName, String contentType) {
        if(key != null && stream != null) {
            fileParams.put(key, new FileWrapper(key, stream, fileName, contentType));
        }
    }

    /**
     * Removes a parameter from the request.
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key){
        urlParams.remove(key);
        fileParams.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if(result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for(ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            if(result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        return result.toString();
    }


    public void addMap(Map<String, String> map) {
    	if (urlParams != null) {
    		map.putAll(urlParams);
    	}
    }

    public void addMapFiles(Map<String, FileWrapper> map) {
    	if (fileParams != null) {
    		map.putAll(fileParams);
    	}
    }
    
    private void init(){
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
//        urlParamsWithArray = new ConcurrentHashMap<String, ArrayList<String>>();
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

        for(ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return lparams;
    }

    protected String getParamString() {
        return URLEncodedUtils.format(getParamsList(), ENCODING);
    }

    public static class FileWrapper {
    	public String key = "";
        public InputStream inputStream = null;
        public String fileName = "";
        public String contentType = "";
        public File   file = null;

        public FileWrapper(String key, InputStream inputStream, String fileName, String contentType) {
        	this.key  = key;
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.contentType = contentType;
        }
        
        public FileWrapper(String key, File file) {
        	this.key  = key;
        	this.file = file;
        }

        public String getFileName() {
            if(fileName != null) {
                return fileName;
            } else {
                return "nofilename";
            }
        }
    }
}
