package org.hotuganda.collect.android.injection.config;

import org.hotuganda.collect.android.injection.ViewModelBuilder;
import org.hotuganda.collect.android.injection.config.architecture.ViewModelFactoryModule;
import org.hotuganda.collect.android.injection.config.scopes.PerApplication;
import org.hotuganda.collect.android.utilities.AgingCredentialsProvider;
import org.opendatakit.httpclientandroidlib.client.CookieStore;
import org.opendatakit.httpclientandroidlib.client.CredentialsProvider;
import org.opendatakit.httpclientandroidlib.impl.client.BasicCookieStore;

import dagger.Module;
import dagger.Provides;

/**
 * Add Application level providers here, i.e. if you want to
 * inject something into the Collect instance.
 */
@Module(includes = {ViewModelFactoryModule.class, ViewModelBuilder.class})
class AppModule {

    @PerApplication
    @Provides
    CredentialsProvider provideCredentialsProvider() {
        // retain credentials for 7 minutes...
        return new AgingCredentialsProvider(7 * 60 * 1000);
    }

    @PerApplication
    @Provides
    CookieStore provideCookieStore() {
        // share all session cookies across all sessions.
        return new BasicCookieStore();
    }
}
