/**
 *
 */
package org.cambi.test.run;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.springframework.core.io.Resource;

/**
 * @author luca
 *
 */
public class JsonDataSetLoader extends AbstractDataSetLoader
{

    @Override
    protected IDataSet createDataSet(Resource resource) throws Exception
    {
        return new JSONDataSet(resource.getInputStream());
    }

}
