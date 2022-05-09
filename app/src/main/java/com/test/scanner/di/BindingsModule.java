package com.test.scanner.di;

import com.test.scanner.navigation.FragmentManagerRouterImpl;
import com.test.scanner.navigation.Router;
import com.test.scanner.utils.android.AndroidStrings;
import com.test.scanner.utils.android.Clipboard;
import com.test.scanner.utils.android.ClipboardImpl;
import com.test.scanner.utils.android.Permissions;
import com.test.scanner.utils.android.PermissionsImpl;
import com.test.scanner.utils.android.Strings;
import com.test.scanner.utils.android.Toaster;
import com.test.scanner.utils.android.ToasterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class BindingsModule {

    @Binds
    public abstract Router bindRouter(FragmentManagerRouterImpl impl);

    @Binds
    public abstract Permissions bindPermissions(PermissionsImpl impl);

    @Binds
    public abstract Clipboard bindClipboard(ClipboardImpl impl);

    @Binds
    public abstract Toaster bindToaster(ToasterImpl impl);

    @Binds
    public abstract Strings bindStrings(AndroidStrings impl);

}
