package com.mp.shared;

/**
 * Created by feng on 3/6/17.
 *
 * Inlcude other tests in the path
 */
import com.mp.shared.common.BookInfoTest;
import com.mp.shared.common.HotAreaTest;
import com.mp.shared.common.MpCodeTest;
import com.mp.shared.common.ResourceVersionTest;
import com.mp.shared.common.ShCodeTest;
import com.mp.shared.record.TaskRecordTest;
import com.mp.shared.service.GameParserTest;
import com.mp.shared.utils.JsonRecordReaderTest;
import com.mp.shared.utils.VersionedFile;
import com.mp.shared.utils.VersionedFileTest;
import com.mp.shared.utils.VersionedRecordProcessorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//JUnit Suite Test
@RunWith(Suite.class)

@Suite.SuiteClasses({
        ShCodeTest.class, HotAreaTest.class, BookInfoTest.class,
        ResourceVersionTest.class, MpCodeTest.class,
        TaskRecordTest.class, GameParserTest.class,
        VersionedFileTest.class, JsonRecordReaderTest.class,
        VersionedRecordProcessorTest.class,
})
public final class SharedTests {
}
