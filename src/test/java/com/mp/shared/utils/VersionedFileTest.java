package com.mp.shared.utils;

import com.mp.shared.common.Utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by feng on 4/15/17.
 */

public final class VersionedFileTest {
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void test() throws IOException {
        final File directory = folder.newFolder();
        final String NAME = "records";
        {   // test create files
            final VersionedFile vf = new VersionedFile(directory.getAbsolutePath(), NAME);
            assertTrue(Utils.isEmpty(vf.getAll()));
            for (int idx = 0; idx < 5; ++idx) {
                final String numstr = String.format("%07d", idx);
                final String nextName = NAME + VersionedFile.SEP + numstr;
                assertEquals(nextName, new File(vf.getNext()).getName());
                FUtils.stringToFile(vf.getNext(), numstr, false);
                assertEquals(idx + 1, vf.getAll().length);
                assertEquals(nextName, new File(vf.get()).getName());
                //System.out.println(directory.getAbsolutePath() + " --> " + vf.get());
                //System.out.println(new File(vf.get()).getAbsolutePath());
            }
        }
        final String NAME2 = "records2";
        {   // another file should still be empty
            final VersionedFile vf2 = new VersionedFile(directory.getAbsolutePath(), NAME2);
            assertTrue(Utils.isEmpty(vf2.getAll()));
        }
    }
}
