package com.tendyron.routewifi.core.app.update;

import com.tendyron.routewifi.core.app.App;

/**
 * Created by Neo on 2017/1/4.
 * <p>
 * 检查程序的更新情况
 *
 * @author Neo
 */
public interface UpdateChecker {

    String getVersion();

    String getIcon();

    String getName();

}
