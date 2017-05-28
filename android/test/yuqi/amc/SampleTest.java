package yuqi.amc;

import org.junit.runners.model.TestClass;

/**
 * Created by yuqi on 28/5/17.
 */

public class SampleTest extends TestClass {

    /**
     * Creates a {@code TestClass} wrapping {@code clazz}. Each time this
     * constructor executes, the class is scanned for annotations, which can be
     * an expensive process (we hope in future JDK's it will not be.) Therefore,
     * try to share instances of {@code TestClass} where possible.
     *
     * @param clazz
     */
    public SampleTest(Class<?> clazz) {
        super(clazz);
    }
}
