package com.mp.shared.common;

import java.io.Serializable;


/**
 * Created by feng on 3/3/17.
 * <p>
 * 定义了每本书资源包
 * <p>
 * 需要定义一个 sqllite的数据表，把每条信息放进去。这样可以方便查询：
 * <p>
 * 使用 getFileName 和 retrieveFromFileName 来生成相应文件名
 */
public final class BookInfo implements Serializable, IsValid {
    private static final long serialVersionUID = -3515343075847343747L;
    /** 支持云点读图书类型: 1 */
    public static final int CLOUD_READ_TYPE = 1;
    /** 仅支持本地点读图书类型: 2 */
    public static final int LOCAL_READ_TYPE = 2;
    // TODO 找到一个更有效率的unique id，最好短一些，long 或者 int
    public String id; // unique ID, a special format of UUID string, one without -
    
    public ShCode[] coverCodes; // related SongHan cover codes
    public String name; // book name
    // TODO 老孔包中名称不正确，无法作为app显示名称，定义临时文件名使用数据库中书名
    public String fullName;
    
    public long downloadSize = -1;
    public boolean hasSpeakingEvaluation; //是否支持口语评测
    public boolean hasVideo;//是否支持视频
    // 是否支持云点读
    public int isLineRead;
    public boolean canCloudRead() { return isLineRead == CLOUD_READ_TYPE; }
    // 是否仅支持本地点读
    public boolean localReadOnly() { return isLineRead == LOCAL_READ_TYPE; }
    
    public ResourceVersion version;

    /**
     * check to see if localPath is valid and contain a local file name
     */
    public String getLocalPath() {
        return expansion == null ? null : expansion.getLocalPath(this);
    }
    public boolean hasLocal() {
        final String localPath = getLocalPath();
        return localPath != null && localPath.startsWith("/");
    }
    public void setLocalPath(String path) {
        if (expansion != null) {
            expansion.setLocalPath(this, path);
        }
    }
    // TODO 资源文件头需要提供：其它信息
    public String[] languages; // 这本书支持的语言种类
    /**
     * 这本书包含的松翰码范围
     */
    public ShCode.Range[] shCodeRanges;

    /**
     * 以下可选信息
     */
    public String publisher;
    
    public static BookInfoExpansion expansion;

    // //////////////////////////////////////////////////////////

    /**
     * @return a new copy with the id and version to be set and same; everything else is null
     *         for network call purpose
     */
    public BookInfo cloneSkeleton() {
        final BookInfo bookInfo = new BookInfo();
        bookInfo.id = this.id;
        bookInfo.version = this.version;
        return bookInfo;
    }

    /**
     * 文件名里面包括关键信息，这样可以通过文件名还原、定位BookInfo －－ 这里文件名没有包含 numPages，name －－
     * 设定是：BookInfo的完整信息会在数据库或者别的配置文件里面有
     *
     * @param isDownloading
     * @return
     */
    public String getFileName(boolean isDownloading) {
        if (!isValid()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (isDownloading) {
            sb.append(DOWNLOADING_PREFIX).append(downloadSize).append('.');
        } else {
            sb.append(DOWNLOADED_PREFIX);
        }
        sb.append(id);
        sb.append('.').append(version);
        if (coverCodes != null) {
            for (final ShCode shCode : coverCodes) {
                sb.append('.').append(shCode);
            }
        }
        return sb.toString();
    }

    private static final String DOWNLOADING_PREFIX = "downloading-book-downloadSize_";
    private static final String DOWNLOADED_PREFIX = "book.";

    /**
     * 从文件名解析BookInfo的关键信息
     *
     * @param baseFilename 文件名，应该是从getFileName生成的
     * @return BookInfo 如果能够正确解析；null如果不能
     */
    public static BookInfo retrieveFromFileName(String baseFilename) {
        if (baseFilename == null) {
            return null;
        }
        final BookInfo bookInfo = new BookInfo();
        final String[] parts = baseFilename.split("\\.");
        final int numPart = parts.length;
        if (numPart < 3) {
            return null;
        }
        try {
            if (baseFilename.startsWith(DOWNLOADING_PREFIX)) {
                bookInfo.downloadSize = Long.parseLong(parts[0].substring(DOWNLOADING_PREFIX.length()));
            } else if (!baseFilename.startsWith(DOWNLOADED_PREFIX)) {
                return null;
            }
            // get id
            /* it's a special type of UUID string, one without -
             * to parse it, you will need to do:
             * String plain = "f424376fe38e496eb77d7841d915b074";
             * String uuid = String.format("%1$-%2$-%3$-%4$", plain.substring(0,7), 
             *     plain.substring(7,11), plain.substring(11,15), plain.substring(15,20));
             */
            bookInfo.id = parts[1]; //UUID.fromString(parts[1]);
            // get version
            bookInfo.version = ResourceVersion.fromString(parts[2]);
            // get any ShCode
            final int codesStartPos = 3;
            final int numCodes = numPart - codesStartPos;
            bookInfo.coverCodes = new ShCode[numCodes];
            for (int idx = 0; idx < numCodes; ++idx) {
                final ShCode shCode = ShCode.fromString(parts[idx + codesStartPos]);
                if (shCode == null) {
                    return null;
                }
                bookInfo.coverCodes[idx] = shCode;
            }
            if (bookInfo.id != null && bookInfo.version != null) {
                return bookInfo;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 是否数据合法
     * @return
     */
    @Override
    public boolean isValid() {
        return !(id == null || id.length() == 0 || version == null);
    }
    
    public interface BookInfoExpansion {
        String getLocalPath(BookInfo bookInfo);
        ResourceVersion getLocalVersion(BookInfo bookInfo);
        void setLocalPath(BookInfo bookInfo, String localPath);
    }
}
