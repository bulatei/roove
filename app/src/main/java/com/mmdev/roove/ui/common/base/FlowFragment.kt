/*
 * Created by Andrii Kovalchuk
 * Copyright (c) 2020. All rights reserved.
 * Last modified 09.03.20 17:23
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.mmdev.roove.ui.common.base

import androidx.fragment.app.Fragment
import androidx.navigation.NavController

/**
 * This is the documentation block about the class
 */

abstract class FlowFragment(layoutId: Int = 0): Fragment(layoutId) {

	internal lateinit var navController: NavController

}