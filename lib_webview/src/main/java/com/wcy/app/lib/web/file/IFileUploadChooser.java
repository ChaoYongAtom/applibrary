package com.wcy.app.lib.web.file;

import android.content.Intent;

public interface IFileUploadChooser {



    void openFileChooser();

    void fetchFilePathFromIntent(int requestCode, int resultCode, Intent data);
}
