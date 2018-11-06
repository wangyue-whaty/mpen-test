package com.mp.shared.common;

import java.io.Serializable;

/**
 * Created by feng on 2/23/17.
 *
 * 解码图像后获取的解码结果
 */

public final class Code implements Serializable, IsEqual<Code> {
    private static final long serialVersionUID = 533027021165270010L;

    public enum Type {
        SH, MP, MPR, OID3, POSITION, BARCODE, UNKNOWN
    };

    /**
     * 告诉这个码是一般码还是特别码比如 电子字典，无线，
     */
    public enum Property {
        REGULAR, DICT, WUXIAN, LEARN_WORD
    };

    public Type type;
    public Property property;

    public boolean isSpecialBookInfo() {
        return property != null && (property == Property.DICT || property == Property.LEARN_WORD);
    }

    public ShCode shCode;
    public MpCode mpCode;
    public MpCode.Point mpPoint;
    public long mprValue = -1;

    public Object extra;  // 用来传输额外数据（debug，或者其它目的）
    // internal computation data
    public float prx = 0;
    public float pry = 0;

    /**
     * Detail print out each part of MPR
     */
    public static String getMprStr(long mprValue) {
        if (mprValue <= 0) {
            return Long.toString(mprValue);
        }
        final long prefaceCode = mprValue / 1000000;
        final long pageNum = (mprValue % 1000000) / 1000;
        final long inPagePos = (mprValue % 1000) / 10;
        return String.format("%d:%d:%d", prefaceCode, pageNum, inPagePos);
    }

    /**
     * check if Code has valid content:
     * -- for SH, OID3: need to  have shCode != null
     * -- for MP: mpCode != null, prx, pry has value
     * -- for MPR: mprValue >= 0
     */
    public boolean hasValid() {
        if (type == null) {
            return false;
        }
        switch (type) {
            case SH:
            case OID3:
                return shCode != null;
            case MPR:
                return mprValue >= 0;
            case MP:
                return mpCode != null && !(prx == 0 && pry == 0);
            case POSITION:
                return mpPoint != null;
            case BARCODE:
            case UNKNOWN:
            default:
                return false;
        }
    }

    @Override
    public boolean isEqual(Code other) {
        if (this == other) {
            return true;
        } else if (type != other.type) {
            if ((type == Type.SH || type == Type.OID3)
                    && (other.type == Type.SH || other.type == Type.OID3)) {
                // do nothing
            } else {
                return false;
            }
        }
        if (type == null) {
            return true;
        }
        switch (type) {
            case SH:
            case OID3:
                return Utils.isEqual(shCode, other.shCode);
            case MPR:
                return mprValue == other.mprValue;
            case MP:
                if (mpCode == null && other.mpCode == null) {
                    return Utils.isEqual(mpPoint, other.mpPoint);
                } else {
                    return mpCode == other.mpCode || (mpCode != null && other.mpCode != null && mpCode.compareTo(other.mpCode) == 0);
                }
            default: // BARCODE, UNKNOWN
                return true;
        }
    }

    /**
     * @return more meaningful string description
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Code{");
        sb.append(type);
        if (type != null) {
            switch (type) {
                case SH:
                case OID3:
                    sb.append(shCode);
                    break;
                case MPR:
                    sb.append(mprValue);
                    break;
                case MP:
                    sb.append(" code:").append(mpCode);
                    sb.append(" point:").append(mpPoint);
                    sb.append(" originalX:").append(prx);
                    sb.append(" originalY:").append(pry);
                    break;
                default: // BARCODE, UNKNOWN
                    sb.append(type);
                    break; // do nothing
            }
        }
        sb.append('}');
        return sb.toString();
    }

    /**
     * @return short string description
     */
    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        if (type != null) {
            switch (type) {
                case SH:
                case OID3:
                    sb.append(shCode);
                    break;
                case MPR:
                    sb.append("MPR_").append(mprValue);
                    break;
                case MP:
                    sb.append(mpCode);
                    break;
                default: // BARCODE, UNKNOWN
                    sb.append(type);
                    break; // do nothing
            }
        }
        return sb.toString();
    }

    /**
     * Code构造
     * @param shCode
     * @param property
     * @return
     */
    public static final Code newShCode(ShCode shCode, Property property) {
        final Code code = new Code();
        code.type = Code.Type.SH;
        code.shCode = shCode;
        code.property = property;
        return code;
    }
}