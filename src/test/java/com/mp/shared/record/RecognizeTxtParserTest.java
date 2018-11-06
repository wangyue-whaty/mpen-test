package com.mp.shared.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hsqldb.lib.IntIndex;
import org.junit.Assert;
import org.junit.Test;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.mp.shared.record.RecognizeTxtParser.RecognizeInfo;
import com.mp.shared.record.YZSResult.Line;
import com.mp.shared.record.YZSResult.Word;

public class RecognizeTxtParserTest {
    // 云知声原始数据
    public  final String userRecognizeTxt = "{\"EvalType\" : \"general\",\"lines\" : [{\"begin\" : 0.0,\"end\" : 3.761,\"fluency\" : 74.552,\"integrity\" : 100.0,\"pronunciation\" : 71.748,\"sample\" : \"Do you want to go to Chinatown?\",\"score\" : 71.888,\"usertext\" : \"Do you want to go to Chinatown\",\"words\" : [{\"begin\" : 0.081,\"end\" : 0.211,\"score\" : 5.265,\"text\" : \"sil\",\"type\" : 4,\"volume\" : 1.535},{\"StressOfWord\" : 1,\"begin\" : 0.211,\"end\" : 0.321,\"phonetic\" : \"'duː\",\"score\" : 4.114,\"text\" : \"Do\",\"type\" : 2,\"volume\" : 7.861},{\"StressOfWord\" : 0,\"begin\" : 0.321,\"end\" : 0.441,\"phonetic\" : \"jə\",\"score\" : 9.206,\"text\" : \"you\",\"type\" : 2,\"volume\" : 9.0},{\"StressOfWord\" : 1,\"begin\" : 0.441,\"end\" : 0.801,\"phonetic\" : \"'wɒnt\",\"score\" : 8.637,\"text\" : \"want\",\"type\" : 2,\"volume\" : 6.725},{\"begin\" : 0.801,\"end\" : 0.831,\"score\" : 3.301,\"text\" : \"sil\",\"type\" : 4,\"volume\" : 2.464},{\"StressOfWord\" : 1,\"begin\" : 0.831,\"end\" : 0.961,\"phonetic\" : \"'tuː\",\"score\" : 6.692,\"text\" : \"to\",\"type\" : 2,\"volume\" : 7.983},{\"StressOfWord\" : 1,\"begin\" : 0.961,\"end\" : 1.241,\"phonetic\" : \"'gəʊ\",\"score\" : 9.139,\"text\" : \"go\",\"type\" : 2,\"volume\" : 7.541},{\"StressOfWord\" : 1,\"begin\" : 1.241,\"end\" : 1.541,\"phonetic\" : \"'tuː\",\"score\" : 7.554,\"text\" : \"to\",\"type\" : 2,\"volume\" : 7.137},{\"StressOfWord\" : 1,\"begin\" : 1.541,\"end\" : 2.441,\"phonetic\" : \"'tʃaɪnəˌtaʊn\",\"score\" : 3.694,\"text\" : \"Chinatown\",\"type\" : 2,\"volume\" : 7.477},{\"begin\" : 2.441,\"end\" : 3.761,\"score\" : 4.479,\"text\" : \"sil\",\"type\" : 4,\"volume\" : 1.698}]}],\"score\" : 71.888,\"version\" : \"full 1.0\"}";
    // 驰声评测数据
    public  final String chivoxUserRecognizeTxt="{\"recordId\":\"5b34bcad7b6ccf3ad40cd268\",\"tokenId\":\"5b34bcac33279301e3000003\",\"applicationId\":\"1522231290000044\",\"audioUrl\":\"download.cloud.chivox.com:8002\\/5b34bcad7b6ccf3ad40cd268\",\"dtLastResponse\":\"2018-06-28 18:47:15:736\",\"params\":{\"app\":{\"timestamp\":\"1530182828\",\"sig\":\"c67cc9c1fd2245f7588bb0c66394c71a8442e9d4\",\"applicationId\":\"1522231290000044\",\"userId\":\"KTNW_Android\",\"clientId\":\"16503cb6dc450fa4\"},\"request\":{\"coreType\":\"en.sent.score\",\"tokenId\":\"5b34bcac33279301e3000003\",\"refText\":\"It's my leg.\",\"rank\":100,\"attachAudioUrl\":1},\"audio\":{\"sampleBytes\":2,\"channel\":1,\"sampleRate\":16000,\"audioType\":\"ogg\"}},\"eof\":1,\"refText\":\"It's my leg.\",\"result\":{\"useref\":1,\"version\":\"0.0.80.2018.1.18.15:21:41\",\"rank\":100,\"res\":\"eng.snt.G4.N1.0.2\",\"integrity\":100,\"forceout\":0,\"pron\":75,\"info\":{\"snr\":22.813347,\"trunc\":0,\"clip\":0.044581,\"volume\":8981,\"tipId\":10005},\"textmode\":0,\"wavetime\":3580,\"en_prob\":1,\"pretime\":80,\"delaytime\":68,\"usehookw\":0,\"overall\":76,\"is_en\":1,\"rhythm\":{\"stress\":67,\"overall\":81,\"sense\":100,\"tone\":100},\"accuracy\":80,\"details\":[{\"fluency\":0,\"dur\":420,\"endindex\":3,\"beginindex\":0,\"end\":1380,\"stressref\":0,\"toneref\":0,\"stressscore\":0,\"char\":\"it's\",\"start\":960,\"sensescore\":0,\"senseref\":0,\"tonescore\":0,\"indict\":1,\"score\":70},{\"fluency\":0,\"dur\":380,\"endindex\":6,\"beginindex\":5,\"end\":1760,\"stressref\":0,\"toneref\":0,\"stressscore\":0,\"char\":\"my\",\"start\":1380,\"sensescore\":0,\"senseref\":0,\"tonescore\":0,\"indict\":1,\"score\":100},{\"fluency\":0,\"dur\":780,\"endindex\":10,\"beginindex\":8,\"end\":2540,\"stressref\":0,\"toneref\":0,\"stressscore\":1,\"char\":\"leg\",\"start\":1760,\"sensescore\":0,\"senseref\":1,\"tonescore\":0,\"indict\":1,\"score\":70}],\"fluency\":{\"pause\":0,\"overall\":69,\"speed\":190},\"systime\":746}}";

    /*
     * 云知声或驰声原始数据转化测试
     */
    @Test
    public void testParseYZSOrChivox() {
        Map<String, ArrayList<RecognizeInfo>> evaluationDataMap = new HashMap<>();
        // 云知声结果预计输出
        final ArrayList<RecognizeInfo> wordsList01 = new ArrayList<>();
        wordsList01.add(this.getRecognizeInfo("sil", 4, (float) 5.265));
        wordsList01.add(this.getRecognizeInfo("Do", 2, (float) 4.114));
        wordsList01.add(this.getRecognizeInfo("you", 2, (float) 9.206));
        wordsList01.add(this.getRecognizeInfo("want", 2, (float) 8.637));
        wordsList01.add(this.getRecognizeInfo("sil", 4, (float) 3.301));
        wordsList01.add(this.getRecognizeInfo("to", 2, (float) 6.692));
        wordsList01.add(this.getRecognizeInfo("go", 2, (float) 9.139));
        wordsList01.add(this.getRecognizeInfo("to", 2, (float) 7.554));
        wordsList01.add(this.getRecognizeInfo("Chinatown", 2, (float) 3.694));
        wordsList01.add(this.getRecognizeInfo("sil", 4, (float) 4.479));
        evaluationDataMap.put("wordsList01", wordsList01);
        // 驰声评测结果预计输出
        final ArrayList<RecognizeInfo> wordsList02 = new ArrayList<>();
        wordsList02.add(this.getRecognizeInfo("it's", 0, (float) 70.0));
        wordsList02.add(this.getRecognizeInfo("my", 0, (float) 100));
        wordsList02.add(this.getRecognizeInfo("leg", 0, (float) 70.0));
        evaluationDataMap.put("wordsList02", wordsList02);
        String[] evaluationData = { userRecognizeTxt, chivoxUserRecognizeTxt };
        for (int idx = 1; idx <= 2; idx++) {
            // 调用被测试的方法
            final ArrayList<RecognizeInfo> parseYZSOrChivox = RecognizeTxtParser
                    .ParseYZSOrChivox(evaluationData[idx - 1]);
            // 判断和预计输出的是否一致
            for (int i = 0; i < parseYZSOrChivox.size(); i++) {
                Assert.assertEquals(evaluationDataMap.get("wordsList0" + idx).get(i).text, parseYZSOrChivox.get(i).text);
                Assert.assertEquals(String.valueOf(evaluationDataMap.get("wordsList0" + idx).get(i).score),
                        String.valueOf(parseYZSOrChivox.get(i).score));
                Assert.assertEquals(evaluationDataMap.get("wordsList0" + idx).get(i).type, parseYZSOrChivox.get(i).type);
            }
        }
    }

    /*
     * 单词信息
     */
    public RecognizeInfo getRecognizeInfo(String text, int type, float score) {
        final RecognizeInfo recognizeInfo = new RecognizeInfo();
        recognizeInfo.text = text;
        recognizeInfo.type = type;
        recognizeInfo.score = score;
        return recognizeInfo;
    }
}
