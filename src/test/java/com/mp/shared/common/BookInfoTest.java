package com.mp.shared.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by feng on 3/6/17.
 *
 * pure junit test
 * 因为这些定义可能会需要和后台共享
 *
 * 测试 getFileName， retrieveFromFileName
 */

public final class BookInfoTest {
    @Test
    public void testFileName() {
        final String[][] testData = {
            {   // valid downloading name, no Shcode
                "downloading-book-downloadSize_12345.9d766c23-15d6-4f55-aad1-a627bae84662.ResVer_1_1",
                "12345", "9d766c23-15d6-4f55-aad1-a627bae84662",
                "ResVer_1_1", "0"
            },
            {   // valid downloading name, 1 Shcode
                    "downloading-book-downloadSize_12345.9d766c23-15d6-4f55-aad1-a627bae84662.ResVer_1_1.SH_60113_0",
                    "12345", "9d766c23-15d6-4f55-aad1-a627bae84662",
                    "ResVer_1_1", "1",
            },
            {   // valid downloading name, 2 Shcode
                    "downloading-book-downloadSize_54321.a4a27e0b-8683-40a5-b120-f9caae848f61.ResVer_1_2.SH_60113_0.SH_601133_16",
                    "54321", "a4a27e0b-8683-40a5-b120-f9caae848f61",
                    "ResVer_1_2", "2",
            },
            {   // valid downloaded name, no Shcode
                    "book.9d766c23-15d6-4f55-aad1-a627bae84662.ResVer_1_1",
                    "-1", "9d766c23-15d6-4f55-aad1-a627bae84662",
                    "ResVer_1_1", "0"
            },
            {   // valid downloaded name, 1 Shcode
                    "book.9d766c23-15d6-4f55-aad1-a627bae84662.ResVer_1_1.SH_60113_0",
                    "-1", "9d766c23-15d6-4f55-aad1-a627bae84662",
                    "ResVer_1_1", "1",
            },
            {   // valid downloaded name, 2 Shcode
                    "book.a4a27e0b-8683-40a5-b120-f9caae848f61.ResVer_1_2.SH_60113_0.SH_601133_16",
                    "-1", "a4a27e0b-8683-40a5-b120-f9caae848f61",
                    "ResVer_1_2", "2",
            },
        };
        for (final String[] test: testData) {
            final String fn = test[0];
            final long downloadSize = Long.parseLong(test[1]);
            //final UUID id = UUID.fromString(test[2]);
            final String id = test[2];
            final ResourceVersion version = ResourceVersion.fromString(test[3]);
            final int numCodes = Integer.parseInt(test[4]);
            final BookInfo bookInfo = BookInfo.retrieveFromFileName(fn);
            assertEquals(downloadSize, bookInfo.downloadSize);
            assertEquals(id, bookInfo.id);
            assertTrue(version.compareTo(bookInfo.version) == 0);
            assertEquals(numCodes, bookInfo.coverCodes == null ? 0 : bookInfo.coverCodes.length);
            assertEquals(fn, bookInfo.getFileName(downloadSize > 0));
        }
        // test bad input won't crash
        final String[] badParse = {
                // downloadSize incorrect
                "downloading-book-downloadSize_12345g.9d766c23-15d6-4f55-aad1-a627bae84662.ResVer_1_1.SH_60113_0",
                // id incorrect
                "downloading-book-downloadSize_12345g.9d766c2315d64f55aad1a627bae84662.ResVer_1_1.SH_60113_0",
                // version mission
                "downloading-book-downloadSize_12345g.9d766c2315d64f55aad1a627bae84662.SH_60113_0",
                // version incorrect
                "book.9d766c23-15d6-4f55-aad1-a627bae84662.ResVer_1",
                // code incorrect
                "book.9d766c23-15d6-4f55-aad1-a627bae84662.ResVer_1.SH_60113_0.SH_60113_k",
        };
        for (final String bad: badParse) {
            assertEquals(null, BookInfo.retrieveFromFileName(bad));
        }
    }
}
