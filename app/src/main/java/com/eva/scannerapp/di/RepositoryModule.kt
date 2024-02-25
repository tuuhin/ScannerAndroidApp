package com.eva.scannerapp.di

import com.eva.scannerapp.data.repository.ImageReaderRepoImpl
import com.eva.scannerapp.domain.repository.ImageReaderRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

	@Binds
	@ViewModelScoped
	abstract fun bindsReaderRepo(impl: ImageReaderRepoImpl): ImageReaderRepo

}