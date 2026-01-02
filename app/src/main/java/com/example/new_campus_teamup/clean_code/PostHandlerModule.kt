package com.example.new_campus_teamup.clean_code

import androidx.annotation.InspectableProperty
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PostTypeKey(val value : PostType)

@Module
@InstallIn(SingletonComponent::class)
abstract class PostHandlerModule  {

    @Binds
    @IntoMap
    @PostTypeKey(PostType.ROLE)
    abstract fun providesRoleHandler(roleHandler: RoleHandler) : BasePostHandler

    @Binds
    @IntoMap
    @PostTypeKey(PostType.VACANCY)
    abstract fun providesVacancyHandler(vacancyHandler: VacancyHandler) : BasePostHandler

    @Binds
    @IntoMap
    @PostTypeKey(PostType.PROJECT)
    abstract fun providesProjectHandler(projectHandler: ProjectHandler) : BasePostHandler


}