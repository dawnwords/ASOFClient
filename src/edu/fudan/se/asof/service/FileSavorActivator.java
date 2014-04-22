package edu.fudan.se.asof.service;

import android.widget.Toast;
import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.ReturnType;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Dawnwords on 2014/4/22.
 */
public class FileSavorActivator extends AbstractService {

    @Override
    protected ReturnType invoke(Object... input) {
        String path = (String) input[0];
        byte[] fileContent = (byte[]) input[1];
        String name = (String) input[2];

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path + File.separator + name);
            fos.write(fileContent);
            fos.flush();
            toast("Save File Success!");
        } catch (Exception e) {
            String msg = "Save File Error:" + e.getMessage();
            toast(msg);
        } finally {
            close(fos);
        }
        return null;
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
