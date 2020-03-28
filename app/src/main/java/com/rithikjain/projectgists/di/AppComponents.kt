package com.rithikjain.projectgists.di

import com.rithikjain.projectgists.di.modules.apiModule
import com.rithikjain.projectgists.di.modules.repoModule
import com.rithikjain.projectgists.di.modules.roomModule

val appComponent = listOf(roomModule, apiModule, repoModule)