package com.example.new_campus_teamup.clean_code_1

import com.example.new_campus_teamup.clean_code.PostType
import com.example.new_campus_teamup.clean_code.PostTypeKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap


@Module
@InstallIn(SingletonComponent::class)
abstract class ViewPostHandlerModule {


    @Binds
    @IntoMap
    @PostTypeKey(PostType.ROLE)
    abstract fun providesViewRoleHandler(viewRoleHandler: ViewRoleHandler) : ViewPostHandler

    @Binds
    @IntoMap
    @PostTypeKey(PostType.VACANCY)
    abstract fun providesViewVacancyHandler(viewVacancyHandler: ViewVacancyHandler) : ViewPostHandler

    @Binds
    @IntoMap
    @PostTypeKey(PostType.PROJECT)
    abstract fun providesViewProjectHandler(viewProjectHandler: ViewProjectHandler) : ViewPostHandler
}
