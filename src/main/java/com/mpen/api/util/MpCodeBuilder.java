package com.mpen.api.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mp.lib.so.MpenOidDisorderAndOrder;
import com.mp.shared.common.MpCode;
import com.mp.shared.common.PageInfo;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.Book.DotParam;
import com.mpen.api.bean.Book.SubPageParam;
import com.mpen.api.bean.SonixCode;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.domain.DdbResourcePageCode;
import com.mpen.api.domain.DdbResourcePageScope;
import com.mpen.api.exception.SdkException;
import com.sun.media.imageio.plugins.tiff.BaselineTIFFTagSet;
import com.sun.media.imageio.plugins.tiff.TIFFDirectory;
import com.sun.media.imageio.plugins.tiff.TIFFField;
import com.sun.media.imageio.plugins.tiff.TIFFTag;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;

/**
 * 设置自定义dpi值到TIFF格式图像的样例程序。 需要 JAI_ImageIO_1.1 提供支持。 A4 210×297=0.7 9920 14171
 * 630 9353 13361 594 8000 11428 7500 10714 8500 12142 8800 12572 9100 13000
 */
public class MpCodeBuilder {
    private static final Logger LOGGER = Logger.getLogger(MpCodeBuilder.class);

    private static final float LENGTH_PRE = 47.24f;
    private static final String COMPRESSION = "CCITT T.6";
    private static final int DPI_X = 1200;
    private static final int DPI_Y = 1200;

    private static int indexPoint = 0;

    private int mPointOffset = 4;// 每个点的偏移
    private int mPointSize = 4;// 每个点的宽度
    private int mPointNum = 5;// 点阵大小，想改成其他的只需要修改 mPointNum 这个值就行了
    private int mOffset = 16; // 点距
    private int padding;
    private int mTopOffset = 7; // 中心点位置
    private int mLeftOffset = 7; // 中心点位置
    private int type;// 扰乱所需的参数

    private BufferedImage mImage;
    private String mFileName;
    private String mFilePath;
    private String mFileSavePath;

    private long endCode;
    private long startCode;
    private int quadrateH;
    private int xCodeNum;
    private int yCodeNum;
    private Graphics g;
    private int x;
    private int y;
    private int startPointX;
    private int startPointY;
    private Map<Integer, List<SonixCode>> sonixCodeMap;
    private CodeType codeType = CodeType.MP;

    // 加 了这句才可以正常执行， 不然ImageIO.getImageWritersByFormatName("TIFF"); 会找不到writter
    {
        ImageIO.scanForPlugins();
    }

    public MpCodeBuilder(DdbResourceBook book) {
        try {
            mFileSavePath = FileUtils.getBookFolderSavePath(FileUtils.BOOK_TIF, book.getId());
            this.mFilePath = FileUtils.getFileSaveRealPath(mFileSavePath);
            if (StringUtils.isNotBlank(book.getSonixFilePath())) {
                sonixCodeMap = getSonixMap(FileUtils.getFileSaveRealPath(book.getSonixFilePath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化该类的一些生成tif信息
     * 
     * @param book
     * @throws EntityException
     */
    public void init(Book.PageParam pageParam, long startCode) {
        final DotParam codeParam = pageParam.getDotParam();
        this.mPointNum = codeParam.getMatrixSize();
        this.mOffset = codeParam.getDotDistanceInpixels();
        this.mPointSize = codeParam.getDotSize();
        this.mPointOffset = codeParam.getDotShift();
        this.padding = codeParam.getMatrixGap();
        this.quadrateH = mOffset * (mPointNum + padding);
        this.mTopOffset = this.mOffset / 2 - 1;
        this.mLeftOffset = this.mOffset / 2 - 1;
        this.type = mPointNum - 5;
        this.startCode = startCode;
    }

    /**
     * 生成点读码对外的方法， 包括整个逻辑流程， 返回tif的相对路径
     * 
     * @throws SdkException
     * 
     */
    public DdbResourcePageScope createCode(long code, int sign, Book.PageParam pageParam, DdbResourcePageCode page) {
        // 创建画板
        final int width = (int) (page.getWidth() * LENGTH_PRE);
        final int height = (int) (page.getHeight() * LENGTH_PRE);
        createGraphics(width, height);
        init(pageParam, code);
        SubPageParam[] subPages = pageParam.getSubPages();
        if (subPages == null || subPages.length == 0) {
            // 默认为全页铺码
            subPages = new SubPageParam[1];
            subPages[0] = new SubPageParam(pageParam.getDotParam().getMargin()[1],
                pageParam.getDotParam().getMargin()[0], page.getHeight() - pageParam.getDotParam().getMargin()[3],
                page.getWidth() - pageParam.getDotParam().getMargin()[2], 0);
        }
        final PageInfo.SubPageInfo[] subPageInfos = new PageInfo.SubPageInfo[subPages.length];
        for (int i = 0; i < subPages.length; i++) {
            final int left = (int) (subPages[i].getLeft() * LENGTH_PRE);
            final int top = (int) (subPages[i].getTop() * LENGTH_PRE);
            final int right = (int) (subPages[i].getRight() * LENGTH_PRE);
            final int bottom = (int) (subPages[i].getBottom() * LENGTH_PRE);
            startPointX = left;
            startPointY = top;
            this.xCodeNum = (right - left) / quadrateH;
            this.yCodeNum = (bottom - top) / quadrateH;
            final int marginRight = width - xCodeNum * quadrateH - left;
            final int marginBottom = height - yCodeNum * quadrateH - top;
            final int[] margin = { left, top, marginRight, marginBottom };
            subPageInfos[i] = new PageInfo.SubPageInfo(this.xCodeNum, this.yCodeNum,
                new MpCode(this.startCode, 0, (byte) this.mPointNum), margin, subPages[i].getNum());
            // 生成点读码
            final int xpadding = (right - xCodeNum * quadrateH - left) / mOffset;
            final int ypadding = (bottom - yCodeNum * quadrateH - top) / mOffset;
            create(xpadding, ypadding);
        }
        if (this.sonixCodeMap != null && this.sonixCodeMap.size() > 0) {
            final List<SonixCode> sonixCode = sonixCodeMap.get(page.getPageNum());
            if (sonixCode != null && sonixCode.size() > 0) {
                shRepalce(sonixCode);
            }
        }
        g.dispose();
        // tif的文件名称
        mFileName = page.getName() + "__id" + sign + "__" + codeType + FileType.TIF.suffix;
        // 写入到硬盘
        write();
        return new DdbResourcePageScope(code, this.endCode, mFileSavePath + mFileName, sign, page.getId(), padding,
            mPointNum, mOffset, mPointSize, mPointOffset, Constants.ONE, Constants.GSON.toJson(subPageInfos));
    }

    /**
     * 生成画板
     */
    private void createGraphics(int width, int height) {
        mImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        // 设置背景颜色
        final Graphics2D dg = (Graphics2D) mImage.createGraphics();
        dg.setBackground(Color.WHITE);
        dg.fillRect(0, 0, width, height);
        dg.dispose();
        g = ((BufferedImage) mImage).createGraphics();
        g.setColor(Color.black);
    }

    /**
     * 生成不同码的核心逻辑
     * 
     * @throws SdkException
     * 
     */
    private void create(int xpadding, int ypadding) {
        // 不足一个码值部分用黑点补足
        for (int y = 0; y <= yCodeNum; y++) {
            for (int x = 0; x <= xCodeNum; x++, startCode++) {
                this.x = x;
                this.y = y;
                if (x == xCodeNum && y != yCodeNum) {
                    for (int yy = 0; yy < mPointNum + padding; yy++) {
                        for (int xx = 0; xx < xpadding; xx++) {
                            createPiont(xx, yy, PointType.RANDOM_LOCATION_POINT);
                        }
                    }
                    startCode--;
                    continue;
                } else if (x == xCodeNum && y == yCodeNum) {
                    for (int yy = 0; yy < ypadding; yy++) {
                        for (int xx = 0; xx < xpadding; xx++) {
                            createPiont(xx, yy, PointType.RANDOM_LOCATION_POINT);
                        }
                    }
                    startCode--;
                    continue;
                } else if (x != xCodeNum && y == yCodeNum) {
                    for (int yy = 0; yy < ypadding; yy++) {
                        for (int xx = 0; xx < mPointNum + padding; xx++) {
                            createPiont(xx, yy, PointType.RANDOM_LOCATION_POINT);
                        }
                    }
                    startCode--;
                    continue;
                }
                final byte[] codes = new MpCode(MpenOidDisorderAndOrder.mpenDisturb(startCode, type), 0,
                    (byte) mPointNum).toByteVal();
                if (isEven(codes)) {
                    createPiont(mPointNum - 2, 0, PointType.EVEN_LOCATION_POINT);
                } else {
                    createPiont(mPointNum - 2, 0, PointType.ODD_LOCATION_POINT);
                }
                indexPoint = 0; // 标志位归零，重新开始标记所在的位置
                for (int yy = 0; yy < mPointNum + padding; yy++) { // X轴
                    for (int xx = 0; xx < mPointNum + padding; xx++) { // Y轴
                        if (xx < mPointNum && yy < mPointNum) {
                            // 第一排
                            if (yy % mPointNum == 0) { // 边界
                                if (xx % mPointNum == 2) {
                                    createPiont(xx, yy, codes);// 第一排只有一个含义点，就是它了
                                } else if (xx % mPointNum == mPointNum - 2) {
                                    continue;// 这个是奇偶校验位，上面已经生成好啦
                                } else {
                                    createPiont(xx, yy, PointType.ORDINARY_LOCATION_POINT); // 生成标志位，生成定位点都调用这个方法
                                }
                                // 第二排 第三排 ...
                            } else {
                                if (xx == yy) { // 对角线都是标志位
                                    if (yy % mPointNum + 2 == mPointNum) {
                                        createPiont(xx, yy, PointType.OBLIQUE_LOCATION_POINT); // 倒数第二排生成斜轴标志位，斜轴上倒数第二个标志点有点特殊，在这里处理
                                    } else {
                                        createPiont(xx, yy, PointType.ORDINARY_LOCATION_POINT); // 生成标志位，这里就是对角线了
                                    }
                                } else if (yy % mPointNum + 1 == mPointNum && xx % mPointNum == 0) {
                                    createPiont(xx, yy, PointType.ORDINARY_LOCATION_POINT); // 最后一排第一个点为标志位
                                } else {
                                    createPiont(xx, yy, codes);// 其他情况都是数据点了
                                }
                            }
                        } else {
                            createPiont(xx, yy, PointType.RANDOM_LOCATION_POINT); // 绘制padding
                        }
                    }
                }
                this.endCode = startCode;
            }
        }
    }

    /**
     * 嵌入松翰码
     */
    private void shRepalce(List<SonixCode> sonixList) {
        // SH码覆盖
        if (sonixList != null && sonixList.size() > 0) {
            this.codeType = CodeType.SM;
            for (SonixCode sonix : sonixList) {
                final List<String> list = sonix.getList();
                for (int j = 0; j < list.size(); j++) {
                    char[] charArray = list.get(j).toCharArray();
                    for (int i = 0; i < charArray.length; i++) {
                        if (charArray[i] == '0') {
                            g.setColor(Color.WHITE);
                            g.fillRect(Integer.valueOf(sonix.getX()) + i, Integer.valueOf(sonix.getY()) + j, 1, 1);
                        } else if (charArray[i] == '1') {
                            g.setColor(Color.black);
                            g.fillRect(Integer.valueOf(sonix.getX()) + i, Integer.valueOf(sonix.getY()) + j, 1, 1);
                        }
                    }
                }
            }
        }
    }

    public static boolean isEven(byte[] mValues) {
        int sum = 0;
        for (byte b : mValues) {
            sum += b;
        }
        return ((sum % 2) == 0);
    }

    /**
     * 按照自己需要的点数生成相应的点 ，其他多余的 标记为0
     * 
     */
    public void createPiont(int xx, int yy, byte[] codes) {
        indexPoint++;
        if (indexPoint <= codes.length) {
            createPiont(xx, yy, PointType.values()[codes[indexPoint - 1]]);
        } else { // 绘制多余的点 全部标记成0
            createPiont(xx, yy, PointType.ZERO_LOCATION_POINT);
        }
    }

    /**
     * 写入到硬盘
     */
    private void write() {
        FileImageOutputStream outStream = null;
        try {
            File file = new File(mFilePath, mFileName);
            file.getParentFile().mkdirs();
            final Iterator writers = ImageIO.getImageWritersByFormatName(FileType.TIF.typeName);
            if (writers == null || !writers.hasNext()) {
                throw new IllegalStateException("No TIFF writers!");
            }
            final ImageWriter writer = (ImageWriter) writers.next();
            final ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromRenderedImage(mImage);
            final ImageWriteParam writerParams = writer.getDefaultWriteParam();
            writerParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writerParams.setCompressionType(COMPRESSION);
            writerParams.setCompressionQuality(0.5f);
            final ColorModel colorModel = ColorModel.getRGBdefault(); // 指定压缩时使用的色彩模式
            writerParams.setDestinationType(
                new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
            IIOMetadata imageMetadata = writer.getDefaultImageMetadata(imageType, writerParams);
            imageMetadata = setDPIViaAPI(imageMetadata);
            outStream = new FileImageOutputStream(file);
            writer.setOutput(outStream);
            writer.write(new IIOImage(mImage, null, imageMetadata));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set DPI using API.
     */
    private IIOMetadata setDPIViaAPI(IIOMetadata imageMetadata) throws IIOInvalidTreeException {
        IIOMetadata metadata = null;
        // Derive the TIFFDirectory from the metadata.
        final TIFFDirectory dir = TIFFDirectory.createFromMetadata(imageMetadata);

        // Get {X,Y}Resolution tags.
        final BaselineTIFFTagSet base = BaselineTIFFTagSet.getInstance();
        final TIFFTag tagXRes = base.getTag(BaselineTIFFTagSet.TAG_X_RESOLUTION);
        final TIFFTag tagYRes = base.getTag(BaselineTIFFTagSet.TAG_Y_RESOLUTION);

        // Create {X,Y}Resolution fields.
        final TIFFField fieldXRes = new TIFFField(tagXRes, TIFFTag.TIFF_RATIONAL, 1, new long[][] { { DPI_X, 1 } });
        final TIFFField fieldYRes = new TIFFField(tagYRes, TIFFTag.TIFF_RATIONAL, 1, new long[][] { { DPI_Y, 1 } });

        // Append {X,Y}Resolution fields to directory.
        dir.addTIFFField(fieldXRes);
        dir.addTIFFField(fieldYRes);

        // Convert to metadata object.
        metadata = dir.getAsMetadata();

        // Add other metadata.
        final IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        final IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(25.4f / 1200));
        final IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(25.4f / 1200));
        final IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);

        // Convert to metadata object and return.
        return metadata;
    }

    /*
     * 绘制0，1 2 3 对应的坐标 生成有意义的数据点 createPiont(xx, yy, listPageNum, listY, listX);
     * 生成普通定位点 createPiont(xx, yy, -1); 生成斜轴上倒数第二个特殊定位点 createPiont(xx, yy, -2);
     */

    public enum PointType {
        ZERO_LOCATION_POINT, ONE_LOCATION_POINT, TWO_LOCATION_POINT, THREE_LOCATION_POINT, OBLIQUE_LOCATION_POINT, ORDINARY_LOCATION_POINT, EVEN_LOCATION_POINT, ODD_LOCATION_POINT, RANDOM_LOCATION_POINT
    }

    private void createPiont(int xx, int yy, PointType value) {
        // 计算真实的高度
        xx = startPointX + mOffset * (xx + x * (mPointNum + padding)) + mLeftOffset;
        yy = startPointY + mOffset * (yy + y * (mPointNum + padding)) + mTopOffset;

        switch (value) {

        case OBLIQUE_LOCATION_POINT: // 绘制斜线上的个数定位点
            g.fillRect(xx + 4, yy + 4, mPointSize, mPointSize);
            break;

        case ORDINARY_LOCATION_POINT: // 绘制普通定位点
            g.fillRect(xx, yy, mPointSize, mPointSize);
            break;

        case ZERO_LOCATION_POINT: // 绘制0
            g.fillRect(xx - mPointOffset, yy - mPointOffset, mPointSize, mPointSize);

            break;
        case ONE_LOCATION_POINT: // 绘制1
            g.fillRect(xx + mPointOffset, yy - mPointOffset, mPointSize, mPointSize);
            break;
        case TWO_LOCATION_POINT: // 绘制2
            g.fillRect(xx + mPointOffset, yy + mPointOffset, mPointSize, mPointSize);
            break;
        case THREE_LOCATION_POINT: // 绘制3
            g.fillRect(xx - mPointOffset, yy + mPointOffset, mPointSize, mPointSize);
            break;
        case EVEN_LOCATION_POINT: // 绘制偶位，右移一点
            g.fillRect(xx + 4, yy, mPointSize, mPointSize);
            break;
        case ODD_LOCATION_POINT: // 绘制奇位，左移一点
            g.fillRect(xx - 4, yy, mPointSize, mPointSize);
            break;
        case RANDOM_LOCATION_POINT: // 绘制随机点
            if ((yy + xx) % 2 == 0) {
                g.fillRect(xx - 2, yy, mPointSize, mPointSize);
            } else {
                g.fillRect(xx + 2, yy, mPointSize, mPointSize);
            }
            break;
        default:
            break;
        }
    }

    public enum FileType {
        PNG("png", ".png"), TIF("tiff", ".tif");
        String typeName;
        String suffix;

        private FileType(String typeName, String suffix) {
            this.typeName = typeName;
            this.suffix = suffix;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getSuffix() {
            return suffix;
        }

    }

    public static void changeFileType(String srcFilePath, FileType srcFileType, String desFilePath,
        FileType desFileType) throws SdkException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(desFilePath);
            ImageDecoder decoder = ImageCodec.createImageDecoder(srcFileType.typeName, new File(srcFilePath), null);
            ImageEncoder encoder = ImageCodec.createImageEncoder(desFileType.typeName, out, null);
            encoder.encode(decoder.decodeAsRenderedImage());
        } catch (Exception e) {
            throw new SdkException(Constants.GET_INFO_FAILURE);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new SdkException(Constants.GET_INFO_FAILURE);
            }
        }
    }

    private static Map<Integer, List<SonixCode>> getSonixMap(String srcFile) {
        FileReader fr = null;
        BufferedReader br = null;
        Map<Integer, List<SonixCode>> map = null;
        try {
            map = new HashMap<Integer, List<SonixCode>>();
            fr = new FileReader(srcFile);
            br = new BufferedReader(fr);
            String str = null;
            while ((str = br.readLine()) != null) {
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                if (!str.startsWith("#")) {
                    String[] split = str.split(",");
                    final String page = split[0].split("=")[1];
                    // codeList存放的是
                    List<SonixCode> codeList = map.get(Integer.parseInt(page));
                    if (codeList == null) {
                        codeList = new ArrayList<SonixCode>();
                        map.put(Integer.parseInt(page), codeList);
                    }
                    final SonixCode code = new SonixCode();
                    code.setPage(Integer.valueOf(split[0].split("=")[1]));
                    if ("all_replace".equals(split[1].split("=")[0])) {
                        continue;
                    }
                    code.setX(split[1].split("=")[1]);
                    code.setY(split[2].split("=")[1]);
                    code.setWidth(split[3].split("=")[1]);
                    code.setHeight(split[4].split("=")[1]);
                    final List<String> list = new ArrayList<String>();
                    String temp = split[5].split("=")[1];
                    while (temp.length() > 0) {
                        String substring = temp.substring(0, Integer.valueOf(split[3].split("=")[1]));
                        list.add(substring);
                        temp = temp.substring(Integer.valueOf(split[3].split("=")[1]));
                    }
                    code.setList(list);
                    codeList.add(code);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("松翰码文件信息错误！");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    
    public enum CodeType {
        MP, SH, SM
    }

}