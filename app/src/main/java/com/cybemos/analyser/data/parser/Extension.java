package com.cybemos.analyser.data.parser;

import android.support.annotation.StringRes;

import com.cybemos.analyser.data.Util;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class Extension {

    /**
     * characters after "." in a file
     */
    private final String extension;

    /**
     * the string representation of the extension
     */
    @StringRes
    private final int name;

    public Extension(String extension, @StringRes int name) {
        this.extension = extension;
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    @StringRes
    public int getName() {
        return name;
    }

    @Override
    public String toString() {
        return Util.context.getString(name);
    }

    @Override
    public boolean equals(Object obj) {
        boolean ret = false;
        if (obj instanceof Extension) {
            Extension extension = (Extension) obj;
            ret = getExtension().equals(extension.getExtension());
        }
        return ret;
    }
}
